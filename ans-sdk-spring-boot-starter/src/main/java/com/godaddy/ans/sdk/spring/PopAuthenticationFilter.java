package com.godaddy.ans.sdk.spring;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import com.godaddy.ans.sdk.pop.CallerOptions;
import com.godaddy.ans.sdk.pop.CallerVerifier;
import com.godaddy.ans.sdk.pop.PopException;
import com.godaddy.ans.sdk.pop.PopHttp;
import com.godaddy.ans.sdk.pop.ReplayCache;
import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;
import com.godaddy.ans.sdk.transparency.scitt.StatusToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.PublicKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public final class PopAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(PopAuthenticationFilter.class);

    private static final List<String> SECURITY_HEADERS = List.of(
        PopHttp.DPOP_HEADER, "Authorization",
        ScittHeaders.SCITT_RECEIPT_HEADER, ScittHeaders.STATUS_TOKEN_HEADER);

    private final CallerVerifier verifier;
    private final Supplier<Map<String, PublicKey>> rootKeys;
    private final ReplayCache replay;
    private final Function<HttpServletRequest, String> externalUrl;
    private final Set<String> trustedHosts;
    private final Set<String> allowedHosts;

    // Package-private for tests: lets a test inject a stubbed verifier and pre-resolved host sets.
    PopAuthenticationFilter(CallerVerifier verifier, Supplier<Map<String, PublicKey>> rootKeys,
                                    ReplayCache replay, Function<HttpServletRequest, String> externalUrl,
                                    Set<String> trustedHosts, Set<String> allowedHosts) {
        this.verifier = verifier;
        this.rootKeys = rootKeys;
        this.replay = replay;
        this.externalUrl = externalUrl;
        this.trustedHosts = trustedHosts;
        this.allowedHosts = allowedHosts;
    }

    public static Builder builder(String expectedIssuer, Supplier<Map<String, PublicKey>> rootKeys,
                                  ReplayCache replay) {
        return new Builder(expectedIssuer, rootKeys, replay);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        for (String header : SECURITY_HEADERS) {
            if (countHeaders(request, header) > 1) {
                LOG.info("caller rejected: MALFORMED_PROOF - duplicate {} header", header);
                reject(response);
                return;
            }
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
            identity = verifier.verifyCaller(proof, headerMap(request), request.getMethod(),
                resolveUrl(request), keys, replay, options);
        } catch (PopException e) {
            reject(response);
            return;
        }

        if (!allowedHosts.isEmpty() && !allowedByHost(identity)) {
            LOG.info("caller rejected: EXPECTED_PEER_MISMATCH - caller ans host is not in the accepted set");
            reject(response);
            return;
        }

        request.setAttribute(PopAuthentication.CALLER_ATTRIBUTE, identity);
        filterChain.doFilter(request, response);
    }

    private boolean allowedByHost(CallerIdentity identity) {
        try {
            return allowedHosts.contains(CallerVerifier.ansHost(identity.ansName()));
        } catch (PopException e) {
            return false;
        }
    }

    private boolean checkAuthority(HttpServletRequest request) {
        if (trustedHosts.isEmpty()) {
            return true;
        }
        String authority;
        if (externalUrl != null) {
            try {
                authority = new URI(externalUrl.apply(request)).getAuthority();
            } catch (URISyntaxException e) {
                return false;
            }
        } else {
            authority = request.getHeader("Host");
            if (authority == null) {
                authority = request.getServerName();
            }
        }
        if (authority == null) {
            return false;
        }
        return trustedHosts.contains(normalizeAuthority(authority));
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

    private static int countHeaders(HttpServletRequest request, String name) {
        Enumeration<String> values = request.getHeaders(name);
        int count = 0;
        while (values != null && values.hasMoreElements()) {
            values.nextElement();
            count++;
        }
        return count;
    }

    private static void reject(HttpServletResponse response) throws IOException {
        response.setHeader("WWW-Authenticate", PopHttp.DPOP_HEADER);
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized");
    }

    private static String normalizeAuthority(String host) {
        String normalized = host.trim().toLowerCase(Locale.ROOT);
        if (normalized.endsWith(":443")) {
            return normalized.substring(0, normalized.length() - 4);
        }
        if (normalized.endsWith(":80")) {
            return normalized.substring(0, normalized.length() - 3);
        }
        return normalized;
    }

    public static final class Builder {

        private final String expectedIssuer;
        private final Supplier<Map<String, PublicKey>> rootKeys;
        private final ReplayCache replay;
        private final Set<String> trustedHosts = new HashSet<>();
        private final List<String> allowedNames = new ArrayList<>();
        private Function<HttpServletRequest, String> externalUrl;
        private Duration popSkew;

        private Builder(String expectedIssuer, Supplier<Map<String, PublicKey>> rootKeys, ReplayCache replay) {
            this.expectedIssuer = Objects.requireNonNull(expectedIssuer, "expectedIssuer");
            this.rootKeys = Objects.requireNonNull(rootKeys, "rootKeys");
            this.replay = Objects.requireNonNull(replay, "replay");
        }

        public Builder withExternalUrl(Function<HttpServletRequest, String> externalUrl) {
            this.externalUrl = Objects.requireNonNull(externalUrl, "externalUrl");
            return this;
        }

        public Builder withTrustedHosts(String... hosts) {
            for (String host : hosts) {
                if (host == null) {
                    continue;
                }
                String normalized = normalizeAuthority(host);
                if (!normalized.isEmpty()) {
                    trustedHosts.add(normalized);
                }
            }
            if (hosts.length > 0 && trustedHosts.isEmpty()) {
                throw new IllegalArgumentException("withTrustedHosts: every supplied host was empty");
            }
            return this;
        }

        public Builder withExpectedAnsName(String ansName) {
            allowedNames.add(Objects.requireNonNull(ansName, "ansName"));
            return this;
        }

        public Builder withAllowedAnsNames(String... ansNames) {
            for (String ansName : ansNames) {
                allowedNames.add(Objects.requireNonNull(ansName, "ansName"));
            }
            return this;
        }

        public Builder withPoPSkew(Duration popSkew) {
            this.popSkew = Objects.requireNonNull(popSkew, "popSkew");
            return this;
        }

        public PopAuthenticationFilter build() {
            if (externalUrl != null) {
                probeExternalUrl(externalUrl);
            }
            if (externalUrl == null && trustedHosts.isEmpty()) {
                LOG.warn("htu will be derived from the client-controlled Host header; "
                    + "set withExternalUrl or withTrustedHosts before production");
            }
            Set<String> resolvedAllowed = new HashSet<>();
            for (String ansName : allowedNames) {
                try {
                    resolvedAllowed.add(CallerVerifier.ansHost(ansName));
                } catch (PopException e) {
                    throw new IllegalArgumentException("invalid allowed ans name: " + ansName, e);
                }
            }
            CallerVerifier verifier = popSkew != null
                ? CallerVerifier.create(expectedIssuer, StatusToken.DEFAULT_CLOCK_SKEW, popSkew)
                : CallerVerifier.create(expectedIssuer);
            return new PopAuthenticationFilter(verifier, rootKeys, replay, externalUrl,
                Set.copyOf(trustedHosts), resolvedAllowed);
        }

        private static void probeExternalUrl(Function<HttpServletRequest, String> fn) {
            String first = fn.apply(probeRequest("/pop-probe-a"));
            String second = fn.apply(probeRequest("/pop-probe-b"));
            if (Objects.equals(first, second)) {
                throw new IllegalArgumentException("withExternalUrl function ignores the request path; "
                    + "htu would not bind the request target - append request.getRequestURI() to the authority");
            }
        }

        private static HttpServletRequest probeRequest(String path) {
            InvocationHandler handler = (proxy, method, args) -> {
                switch (method.getName()) {
                    case "getRequestURI":
                    case "getServletPath":
                        return path;
                    case "getRequestURL":
                        return new StringBuffer("https://probe.invalid").append(path);
                    case "getMethod":
                        return "GET";
                    case "getScheme":
                        return "https";
                    case "getServerName":
                        return "probe.invalid";
                    case "getServerPort":
                        return 443;
                    default:
                        break;
                }
                Class<?> returnType = method.getReturnType();
                if (returnType.equals(boolean.class)) {
                    return false;
                }
                if (returnType.equals(int.class)) {
                    return 0;
                }
                if (returnType.equals(long.class)) {
                    return 0L;
                }
                return null;
            };
            return (HttpServletRequest) Proxy.newProxyInstance(
                PopAuthenticationFilter.class.getClassLoader(),
                new Class<?>[] {HttpServletRequest.class}, handler);
        }
    }
}