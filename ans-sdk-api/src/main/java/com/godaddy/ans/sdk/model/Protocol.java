package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Protocol
 */
public enum Protocol {

    A2_A("A2A"),

    MCP("MCP"),

    HTTP_API("HTTP_API");

    private String value;

    Protocol(String value) {
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
    public static Protocol fromValue(String value) {
        for (Protocol b : Protocol.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
