package com.godaddy.ans.sdk.pop;

import java.util.Objects;

/** Options for a single {@link DpopProofVerifier#verify} call. */
public final class VerifyOptions {

    private final String accessToken;
    private final ContentSource receivedContent;

    private VerifyOptions(String accessToken, ContentSource receivedContent) {
        this.accessToken = accessToken;
        this.receivedContent = receivedContent;
    }

    /**
     * No access token was presented, so the proof must carry no ath, and the
     * request carried no content, so its ans_content_digest must be the digest of
     * the empty octet string (ANS-6 §7.13).
     */
    public static VerifyOptions none() {
        return new VerifyOptions(null, null);
    }

    /**
     * Tells the verifier the request presented this OAuth2 access token
     * ({@code Authorization: DPoP <token>}, RFC 9449 §7.1), which requires the
     * proof's ath to hash-match it. Without this, a proof carrying ath is
     * rejected — the profile enforces ath vs presented token in both directions.
     */
    public static VerifyOptions withAccessToken(String accessToken) {
        return new VerifyOptions(Objects.requireNonNull(accessToken, "accessToken"), null);
    }

    /**
     * Supplies the request content the proof binds (ANS-6 §7.13). The verifier
     * reads it only after every other check has passed and compares its SHA-256
     * with the proof's ans_content_digest; a mismatch rejects the proof before
     * its jti is recorded.
     */
    public VerifyOptions withReceivedContent(ContentSource content) {
        return new VerifyOptions(accessToken, Objects.requireNonNull(content, "content"));
    }

    String accessToken() {
        return accessToken;
    }

    public ContentSource receivedContent() {
        return receivedContent;
    }
}
