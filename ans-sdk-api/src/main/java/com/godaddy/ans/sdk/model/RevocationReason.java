package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets RevocationReason
 */
public enum RevocationReason {

    KEY_COMPROMISE("KEY_COMPROMISE"),

    CESSATION_OF_OPERATION("CESSATION_OF_OPERATION"),

    AFFILIATION_CHANGED("AFFILIATION_CHANGED"),

    SUPERSEDED("SUPERSEDED"),

    CERTIFICATE_HOLD("CERTIFICATE_HOLD"),

    PRIVILEGE_WITHDRAWN("PRIVILEGE_WITHDRAWN"),

    AA_COMPROMISE("AA_COMPROMISE");

    private String value;

    RevocationReason(String value) {
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
    public static RevocationReason fromValue(String value) {
        for (RevocationReason b : RevocationReason.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
