package com.godaddy.ans.sdk.pop;

import java.time.Instant;
import java.util.Objects;

public final class CallerOptions {

    private final String accessToken;
    private final String expectedPeer;
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