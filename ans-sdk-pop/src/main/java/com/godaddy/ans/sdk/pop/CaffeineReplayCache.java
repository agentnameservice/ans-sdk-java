package com.godaddy.ans.sdk.pop;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.Ticker;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * In-JVM replay cache backed by Caffeine.
 *
 * <p><b>Scope:</b> replay protection is per-process. Entries are not shared across
 * replicas, so a proof replayed to a different instance within its freshness window
 * is accepted. Multi-replica deployments need a distributed {@link ReplayCache}
 * (for example Redis) or sticky routing to keep a single-use guarantee.
 *
 * <p><b>Sizing:</b> {@code maxEntries} bounds memory. Eviction is by TTL only; the
 * cache never drops a still-fresh jti to make room. At capacity it fails closed —
 * {@link #checkAndStore} throws {@link ErrorType#REPLAY_CACHE_FULL} rather than admit
 * an id it cannot record. Set {@code maxEntries} above the peak count of vouched
 * requests within one freshness window, with headroom, or callers are rejected.
 */
public final class CaffeineReplayCache implements ReplayCache {

    private final Cache<String, Duration> cache;
    private final int maxEntries;

    private CaffeineReplayCache(Cache<String, Duration> cache, int maxEntries) {
        this.cache = cache;
        this.maxEntries = maxEntries;
    }

    public static CaffeineReplayCache create(int maxEntries) {
        return create(maxEntries, Ticker.systemTicker());
    }

    static CaffeineReplayCache create(int maxEntries, Ticker ticker) {
        Objects.requireNonNull(ticker, "ticker");
        Cache<String, Duration> cache = Caffeine.newBuilder()
            .expireAfter(Expiry.creating((String key, Duration ttl) -> ttl))
            .ticker(ticker)
            .build();
        return new CaffeineReplayCache(cache, maxEntries);
    }

    @Override
    public boolean checkAndStore(String key, Duration ttl) throws PopException {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(ttl, "ttl");
        ConcurrentMap<String, Duration> map = cache.asMap();
        if (map.containsKey(key)) {
            return true;
        }
        // ponytail: approximate gate — a concurrent burst may seat a few entries
        // over maxEntries. That is the safe direction (over-retention); memory
        // stays bounded by TTL. cleanUp() purges expired ids before we reject.
        if (map.size() >= maxEntries) {
            cache.cleanUp();
            if (map.size() >= maxEntries) {
                throw new PopException(ErrorType.REPLAY_CACHE_FULL,
                    "replay cache at capacity; cannot record proof id");
            }
        }
        return map.putIfAbsent(key, ttl) != null;
    }

    /**
     * Approximate number of ids currently held. Compare to {@link #cap()} to alarm
     * on saturation before the cache starts rejecting callers. Purges expired ids
     * first, so the count reflects live entries.
     */
    public long len() {
        cache.cleanUp();
        return cache.estimatedSize();
    }

    /** Configured entry ceiling ({@code maxEntries}). */
    public int cap() {
        return maxEntries;
    }
}