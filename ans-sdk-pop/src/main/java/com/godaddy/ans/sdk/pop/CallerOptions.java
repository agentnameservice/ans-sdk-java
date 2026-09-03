package com.godaddy.ans.sdk.pop;

import java.time.Instant;
import java.util.Objects;

/** Options for a single {@link CallerVerifier#verifyCaller} call. */
public final class CallerOptions {

    // The OAuth2 access token presented on the request, or null.
    private final String accessToken;
    // The ans:// name the callee will accept, or null to accept any proven agent.
    private final String expectedPeer;
    // A fixed verification time, or null to use the current time.
    private final Instant clock;
    // The SHA-256 of the request body (32 bytes), or null when no body is bound.
    private final byte[] contentSha256;
    // Whether the proof MUST carry an ans_content_digest.
    private final boolean requireContentBinding;

    private CallerOptions(String accessToken, String expectedPeer, Instant clock,
                          byte[] contentSha256, boolean requireContentBinding) {
        this.accessToken = accessToken;
        this.expectedPeer = expectedPeer;
        this.clock = clock;
        this.contentSha256 = contentSha256;
        this.requireContentBinding = requireContentBinding;
    }

    public static CallerOptions none() {
        return new CallerOptions(null, null, null, null, false);
    }

    public CallerOptions withAccessToken(String token) {
        return new CallerOptions(Objects.requireNonNull(token, "token"), expectedPeer, clock,
            contentSha256, requireContentBinding);
    }

    /**
     * Restricts accepted callers to this ans:// name. When no expected peer is
     * set, any proven agent authenticates, and the callee authorizes downstream.
     */
    public CallerOptions withExpectedPeer(String peer) {
        return new CallerOptions(accessToken, Objects.requireNonNull(peer, "peer"), clock,
            contentSha256, requireContentBinding);
    }

    public CallerOptions withClock(Instant now) {
        return new CallerOptions(accessToken, expectedPeer, Objects.requireNonNull(now, "now"),
            contentSha256, requireContentBinding);
    }

    /**
     * Binds the request body: the proof's ans_content_digest must match the
     * SHA-256 of the body (ANS-6 §7.13). The caller hashes the body; the digest
     * must be exactly 32 bytes. The array is copied defensively.
     */
    public CallerOptions withContentSha256(byte[] contentSha256) {
        Objects.requireNonNull(contentSha256, "contentSha256");
        return new CallerOptions(accessToken, expectedPeer, clock, contentSha256.clone(), requireContentBinding);
    }

    /** Requires the proof to carry an ans_content_digest (ANS-6 §7.13). */
    public CallerOptions withRequiredContentBinding() {
        return new CallerOptions(accessToken, expectedPeer, clock, contentSha256, true);
    }

    String accessToken() {
        return accessToken;
    }

    String expectedPeer() {
        return expectedPeer;
    }

    Instant clock() {
        return clock;
    }

    byte[] contentSha256() {
        return contentSha256;
    }

    boolean requireContentBinding() {
        return requireContentBinding;
    }
}