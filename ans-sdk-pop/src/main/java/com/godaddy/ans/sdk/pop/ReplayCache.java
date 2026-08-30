package com.godaddy.ans.sdk.pop;

import java.time.Duration;

public interface ReplayCache {

    boolean checkAndStore(String key, Duration ttl);
}