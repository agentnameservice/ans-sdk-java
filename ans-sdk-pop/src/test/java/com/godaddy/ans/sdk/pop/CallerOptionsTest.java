package com.godaddy.ans.sdk.pop;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CallerOptionsTest {

    @Test
    void noneHasNullFields() {
        CallerOptions options = CallerOptions.none();

        assertThat(options.accessToken()).isNull();
        assertThat(options.expectedPeer()).isNull();
        assertThat(options.clock()).isNull();
    }

    @Test
    void withAccessTokenSetsTokenAndPreservesOthers() {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        CallerOptions options = CallerOptions.none()
            .withExpectedPeer("ans://peer.example.com")
            .withClock(now)
            .withAccessToken("access-token");

        assertThat(options.accessToken()).isEqualTo("access-token");
        assertThat(options.expectedPeer()).isEqualTo("ans://peer.example.com");
        assertThat(options.clock()).isEqualTo(now);
    }

    @Test
    void withClockSetsClockAndPreservesOthers() {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        CallerOptions options = CallerOptions.none()
            .withAccessToken("access-token")
            .withExpectedPeer("ans://peer.example.com")
            .withClock(now);

        assertThat(options.clock()).isEqualTo(now);
        assertThat(options.accessToken()).isEqualTo("access-token");
        assertThat(options.expectedPeer()).isEqualTo("ans://peer.example.com");
    }

    @Test
    void withAccessTokenRejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CallerOptions.none().withAccessToken(null));
    }

    @Test
    void withClockRejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CallerOptions.none().withClock(null));
    }

    @Test
    void withExpectedPeerRejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CallerOptions.none().withExpectedPeer(null));
    }
}