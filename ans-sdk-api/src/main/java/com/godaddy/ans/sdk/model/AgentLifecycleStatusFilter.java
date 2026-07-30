package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets AgentLifecycleStatusFilter
 */
public enum AgentLifecycleStatusFilter {

    PENDING_DNS("PENDING_DNS"),

    ACTIVE("ACTIVE"),

    DEPRECATED("DEPRECATED"),

    REVOKED("REVOKED"),

    ALL("ALL");

    private String value;

    AgentLifecycleStatusFilter(String value) {
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
    public static AgentLifecycleStatusFilter fromValue(String value) {
        for (AgentLifecycleStatusFilter b : AgentLifecycleStatusFilter.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
