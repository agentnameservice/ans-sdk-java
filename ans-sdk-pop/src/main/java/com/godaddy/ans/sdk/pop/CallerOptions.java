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
    // The request content, or null when the request carried none.
    private final ContentSource receivedContent;

    private CallerOptions(String accessToken, String expectedPeer, Instant clock, ContentSource receivedContent) {
        this.accessToken = accessToken;
        this.expectedPeer = expectedPeer;
        this.clock = clock;
        this.receivedContent = receivedContent;
    }

    /**
     * No access token, any proven agent accepted, the current time, and no
     * request content: the proof's ans_content_digest must be the digest of the
     * empty octet string (ANS-6 §7.13).
     */
    public static CallerOptions none() {
        return new CallerOptions(null, null, null, null);
    }

    public CallerOptions withAccessToken(String token) {
        return new CallerOptions(Objects.requireNonNull(token, "token"), expectedPeer, clock, receivedContent);
    }

    /**
     * Restricts accepted callers to this ans:// name. When no expected peer is
     * set, any proven agent authenticates, and the callee authorizes downstream.
     */
    public CallerOptions withExpectedPeer(String peer) {
        return new CallerOptions(accessToken, Objects.requireNonNull(peer, "peer"), clock, receivedContent);
    }

    public CallerOptions withClock(Instant now) {
        return new CallerOptions(accessToken, expectedPeer, Objects.requireNonNull(now, "now"), receivedContent);
    }

    /**
     * Supplies the request content the proof binds (ANS-6 §7.13). It is read once,
     * after the proof is bound to a live ANS identity and before the jti is
     * recorded, and its SHA-256 must equal the proof's ans_content_digest.
     */
    public CallerOptions withReceivedContent(ContentSource content) {
        return new CallerOptions(accessToken, expectedPeer, clock, Objects.requireNonNull(content, "content"));
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

    public ContentSource receivedContent() {
        return receivedContent;
    }
}
