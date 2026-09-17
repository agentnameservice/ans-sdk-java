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
        assertThat(options.receivedContent()).isNull();
    }

    @Test
    void withReceivedContentSetsSourceAndPreservesOthers() throws Exception {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        byte[] body = {1, 2, 3};
        ContentSource source = () -> body;
        CallerOptions options = CallerOptions.none()
            .withAccessToken("token")
            .withExpectedPeer("ans://peer.example.com")
            .withClock(now)
            .withReceivedContent(source);

        assertThat(options.receivedContent()).isSameAs(source);
        assertThat(options.receivedContent().read()).isEqualTo(body);
        assertThat(options.accessToken()).isEqualTo("token");
        assertThat(options.expectedPeer()).isEqualTo("ans://peer.example.com");
        assertThat(options.clock()).isEqualTo(now);
    }

    @Test
    void withReceivedContentRejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> CallerOptions.none().withReceivedContent(null));
    }

    @Test
    void withAccessTokenSetsTokenAndPreservesOthers() {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        ContentSource source = () -> new byte[0];
        CallerOptions options = CallerOptions.none()
            .withExpectedPeer("ans://peer.example.com")
            .withClock(now)
            .withReceivedContent(source)
            .withAccessToken("access-token");

        assertThat(options.accessToken()).isEqualTo("access-token");
        assertThat(options.expectedPeer()).isEqualTo("ans://peer.example.com");
        assertThat(options.clock()).isEqualTo(now);
        assertThat(options.receivedContent()).isSameAs(source);
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
    void withExpectedPeerPreservesContent() {
        ContentSource source = () -> new byte[0];
        CallerOptions options = CallerOptions.none()
            .withReceivedContent(source)
            .withExpectedPeer("ans://peer.example.com");

        assertThat(options.expectedPeer()).isEqualTo("ans://peer.example.com");
        assertThat(options.receivedContent()).isSameAs(source);
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
