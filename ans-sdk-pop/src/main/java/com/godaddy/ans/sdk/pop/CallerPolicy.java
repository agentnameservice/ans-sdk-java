package com.godaddy.ans.sdk.pop;

import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Transport-agnostic admission policy for an authenticated caller.
 *
 * <p>It holds the two host-based decisions a callee makes around
 * {@link CallerVerifier}, plus the single-value-header rule, without any
 * dependency on a specific HTTP stack. An adapter (for example a Servlet
 * filter) extracts the request authority, header map, and {@link CallerIdentity}
 * and asks this policy to decide; a second adapter reuses the same policy rather
 * than reimplementing it.
 *
 * <p>Both host sets may be empty. An empty set means "no restriction": every
 * request authority is trusted, or every proven caller is accepted.
 */
public final class CallerPolicy {

    /**
     * The security headers that must appear at most once on a request (RFC 9449
     * §4.3). A duplicate lets an attacker smuggle a second value past one parser.
     */
    public static final List<String> SINGLE_VALUE_HEADERS = List.of(
        PopHttp.DPOP_HEADER, "Authorization",
        ScittHeaders.SCITT_RECEIPT_HEADER, ScittHeaders.STATUS_TOKEN_HEADER);

    private final Set<String> trustedHosts;
    private final Set<String> allowedHosts;

    private CallerPolicy(Set<String> trustedHosts, Set<String> allowedHosts) {
        this.trustedHosts = trustedHosts;
        this.allowedHosts = allowedHosts;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Returns {@code true} when no trusted-authority restriction is configured,
     * so the caller can skip deriving the request authority.
     */
    public boolean trustsAnyAuthority() {
        return trustedHosts.isEmpty();
    }

    /**
     * Returns {@code true} when the request authority may present pop
     * credentials: either no restriction is set, or the normalized authority is
     * in the trusted set. A null authority fails a configured check.
     */
    public boolean authorityTrusted(String authority) {
        if (trustedHosts.isEmpty()) {
            return true;
        }
        return authority != null && trustedHosts.contains(normalizeAuthority(authority));
    }

    /**
     * Returns {@code true} when the proven caller is accepted: either no
     * restriction is set, or the caller's ans host is in the allowed set. An
     * unparseable ans name fails a configured check.
     */
    public boolean callerAllowed(CallerIdentity identity) {
        if (allowedHosts.isEmpty()) {
            return true;
        }
        try {
            return allowedHosts.contains(CallerVerifier.ansHost(identity.ansName()));
        } catch (PopException e) {
            return false;
        }
    }

    /**
     * Returns the name of the first {@link #SINGLE_VALUE_HEADERS} entry that the
     * request carries more than once, or empty when none is duplicated. Header
     * names are matched case-insensitively.
     */
    public Optional<String> duplicateSecurityHeader(Map<String, List<String>> headers) {
        for (String name : SINGLE_VALUE_HEADERS) {
            if (countHeader(headers, name) > 1) {
                return Optional.of(name);
            }
        }
        return Optional.empty();
    }

    private static int countHeader(Map<String, List<String>> headers, String name) {
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(name)) {
                return entry.getValue() == null ? 0 : entry.getValue().size();
            }
        }
        return 0;
    }

    /**
     * Lowercases an authority and drops the default HTTPS/HTTP port, so trust
     * comparison ignores case and an explicit {@code :443}/{@code :80}.
     */
    public static String normalizeAuthority(String host) {
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

        private final Set<String> trustedHosts = new HashSet<>();
        private final List<String> allowedNames = new ArrayList<>();

        private Builder() {
        }

        public Builder trustedHosts(String... hosts) {
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
                throw new IllegalArgumentException("trustedHosts: every supplied host was empty");
            }
            return this;
        }

        public Builder allowedAnsNames(String... ansNames) {
            for (String ansName : ansNames) {
                allowedNames.add(Objects.requireNonNull(ansName, "ansName"));
            }
            return this;
        }

        public CallerPolicy build() {
            Set<String> resolvedAllowed = new HashSet<>();
            for (String ansName : allowedNames) {
                try {
                    resolvedAllowed.add(CallerVerifier.ansHost(ansName));
                } catch (PopException e) {
                    throw new IllegalArgumentException("invalid allowed ans name: " + ansName, e);
                }
            }
            return new CallerPolicy(Set.copyOf(trustedHosts), resolvedAllowed);
        }
    }
}
