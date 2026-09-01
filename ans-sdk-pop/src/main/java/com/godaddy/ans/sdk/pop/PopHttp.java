package com.godaddy.ans.sdk.pop;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** HTTP helpers for attaching and reading pop credentials on requests. */
public final class PopHttp {

    /** The HTTP header that carries the compact DPoP proof (RFC 9449). */
    public static final String DPOP_HEADER = "DPoP";

    private static final String DPOP_SCHEME = "DPoP";

    private PopHttp() {
    }

    /**
     * Signs a DPoP proof for the request and attaches it as the {@code DPoP}
     * header, then copies the SCITT headers. When {@code accessToken} is
     * non-null, the proof also binds it via ath (RFC 9449 §4.2 / §7.1).
     */
    public static void attachIdentity(HttpRequest.Builder req, PopSigner signer,
            Map<String, List<String>> scittHeaders, String accessToken) throws PopException {
        Objects.requireNonNull(req, "req");
        Objects.requireNonNull(signer, "signer");
        Objects.requireNonNull(scittHeaders, "scittHeaders");

        HttpRequest snapshot = req.build();
        String method = snapshot.method();
        String url = snapshot.uri().toString();

        String proof = accessToken != null
            ? signer.sign(method, url, accessToken)
            : signer.sign(method, url);

        req.setHeader(DPOP_HEADER, proof);
        for (Map.Entry<String, List<String>> entry : scittHeaders.entrySet()) {
            for (String value : entry.getValue()) {
                req.header(entry.getKey(), value);
            }
        }
    }

    /**
     * Returns the access token when an Authorization header value presents one
     * under the DPoP auth scheme (RFC 9449 §7.1). Scheme comparison is
     * case-insensitive (RFC 9110 §11.1). A Bearer or absent Authorization yields
     * empty: such a token is not sender-constrained, so the proof must carry no
     * ath.
     *
     * <p>A callee that completes token binding must use this rather than parsing
     * the header itself. The verifier checks the proof's ath against exactly the
     * bytes this returns. A second, subtly different parser in the handler would
     * let the two halves of RFC 9449 §4.3 operate on different values.
     */
    public static Optional<String> accessTokenFromAuthorization(String value) {
        if (value == null || value.length() <= DPOP_SCHEME.length()) {
            return Optional.empty();
        }
        if (!value.regionMatches(true, 0, DPOP_SCHEME, 0, DPOP_SCHEME.length())) {
            return Optional.empty();
        }
        char separator = value.charAt(DPOP_SCHEME.length());
        if (separator != ' ' && separator != '\t') {
            return Optional.empty();
        }
        String token = trimSpaceTab(value.substring(DPOP_SCHEME.length()));
        if (token.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(token);
    }

    private static String trimSpaceTab(String value) {
        int start = 0;
        int end = value.length();
        while (start < end && (value.charAt(start) == ' ' || value.charAt(start) == '\t')) {
            start++;
        }
        while (end > start && (value.charAt(end - 1) == ' ' || value.charAt(end - 1) == '\t')) {
            end--;
        }
        return value.substring(start, end);
    }
}