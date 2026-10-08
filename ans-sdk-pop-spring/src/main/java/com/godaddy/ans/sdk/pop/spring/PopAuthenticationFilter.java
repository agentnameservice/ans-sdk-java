package com.godaddy.ans.sdk.pop.spring;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import com.godaddy.ans.sdk.pop.CallerOptions;
import com.godaddy.ans.sdk.pop.CallerPolicy;
import com.godaddy.ans.sdk.pop.CallerVerifier;
import com.godaddy.ans.sdk.pop.ContentSource;
import com.godaddy.ans.sdk.pop.PopException;
import com.godaddy.ans.sdk.pop.PopHttp;
import com.godaddy.ans.sdk.pop.ReplayCache;
import com.godaddy.ans.sdk.pop.RootKeyRefresher;
import com.godaddy.ans.sdk.transparency.scitt.StatusToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class PopAuthenticationFilter extends OncePerRequestFilter {

    /**
     * Default bound on the request content read for {@code ans_content_digest}
     * verification (ANS-6 §7.13): 1 MiB.
     */
    public static final long DEFAULT_MAX_CONTENT_BYTES = 1024 * 1024;

    private static final int SC_CONTENT_TOO_LARGE = 413;
    private static final Logger LOG = LoggerFactory.getLogger(PopAuthenticationFilter.class);

    private final CallerVerifier verifier;
    private final Supplier<Map<String, PublicKey>> rootKeys;
    private final ReplayCache replay;
    private final Function<HttpServletRequest, String> externalUrl;
    private final CallerPolicy policy;
    private final long maxContentBytes;

    // Package-private for tests: lets a test inject a stubbed verifier and a pre-built policy.
    PopAuthenticationFilter(CallerVerifier verifier, Supplier<Map<String, PublicKey>> rootKeys,
                            ReplayCache replay, Function<HttpServletRequest, String> externalUrl,
                            CallerPolicy policy, long maxContentBytes) {
        this.verifier = verifier;
        this.rootKeys = rootKeys;
        this.replay = replay;
        this.externalUrl = externalUrl;
        this.policy = policy;
        this.maxContentBytes = maxContentBytes;
    }

    public static Builder builder(String expectedIssuer, Supplier<Map<String, PublicKey>> rootKeys,
                                  ReplayCache replay) {
        return new Builder(expectedIssuer, rootKeys, replay);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        Map<String, List<String>> headers = headerMap(request);

        Optional<String> duplicate = policy.duplicateSecurityHeader(headers);
        if (duplicate.isPresent()) {
            LOG.info("caller rejected: MALFORMED_PROOF - duplicate {} header", duplicate.get());
            reject(response);
            return;
        }

        String url = resolveUrl(request);
        if (!checkAuthority(url)) {
            LOG.info("caller rejected: HTTP_BINDING_MISMATCH - request authority is not trusted");
            reject(response);
            return;
        }

        String proof = request.getHeader(PopHttp.DPOP_HEADER);
        if (proof == null || proof.isBlank()) {
            LOG.info("caller rejected: MISSING_HEADERS - no DPoP proof on request");
            reject(response);
            return;
        }

        BoundedBody body = new BoundedBody(request, maxContentBytes);
        CallerOptions options = CallerOptions.none().withReceivedContent(body);
        Optional<String> accessToken = PopHttp.accessTokenFromAuthorization(request.getHeader("Authorization"));
        if (accessToken.isPresent()) {
            options = options.withAccessToken(accessToken.get());
        }

        Map<String, PublicKey> keys;
        try {
            keys = rootKeys.get();
        } catch (RuntimeException e) {
            LOG.error("caller rejected: MISCONFIGURED - root keys unavailable: {}", e.getMessage());
            reject(response);
            return;
        }

        CallerIdentity identity;
        try {
            identity = verifier.verifyCaller(proof, headers, request.getMethod(), url, keys, replay, options);
        } catch (PopException e) {
            if (e.getCause() instanceof ContentTooLargeException) {
                LOG.info("caller rejected: request content exceeds {} bytes", maxContentBytes);
                response.sendError(SC_CONTENT_TOO_LARGE, "payload too large");
                return;
            }
            LOG.info("caller rejected: {} - {}", e.category(), e.getMessage());
            reject(response);
            return;
        } catch (RuntimeException e) {
            LOG.error("caller rejected: unexpected verification error", e);
            reject(response);
            return;
        }

        if (!policy.callerAllowed(identity)) {
            LOG.info("caller rejected: EXPECTED_PEER_MISMATCH - caller ans host is not in the accepted set");
            reject(response);
            return;
        }

        request.setAttribute(PopAuthentication.CALLER_ATTRIBUTE, identity);
        filterChain.doFilter(body.replay(request), response);
    }

    private boolean checkAuthority(String url) {
        if (policy.trustsAnyAuthority()) {
            return true;
        }
        return policy.authorityTrusted(authorityOf(url));
    }

    // The allowlist is checked on the same URL the proof's htu is compared against, so
    // the two can never read different sources (ANS-6 §7.7).
    private static String authorityOf(String url) {
        try {
            return new URI(url).getAuthority();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private String resolveUrl(HttpServletRequest request) {
        if (externalUrl != null) {
            return externalUrl.apply(request);
        }
        return request.getRequestURL().toString();
    }

    private static Map<String, List<String>> headerMap(HttpServletRequest request) {
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            List<String> values = new ArrayList<>();
            Enumeration<String> headerValues = request.getHeaders(name);
            while (headerValues.hasMoreElements()) {
                values.add(headerValues.nextElement());
            }
            headers.put(name, values);
        }
        return headers;
    }

    private static void reject(HttpServletResponse response) throws IOException {
        response.setHeader("WWW-Authenticate", PopHttp.DPOP_HEADER);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized");
    }

    /**
     * Reads the request content once, bounded, when the verifier asks for it
     * (ANS-6 §7.4 step 12), and afterwards hands the handler a request that
     * replays the same bytes.
     */
    static final class BoundedBody implements ContentSource {

        private final HttpServletRequest request;
        private final long maxBytes;
        private byte[] content;

        BoundedBody(HttpServletRequest request, long maxBytes) {
            this.request = request;
            this.maxBytes = maxBytes;
        }

        @Override
        public byte[] read() throws IOException {
            if (content == null) {
                if (request.getContentLengthLong() > maxBytes) {
                    throw new ContentTooLargeException(maxBytes);
                }
                byte[] read = request.getInputStream().readNBytes((int) maxBytes + 1);
                if (read.length > maxBytes) {
                    throw new ContentTooLargeException(maxBytes);
                }
                content = read;
            }
            return content;
        }

        HttpServletRequest replay(HttpServletRequest original) {
            return content == null ? original : new BufferedContentRequest(original, content);
        }
    }

    static final class ContentTooLargeException extends IOException {

        private static final long serialVersionUID = 1L;

        ContentTooLargeException(long maxBytes) {
            super("request content exceeds " + maxBytes + " bytes");
        }
    }

    static final class BufferedContentRequest extends HttpServletRequestWrapper {

        private final byte[] content;

        BufferedContentRequest(HttpServletRequest request, byte[] content) {
            super(request);
            this.content = content;
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ByteArrayServletInputStream(content);
        }

        @Override
        public BufferedReader getReader() {
            String encoding = getCharacterEncoding();
            Charset charset = encoding != null ? Charset.forName(encoding) : StandardCharsets.ISO_8859_1;
            return new BufferedReader(new InputStreamReader(getInputStream(), charset));
        }

        @Override
        public int getContentLength() {
            return content.length;
        }

        @Override
        public long getContentLengthLong() {
            return content.length;
        }
    }

    private static final class ByteArrayServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream in;

        ByteArrayServletInputStream(byte[] content) {
            this.in = new ByteArrayInputStream(content);
        }

        @Override
        public int read() {
            return in.read();
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return in.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return in.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException("asynchronous reads are not supported");
        }
    }

    public static final class Builder {

        private final String expectedIssuer;
        private final Supplier<Map<String, PublicKey>> rootKeys;
        private final ReplayCache replay;
        private final CallerPolicy.Builder policy = CallerPolicy.builder();
        private boolean trustedHostsSet;
        private Function<HttpServletRequest, String> externalUrl;
        private Duration popSkew;
        private CallerVerifier verifier;
        private RootKeyRefresher rootKeyRefresher;
        private long maxContentBytes = DEFAULT_MAX_CONTENT_BYTES;

        private Builder(String expectedIssuer, Supplier<Map<String, PublicKey>> rootKeys, ReplayCache replay) {
            this.expectedIssuer = Objects.requireNonNull(expectedIssuer, "expectedIssuer");
            this.rootKeys = Objects.requireNonNull(rootKeys, "rootKeys");
            this.replay = Objects.requireNonNull(replay, "replay");
        }

        /**
         * Sets a function that maps a request to its external URL. The filter uses the result as
         * the {@code htu} (HTTP target URI) that PoP proofs bind to, and checks its authority
         * against the trusted hosts when both are configured.
         *
         * <p>The function must vary with the request path. Append the request path - for example
         * {@code request.getRequestURI()} - to the external authority. A function that returns a
         * constant URL, or ignores the path, breaks {@code htu} binding. Every request then
         * produces the same {@code htu}, so a proof no longer binds to a specific request target.
         * This is a security defect.
         *
         * @param externalUrl maps a request to its full external URL, including the request path
         * @return this builder
         */
        public Builder withExternalUrl(Function<HttpServletRequest, String> externalUrl) {
            this.externalUrl = Objects.requireNonNull(externalUrl, "externalUrl");
            return this;
        }

        /**
         * Validates the request authority against a fixed list of trusted hosts.
         *
         * <p>Without {@link #withExternalUrl}, both the authority and the {@code htu} the proof
         * binds to come from the container's view of the request URL (scheme, server name and
         * port). Behind a TLS-terminating proxy that view is the proxy-to-application hop, so
         * callers that signed the public {@code https} URL fail the {@code htu} check; use
         * {@link #withExternalUrl}, or configure forwarded-header handling that only trusts the
         * proxy, for such deployments.
         *
         * @param hosts trusted host authorities
         * @return this builder
         */
        public Builder withTrustedHosts(String... hosts) {
            policy.trustedHosts(hosts);
            if (hosts.length > 0) {
                trustedHostsSet = true;
            }
            return this;
        }

        public Builder withAllowedAnsNames(String... ansNames) {
            policy.allowedAnsNames(ansNames);
            return this;
        }

        public Builder withPoPSkew(Duration popSkew) {
            this.popSkew = Objects.requireNonNull(popSkew, "popSkew");
            return this;
        }

        /**
         * Uses a pre-built verifier, for a non-default SCITT clock skew or an injected SCITT
         * verifier. Mutually exclusive with {@link #withPoPSkew}, which configures the default
         * verifier.
         */
        public Builder withVerifier(CallerVerifier verifier) {
            this.verifier = Objects.requireNonNull(verifier, "verifier");
            return this;
        }

        /**
         * Refreshes the Transparency Log root keys once, and retries verification, when a receipt
         * or status token is signed by a key the callee does not hold (ANS-6 §4.5, §9.5).
         */
        public Builder withRootKeyRefresher(RootKeyRefresher refresher) {
            this.rootKeyRefresher = Objects.requireNonNull(refresher, "refresher");
            return this;
        }

        /**
         * Bounds the request content read for {@code ans_content_digest} verification. Larger
         * requests are rejected with 413 before any content is hashed. Defaults to
         * {@link #DEFAULT_MAX_CONTENT_BYTES}.
         */
        public Builder withMaxContentBytes(long maxContentBytes) {
            if (maxContentBytes < 1 || maxContentBytes >= Integer.MAX_VALUE) {
                throw new IllegalArgumentException(
                    "maxContentBytes must be between 1 and " + (Integer.MAX_VALUE - 1));
            }
            this.maxContentBytes = maxContentBytes;
            return this;
        }

        public PopAuthenticationFilter build() {
            if (externalUrl == null && !trustedHostsSet) {
                throw new IllegalStateException(
                        "htu would be derived from the client-controlled Host header; "
                                + "call withExternalUrl(...) or withTrustedHosts(...) before build()");
            }
            if (verifier != null && popSkew != null) {
                throw new IllegalStateException("withVerifier(...) and withPoPSkew(...) are mutually exclusive");
            }
            CallerVerifier effective = verifier != null ? verifier
                : popSkew != null
                    ? CallerVerifier.create(expectedIssuer, StatusToken.DEFAULT_CLOCK_SKEW, popSkew)
                    : CallerVerifier.create(expectedIssuer);
            if (rootKeyRefresher != null) {
                effective = effective.withRootKeyRefresher(rootKeyRefresher);
            }
            return new PopAuthenticationFilter(effective, rootKeys, replay, externalUrl, policy.build(),
                maxContentBytes);
        }

    }
}
