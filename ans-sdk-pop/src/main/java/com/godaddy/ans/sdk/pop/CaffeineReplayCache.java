package com.godaddy.ans.sdk.pop;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Ticker;

import java.time.Duration;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-JVM replay cache backed by Caffeine.
 *
 * <p><b>Scope:</b> replay protection is per-process. Entries are not shared across
 * replicas, so a proof replayed to a different instance within its freshness window
 * is accepted. Multi-replica deployments need a distributed {@link ReplayCache}
 * (for example Redis) or sticky routing to keep a single-use guarantee.
 *
 * <p><b>Sizing:</b> {@code maxEntries} bounds memory. Size-based eviction can drop a
 * still-fresh jti under load and reopen a replay window for it. Set {@code maxEntries}
 * above the peak count of vouched requests within one freshness window, with headroom.
 */
public final class CaffeineReplayCache implements ReplayCache {

    private static final Logger LOG = LoggerFactory.getLogger(CaffeineReplayCache.class);

    private final Cache<String, Duration> cache;

    private CaffeineReplayCache(Cache<String, Duration> cache) {
        this.cache = cache;
    }

    public static CaffeineReplayCache create(int maxEntries) {
        return create(maxEntries, Ticker.systemTicker());
    }

    static CaffeineReplayCache create(int maxEntries, Ticker ticker) {
        Objects.requireNonNull(ticker, "ticker");
        Cache<String, Duration> cache = Caffeine.newBuilder()
            .maximumSize(maxEntries)
            .expireAfter(Expiry.creating((String key, Duration ttl) -> ttl))
            .ticker(ticker)
            .evictionListener((String key, Duration ttl, RemovalCause cause) -> {
                if (cause == RemovalCause.SIZE) {
                    LOG.warn("replay cache evicted a live entry due to size; "
                            + "increase maxEntries to preserve replay protection");
                }
            })
            .build();
        return new CaffeineReplayCache(cache);
    }

    @Override
    public boolean checkAndStore(String key, Duration ttl) {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(ttl, "ttl");
        return cache.asMap().putIfAbsent(key, ttl) != null;
    }
}