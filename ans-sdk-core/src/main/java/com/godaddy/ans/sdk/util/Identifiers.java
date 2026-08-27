package com.godaddy.ans.sdk.util;

import java.util.UUID;

/**
 * Validation helpers for ANS resource identifiers.
 *
 * <p>Agent and identity IDs are server-assigned UUIDs. Validating them before
 * they are concatenated into a request path fails fast on a malformed value and
 * keeps non-UUID input (a DID, a path segment, a query fragment) from silently
 * routing to the wrong resource or throwing deep inside URI parsing.</p>
 */
public final class Identifiers {

    private Identifiers() {
    }

    /**
     * Returns {@code value} when it is a UUID, otherwise throws.
     *
     * @param value the identifier to validate
     * @param name the parameter name to use in the error message
     * @return the validated value, unchanged
     * @throws IllegalArgumentException if {@code value} is null or not a UUID
     */
    public static String requireUuid(String value, String name) {
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(name + " must be a UUID, got: " + value, e);
        }
        return value;
    }
}