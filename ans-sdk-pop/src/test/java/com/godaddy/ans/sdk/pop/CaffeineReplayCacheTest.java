package com.godaddy.ans.sdk.pop;

import com.github.benmanes.caffeine.cache.Ticker;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CaffeineReplayCacheTest {

    private static final Duration TTL = Duration.ofSeconds(245);

    @Test
    void freshKeyReturnsFalse() throws Exception {
        CaffeineReplayCache cache = CaffeineReplayCache.create(128);

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();
    }

    @Test
    void secondCallSameKeyReturnsSeen() throws Exception {
        CaffeineReplayCache cache = CaffeineReplayCache.create(128);

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();
        assertThat(cache.checkAndStore("jti-1", TTL)).isTrue();
    }

    @Test
    void atCapacityFailsClosed() throws Exception {
        CaffeineReplayCache cache = CaffeineReplayCache.create(1);

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();

        assertThatThrownBy(() -> cache.checkAndStore("jti-2", TTL))
            .isInstanceOf(PopException.class)
            .extracting(e -> ((PopException) e).category())
            .isEqualTo(ErrorType.REPLAY_CACHE_FULL);
    }

    @Test
    void lenAndCapReportSaturation() throws Exception {
        CaffeineReplayCache cache = CaffeineReplayCache.create(128);

        assertThat(cache.cap()).isEqualTo(128);
        assertThat(cache.len()).isZero();

        cache.checkAndStore("jti-1", TTL);

        assertThat(cache.len()).isEqualTo(1);
    }

    @Test
    void afterTtlKeyReadmitted() throws Exception {
        AtomicLong nanos = new AtomicLong(0);
        Ticker ticker = nanos::get;
        CaffeineReplayCache cache = CaffeineReplayCache.create(128, ticker);

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();
        assertThat(cache.checkAndStore("jti-1", TTL)).isTrue();

        nanos.set(Duration.ofSeconds(246).toNanos());

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();
    }

    @Test
    void withinTtlStillSeen() throws Exception {
        AtomicLong nanos = new AtomicLong(0);
        Ticker ticker = nanos::get;
        CaffeineReplayCache cache = CaffeineReplayCache.create(128, ticker);

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();

        nanos.set(Duration.ofSeconds(244).toNanos());

        assertThat(cache.checkAndStore("jti-1", TTL)).isTrue();
    }

    @Test
    void perEntryTtlHonored() throws Exception {
        AtomicLong nanos = new AtomicLong(0);
        Ticker ticker = nanos::get;
        CaffeineReplayCache cache = CaffeineReplayCache.create(128, ticker);

        assertThat(cache.checkAndStore("jti-short", Duration.ofSeconds(100))).isFalse();
        assertThat(cache.checkAndStore("jti-long", Duration.ofSeconds(300))).isFalse();

        nanos.set(Duration.ofSeconds(150).toNanos());

        assertThat(cache.checkAndStore("jti-short", Duration.ofSeconds(100))).isFalse();
        assertThat(cache.checkAndStore("jti-long", Duration.ofSeconds(300))).isTrue();
    }

    @Test
    void repeatedCallDoesNotRefreshExpiry() throws Exception {
        AtomicLong nanos = new AtomicLong(0);
        Ticker ticker = nanos::get;
        CaffeineReplayCache cache = CaffeineReplayCache.create(128, ticker);

        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();

        nanos.set(Duration.ofSeconds(100).toNanos());
        assertThat(cache.checkAndStore("jti-1", TTL)).isTrue();

        nanos.set(Duration.ofSeconds(246).toNanos());
        assertThat(cache.checkAndStore("jti-1", TTL)).isFalse();
    }

    @Test
    void nullTickerRejected() {
        assertThatNullPointerException()
            .isThrownBy(() -> CaffeineReplayCache.create(128, null));
    }

    @Test
    void nullKeyRejected() {
        CaffeineReplayCache cache = CaffeineReplayCache.create(128);

        assertThatNullPointerException()
            .isThrownBy(() -> cache.checkAndStore(null, TTL));
    }

    @Test
    void nullTtlRejected() {
        CaffeineReplayCache cache = CaffeineReplayCache.create(128);

        assertThatNullPointerException()
            .isThrownBy(() -> cache.checkAndStore("jti-1", null));
    }
}