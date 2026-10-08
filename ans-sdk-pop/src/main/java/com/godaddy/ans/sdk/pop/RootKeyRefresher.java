package com.godaddy.ans.sdk.pop;

import java.security.PublicKey;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * Refreshes the Transparency Log root keys when a receipt or status token is signed by a
 * key the verifier does not hold (ANS-6 §4.5, §9.5). {@code artifactIssuedAt} is the issue
 * time of the artifact that named the unknown key, so an implementation can refuse to
 * refresh for artifacts older than its cache. Implementations must rate-limit refreshes
 * and bound their wait: the verifier calls this on the request path. Return the refreshed
 * key set when a refresh happened and the verifier retries verification once with it;
 * return empty to keep the rejection.
 */
@FunctionalInterface
public interface RootKeyRefresher {

    /**
     * Attempts a rate-limited root-key refresh for an artifact issued at {@code artifactIssuedAt}.
     *
     * @return the refreshed keys keyed by key id when a refresh happened, empty otherwise
     */
    Optional<Map<String, PublicKey>> refresh(Instant artifactIssuedAt);
}
