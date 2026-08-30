package com.godaddy.ans.sdk.pop;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.Ticker;

import java.time.Duration;
import java.util.Objects;

public final class CaffeineReplayCache implements ReplayCache {

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