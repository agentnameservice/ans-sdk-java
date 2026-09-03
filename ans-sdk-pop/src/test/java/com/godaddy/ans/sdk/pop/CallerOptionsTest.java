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
        assertThat(options.contentSha256()).isNull();
        assertThat(options.requireContentBinding()).isFalse();
    }

    @Test
    void withContentSha256CopiesArrayAndPreservesOthers() {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        byte[] hash = new byte[32];
        hash[0] = 1;
        CallerOptions options = CallerOptions.none()
            .withAccessToken("token")
            .withExpectedPeer("ans://peer.example.com")
            .withClock(now)
            .withContentSha256(hash);

        hash[0] = 2;

        assertThat(options.contentSha256()[0]).isEqualTo((byte) 1);
        assertThat(options.accessToken()).isEqualTo("token");
        assertThat(options.expectedPeer()).isEqualTo("ans://peer.example.com");
        assertThat(options.clock()).isEqualTo(now);
    }

    @Test
    void withRequiredContentBindingSetsFlagAndPreservesContent() {
        CallerOptions options = CallerOptions.none()
            .withContentSha256(new byte[32])
            .withRequiredContentBinding();

        assertThat(options.requireContentBinding()).isTrue();
        assertThat(options.contentSha256()).hasSize(32);
    }

    @Test
    void withContentSha256RejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CallerOptions.none().withContentSha256(null));
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