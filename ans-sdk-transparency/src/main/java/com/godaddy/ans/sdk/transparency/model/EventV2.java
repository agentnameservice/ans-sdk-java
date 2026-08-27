package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * Event structure in V2 schema.
 *
 * <p>The event core matches V1, so it reuses {@link AgentV1} and {@link EventTypeV1}. Only the
 * attestations shape differs, carried here as {@link AttestationsV2}.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventV2 {

    @JsonProperty("ansId")
    private String ansId;

    @JsonProperty("ansName")
    private String ansName;

    @JsonProperty("eventType")
    private EventTypeV1 eventType;

    @JsonProperty("agent")
    private AgentV1 agent;

    @JsonProperty("attestations")
    private AttestationsV2 attestations;

    @JsonProperty("expiresAt")
    private OffsetDateTime expiresAt;

    @JsonProperty("issuedAt")
    private OffsetDateTime issuedAt;

    @JsonProperty("raId")
    private String raId;

    @JsonProperty("renewalStatus")
    private String renewalStatus;

    @JsonProperty("revocationReasonCode")
    private RevocationReason revocationReasonCode;

    @JsonProperty("revokedAt")
    private OffsetDateTime revokedAt;

    @JsonProperty("timestamp")
    private OffsetDateTime timestamp;

    public EventV2() {
    }

    public String getAnsId() {
        return ansId;
    }

    public void setAnsId(String ansId) {
        this.ansId = ansId;
    }

    public String getAnsName() {
        return ansName;
    }

    public void setAnsName(String ansName) {
        this.ansName = ansName;
    }

    public EventTypeV1 getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeV1 eventType) {
        this.eventType = eventType;
    }

    public AgentV1 getAgent() {
        return agent;
    }

    public void setAgent(AgentV1 agent) {
        this.agent = agent;
    }

    public AttestationsV2 getAttestations() {
        return attestations;
    }

    public void setAttestations(AttestationsV2 attestations) {
        this.attestations = attestations;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(OffsetDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getRaId() {
        return raId;
    }

    public void setRaId(String raId) {
        this.raId = raId;
    }

    public String getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public RevocationReason getRevocationReasonCode() {
        return revocationReasonCode;
    }

    public void setRevocationReasonCode(RevocationReason revocationReasonCode) {
        this.revocationReasonCode = revocationReasonCode;
    }

    public OffsetDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(OffsetDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "EventV2{"
            + "ansId='" + ansId + '\''
            + ", ansName='" + ansName + '\''
            + ", eventType=" + eventType
            + ", agent=" + agent
            + ", issuedAt=" + issuedAt
            + ", expiresAt=" + expiresAt
            + '}';
    }
}
