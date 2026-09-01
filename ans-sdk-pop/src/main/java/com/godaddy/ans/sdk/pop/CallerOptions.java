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

    private CallerOptions(String accessToken, String expectedPeer, Instant clock) {
        this.accessToken = accessToken;
        this.expectedPeer = expectedPeer;
        this.clock = clock;
    }

    public static CallerOptions none() {
        return new CallerOptions(null, null, null);
    }

    public CallerOptions withAccessToken(String token) {
        return new CallerOptions(Objects.requireNonNull(token, "token"), expectedPeer, clock);
    }

    /**
     * Restricts accepted callers to this ans:// name. When no expected peer is
     * set, any proven agent authenticates, and the callee authorizes downstream.
     */
    public CallerOptions withExpectedPeer(String peer) {
        return new CallerOptions(accessToken, Objects.requireNonNull(peer, "peer"), clock);
    }

    public CallerOptions withClock(Instant now) {
        return new CallerOptions(accessToken, expectedPeer, Objects.requireNonNull(now, "now"));
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
}