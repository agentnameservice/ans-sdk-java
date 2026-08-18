package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Producer section of V2 schema.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProducerV2 {

    @JsonProperty("event")
    private EventV2 event;

    @JsonProperty("keyId")
    private String keyId;

    @JsonProperty("signature")
    private String signature;

    public ProducerV2() {
    }

    public EventV2 getEvent() {
        return event;
    }

    public void setEvent(EventV2 event) {
        this.event = event;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "ProducerV2{"
            + "event=" + event
            + ", keyId='" + keyId + '\''
            + '}';
    }
}
