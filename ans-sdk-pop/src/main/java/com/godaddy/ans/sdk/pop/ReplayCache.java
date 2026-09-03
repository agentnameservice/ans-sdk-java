package com.godaddy.ans.sdk.pop;

import java.time.Duration;

public interface ReplayCache {

    /**
     * Records {@code key} for single use. Returns {@code true} if it was already
     * present within its TTL (a replay), {@code false} if it was newly stored.
     *
     * <p>Throws {@link PopException} with {@link ErrorType#REPLAY_CACHE_FULL} when
     * the cache is at capacity and cannot record the id. This fails closed: an id
     * that cannot be recorded must not be admitted, or the replay window reopens.
     */
    boolean checkAndStore(String key, Duration ttl) throws PopException;
}