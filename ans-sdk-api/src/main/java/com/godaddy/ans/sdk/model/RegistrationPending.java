package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * RegistrationPending
 */
@JsonPropertyOrder({
    RegistrationPending.JSON_PROPERTY_AGENT_ID,
    RegistrationPending.JSON_PROPERTY_STATUS,
    RegistrationPending.JSON_PROPERTY_ANS_NAME,
    RegistrationPending.JSON_PROPERTY_NEXT_STEPS,
    RegistrationPending.JSON_PROPERTY_CHALLENGES,
    RegistrationPending.JSON_PROPERTY_DNS_RECORDS,
    RegistrationPending.JSON_PROPERTY_EXPIRES_AT,
    RegistrationPending.JSON_PROPERTY_LINKS
})
public class RegistrationPending {
    public static final String JSON_PROPERTY_AGENT_ID = "agentId";


    @Nonnull
    private UUID agentId;

    /**
     * Gets or Sets status
     */
    public enum StatusEnum {
        PENDING_VALIDATION("PENDING_VALIDATION"),

        PENDING_CERTS("PENDING_CERTS"),

        PENDING_DNS("PENDING_DNS");

        private String value;

        StatusEnum(String value) {
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
        public static StatusEnum fromValue(String value) {
            for (StatusEnum b : StatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_STATUS = "status";


    @Nonnull
    private StatusEnum status;

    public static final String JSON_PROPERTY_ANS_NAME = "ansName";


    @Nonnull
    private String ansName;

    public static final String JSON_PROPERTY_NEXT_STEPS = "nextSteps";


    @Nonnull
    private List<NextStep> nextSteps = new ArrayList<>();

    public static final String JSON_PROPERTY_CHALLENGES = "challenges";

    @Nullable
    private List<ChallengeInfo> challenges = new ArrayList<>();

    public static final String JSON_PROPERTY_DNS_RECORDS = "dnsRecords";

    @Nullable
    private List<DnsRecord> dnsRecords = new ArrayList<>();

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";

    @Nullable
    private OffsetDateTime expiresAt;

    public static final String JSON_PROPERTY_LINKS = "links";

    @Nullable
    private List<Link> links = new ArrayList<>();

    public RegistrationPending() {
    }

    public RegistrationPending agentId(@Nonnull UUID agentId) {
        this.agentId = agentId;
        return this;
    }

    /**
     * Unique identifier assigned to this agent registration. Required for all subsequent API calls (verify-acme, verify-dns, certificates, revoke). New in v2 — previously callers had to parse this from HATEOAS link hrefs. Recommended for backport to v1.
     * @return agentId
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public UUID getAgentId() {
        return agentId;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentId(@Nonnull UUID agentId) {
        this.agentId = agentId;
    }

    public RegistrationPending status(@Nonnull StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * @return status
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public StatusEnum getStatus() {
        return status;
    }

    @JsonProperty(value = JSON_PROPERTY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setStatus(@Nonnull StatusEnum status) {
        this.status = status;
    }

    public RegistrationPending ansName(@Nonnull String ansName) {
        this.ansName = ansName;
        return this;
    }

    /**
     * Get ansName
     * @return ansName
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ANS_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAnsName() {
        return ansName;
    }

    @JsonProperty(value = JSON_PROPERTY_ANS_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAnsName(@Nonnull String ansName) {
        this.ansName = ansName;
    }

    public RegistrationPending nextSteps(@Nonnull List<NextStep> nextSteps) {
        this.nextSteps = nextSteps;
        return this;
    }

    public RegistrationPending addNextStepsItem(NextStep nextStepsItem) {
        if (this.nextSteps == null) {
            this.nextSteps = new ArrayList<>();
        }
        this.nextSteps.add(nextStepsItem);
        return this;
    }

    /**
     * Get nextSteps
     * @return nextSteps
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_NEXT_STEPS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<NextStep> getNextSteps() {
        return nextSteps;
    }

    @JsonProperty(value = JSON_PROPERTY_NEXT_STEPS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setNextSteps(@Nonnull List<NextStep> nextSteps) {
        this.nextSteps = nextSteps;
    }

    public RegistrationPending challenges(@Nullable List<ChallengeInfo> challenges) {
        this.challenges = challenges;
        return this;
    }

    public RegistrationPending addChallengesItem(ChallengeInfo challengesItem) {
        if (this.challenges == null) {
            this.challenges = new ArrayList<>();
        }
        this.challenges.add(challengesItem);
        return this;
    }

    /**
     * Get challenges
     * @return challenges
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CHALLENGES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<ChallengeInfo> getChallenges() {
        return challenges;
    }

    @JsonProperty(value = JSON_PROPERTY_CHALLENGES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setChallenges(@Nullable List<ChallengeInfo> challenges) {
        this.challenges = challenges;
    }

    public RegistrationPending dnsRecords(@Nullable List<DnsRecord> dnsRecords) {
        this.dnsRecords = dnsRecords;
        return this;
    }

    public RegistrationPending addDnsRecordsItem(DnsRecord dnsRecordsItem) {
        if (this.dnsRecords == null) {
            this.dnsRecords = new ArrayList<>();
        }
        this.dnsRecords.add(dnsRecordsItem);
        return this;
    }

    /**
     * Get dnsRecords
     * @return dnsRecords
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DNS_RECORDS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<DnsRecord> getDnsRecords() {
        return dnsRecords;
    }

    @JsonProperty(value = JSON_PROPERTY_DNS_RECORDS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDnsRecords(@Nullable List<DnsRecord> dnsRecords) {
        this.dnsRecords = dnsRecords;
    }

    public RegistrationPending expiresAt(@Nullable OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    /**
     * Get expiresAt
     * @return expiresAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_EXPIRES_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    @JsonProperty(value = JSON_PROPERTY_EXPIRES_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public RegistrationPending links(@Nullable List<Link> links) {
        this.links = links;
        return this;
    }

    public RegistrationPending addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

    /**
     * Get links
     * @return links
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LINKS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLinks(@Nullable List<Link> links) {
        this.links = links;
    }

    /**
     * Return true if this RegistrationPending object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistrationPending registrationPending = (RegistrationPending) o;
        return Objects.equals(this.agentId, registrationPending.agentId) &&
                Objects.equals(this.status, registrationPending.status) &&
                Objects.equals(this.ansName, registrationPending.ansName) &&
                Objects.equals(this.nextSteps, registrationPending.nextSteps) &&
                Objects.equals(this.challenges, registrationPending.challenges) &&
                Objects.equals(this.dnsRecords, registrationPending.dnsRecords) &&
                Objects.equals(this.expiresAt, registrationPending.expiresAt) &&
                Objects.equals(this.links, registrationPending.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, status, ansName, nextSteps, challenges, dnsRecords, expiresAt, links);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RegistrationPending {\n");
        sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    ansName: ").append(toIndentedString(ansName)).append("\n");
        sb.append("    nextSteps: ").append(toIndentedString(nextSteps)).append("\n");
        sb.append("    challenges: ").append(toIndentedString(challenges)).append("\n");
        sb.append("    dnsRecords: ").append(toIndentedString(dnsRecords)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

}
