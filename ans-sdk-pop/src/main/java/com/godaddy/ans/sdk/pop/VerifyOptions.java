package com.godaddy.ans.sdk.pop;

import java.util.Objects;

/** Options for a single {@link DpopProofVerifier#verify} call. */
public final class VerifyOptions {

    private final String accessToken;

    private VerifyOptions(String accessToken) {
        this.accessToken = accessToken;
    }

    /** No access token was presented, so the proof must carry no ath. */
    public static VerifyOptions none() {
        return new VerifyOptions(null);
    }

    /**
     * Tells the verifier the request presented this OAuth2 access token
     * ({@code Authorization: DPoP <token>}, RFC 9449 §7.1), which requires the
     * proof's ath to hash-match it. Without this, a proof carrying ath is
     * rejected — the profile enforces ath vs presented token in both directions.
     */
    public static VerifyOptions withAccessToken(String accessToken) {
        return new VerifyOptions(Objects.requireNonNull(accessToken, "accessToken"));
    }

    String accessToken() {
        return accessToken;
    }
}