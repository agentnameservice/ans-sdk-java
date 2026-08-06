package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * V2 schema for ANS Transparency Log entries.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransparencyLogV2 {

    @JsonProperty("logId")
    private String logId;

    @JsonProperty("producer")
    private ProducerV2 producer;

    public TransparencyLogV2() {
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public ProducerV2 getProducer() {
        return producer;
    }

    public void setProducer(ProducerV2 producer) {
        this.producer = producer;
    }

    /**
     * Convenience method to get the event from the producer.
     *
     * @return the event, or null if producer is null
     */
    public EventV2 getEvent() {
        return producer != null ? producer.getEvent() : null;
    }

    /**
     * Convenience method to get attestations from the event.
     *
     * @return the attestations, or null if not available
     */
    public AttestationsV2 getAttestations() {
        EventV2 event = getEvent();
        return event != null ? event.getAttestations() : null;
    }

    /**
     * Convenience method to get the ANS name.
     *
     * @return the ANS name, or null if not available
     */
    public String getAnsName() {
        EventV2 event = getEvent();
        return event != null ? event.getAnsName() : null;
    }

    /**
     * Convenience method to get the event type.
     *
     * @return the event type, or null if not available
     */
    public EventTypeV1 getEventType() {
        EventV2 event = getEvent();
        return event != null ? event.getEventType() : null;
    }

    @Override
    public String toString() {
        return "TransparencyLogV2{"
            + "logId='" + logId + '\''
            + ", producer=" + producer
            + '}';
    }
}
