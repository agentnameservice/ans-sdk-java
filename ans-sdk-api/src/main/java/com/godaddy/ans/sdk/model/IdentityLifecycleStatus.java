package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * PENDING_CONTROL → VERIFIED → REVOKED. Rotation keeps the row VERIFIED (the staged replacement proves control before
 * anything changes).
 */
public enum IdentityLifecycleStatus {

    PENDING_CONTROL("PENDING_CONTROL"),

    VERIFIED("VERIFIED"),

    REVOKED("REVOKED");

    private String value;

    IdentityLifecycleStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static IdentityLifecycleStatus fromValue(String value) {
        for (IdentityLifecycleStatus b : IdentityLifecycleStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
