package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AgentLifecycleStatus
 */
public enum AgentLifecycleStatus {

    PENDING_VALIDATION("PENDING_VALIDATION"),

    PENDING_DNS("PENDING_DNS"),

    ACTIVE("ACTIVE"),

    FAILED("FAILED"),

    EXPIRED("EXPIRED"),

    DEPRECATED("DEPRECATED"),

    REVOKED("REVOKED");

    private String value;

    AgentLifecycleStatus(String value) {
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
    public static AgentLifecycleStatus fromValue(String value) {
        for (AgentLifecycleStatus b : AgentLifecycleStatus.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
