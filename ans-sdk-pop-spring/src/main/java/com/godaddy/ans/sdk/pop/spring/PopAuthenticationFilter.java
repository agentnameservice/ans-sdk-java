package com.godaddy.ans.sdk.pop.spring;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import com.godaddy.ans.sdk.pop.CallerOptions;
import com.godaddy.ans.sdk.pop.CallerPolicy;
import com.godaddy.ans.sdk.pop.CallerVerifier;
import com.godaddy.ans.sdk.pop.PopException;
import com.godaddy.ans.sdk.pop.PopHttp;
import com.godaddy.ans.sdk.pop.ReplayCache;
import com.godaddy.ans.sdk.transparency.scitt.StatusToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

    private static final Logger LOG = LoggerFactory.getLogger(PopAuthenticationFilter.class);

    private final CallerVerifier verifier;
    private final Supplier<Map<String, PublicKey>> rootKeys;
    private final ReplayCache replay;
    private final Function<HttpServletRequest, String> externalUrl;
    private final CallerPolicy policy;

    // Package-private for tests: lets a test inject a stubbed verifier and a pre-built policy.
    PopAuthenticationFilter(CallerVerifier verifier, Supplier<Map<String, PublicKey>> rootKeys,
                                    ReplayCache replay, Function<HttpServletRequest, String> externalUrl,
                                    CallerPolicy policy) {
        this.verifier = verifier;
        this.rootKeys = rootKeys;
        this.replay = replay;
        this.externalUrl = externalUrl;
        this.policy = policy;
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

        if (!checkAuthority(request)) {
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

        CallerOptions options = CallerOptions.none();
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
            identity = verifier.verifyCaller(proof, headers, request.getMethod(),
                resolveUrl(request), keys, replay, options);
        } catch (PopException e) {
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
        filterChain.doFilter(request, response);
    }

    private boolean checkAuthority(HttpServletRequest request) {
        if (policy.trustsAnyAuthority()) {
            return true;
        }
        return policy.authorityTrusted(deriveAuthority(request));
    }

    private String deriveAuthority(HttpServletRequest request) {
        if (externalUrl != null) {
            try {
                return new URI(externalUrl.apply(request)).getAuthority();
            } catch (URISyntaxException e) {
                return null;
            }
        }
        String authority = request.getHeader("Host");
        return authority != null ? authority : request.getServerName();
    }

    private String resolveUrl(HttpServletRequest request) {
        if (externalUrl != null) {
            return externalUrl.apply(request);
        }
        StringBuffer url = request.getRequestURL();
        String query = request.getQueryString();
        return query == null ? url.toString() : url.append('?').append(query).toString();
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

    public static final class Builder {

        private final String expectedIssuer;
        private final Supplier<Map<String, PublicKey>> rootKeys;
        private final ReplayCache replay;
        private final CallerPolicy.Builder policy = CallerPolicy.builder();
        private boolean trustedHostsSet;
        private Function<HttpServletRequest, String> externalUrl;
        private Duration popSkew;

        private Builder(String expectedIssuer, Supplier<Map<String, PublicKey>> rootKeys, ReplayCache replay) {
            this.expectedIssuer = Objects.requireNonNull(expectedIssuer, "expectedIssuer");
            this.rootKeys = Objects.requireNonNull(rootKeys, "rootKeys");
            this.replay = Objects.requireNonNull(replay, "replay");
        }

        /**
         * Sets a function that maps a request to its external URL. The filter uses the result as
         * the {@code htu} (HTTP target URI) that PoP proofs bind to.
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

        public PopAuthenticationFilter build() {
            if (externalUrl == null && !trustedHostsSet) {
                LOG.warn("htu will be derived from the client-controlled Host header; "
                    + "set withExternalUrl or withTrustedHosts before production");
            }
            CallerVerifier verifier = popSkew != null
                ? CallerVerifier.create(expectedIssuer, StatusToken.DEFAULT_CLOCK_SKEW, popSkew)
                : CallerVerifier.create(expectedIssuer);
            return new PopAuthenticationFilter(verifier, rootKeys, replay, externalUrl, policy.build());
        }

    }
}