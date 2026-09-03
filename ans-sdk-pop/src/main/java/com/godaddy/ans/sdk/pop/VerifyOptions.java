package com.godaddy.ans.sdk.pop;

import java.util.Objects;

/** Options for a single {@link DpopProofVerifier#verify} call. */
public final class VerifyOptions {

    private final String accessToken;
    private final byte[] contentSha256;
    private final boolean requireContentBinding;

    private VerifyOptions(String accessToken, byte[] contentSha256, boolean requireContentBinding) {
        this.accessToken = accessToken;
        this.contentSha256 = contentSha256;
        this.requireContentBinding = requireContentBinding;
    }

    /** No access token was presented, so the proof must carry no ath. */
    public static VerifyOptions none() {
        return new VerifyOptions(null, null, false);
    }

    /**
     * Tells the verifier the request presented this OAuth2 access token
     * ({@code Authorization: DPoP <token>}, RFC 9449 §7.1), which requires the
     * proof's ath to hash-match it. Without this, a proof carrying ath is
     * rejected — the profile enforces ath vs presented token in both directions.
     */
    public static VerifyOptions withAccessToken(String accessToken) {
        return new VerifyOptions(Objects.requireNonNull(accessToken, "accessToken"), null, false);
    }

    /**
     * Tells the verifier the request body hashes to this SHA-256 digest, which
     * requires the proof's ans_content_digest to match it (ANS-6 §7.13). The
     * caller hashes the body; the verifier never sees raw bytes. The digest must
     * be exactly 32 bytes, or verification fails MISCONFIGURED. The array is
     * copied defensively.
     */
    public VerifyOptions withContentSha256(byte[] contentSha256) {
        Objects.requireNonNull(contentSha256, "contentSha256");
        return new VerifyOptions(accessToken, contentSha256.clone(), requireContentBinding);
    }

    /**
     * Requires the proof to carry an ans_content_digest. Without this, a request
     * that supplies a body hash still accepts a proof that omits the digest;
     * with it, the missing digest is rejected. Use at state-changing endpoints
     * where the body MUST be bound (ANS-6 §7.13).
     */
    public VerifyOptions withRequiredContentBinding() {
        return new VerifyOptions(accessToken, contentSha256, true);
    }

    String accessToken() {
        return accessToken;
    }

    byte[] contentSha256() {
        return contentSha256;
    }

    boolean requireContentBinding() {
        return requireContentBinding;
    }
}