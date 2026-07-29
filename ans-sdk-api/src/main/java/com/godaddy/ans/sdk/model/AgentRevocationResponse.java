package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * AgentRevocationResponse
 */
@JsonPropertyOrder({
    AgentRevocationResponse.JSON_PROPERTY_AGENT_ID,
    AgentRevocationResponse.JSON_PROPERTY_ANS_NAME,
    AgentRevocationResponse.JSON_PROPERTY_STATUS,
    AgentRevocationResponse.JSON_PROPERTY_REVOKED_AT,
    AgentRevocationResponse.JSON_PROPERTY_REASON,
    AgentRevocationResponse.JSON_PROPERTY_DNS_RECORDS_TO_REMOVE,
    AgentRevocationResponse.JSON_PROPERTY_LINKS
})
public class AgentRevocationResponse {
    public static final String JSON_PROPERTY_AGENT_ID = "agentId";

    @Nonnull
    private UUID agentId;

    public static final String JSON_PROPERTY_ANS_NAME = "ansName";

    @Nonnull
    private String ansName;

    public static final String JSON_PROPERTY_STATUS = "status";

    @Nonnull
    private AgentLifecycleStatus status;

    public static final String JSON_PROPERTY_REVOKED_AT = "revokedAt";

    @Nonnull
    private OffsetDateTime revokedAt;

    public static final String JSON_PROPERTY_REASON = "reason";

    @Nonnull
    private RevocationReason reason;

    public static final String JSON_PROPERTY_DNS_RECORDS_TO_REMOVE = "dnsRecordsToRemove";

    @Nullable
    private List<DnsRecord> dnsRecordsToRemove = new ArrayList<>();

    public static final String JSON_PROPERTY_LINKS = "links";

    @Nonnull
    private List<Link> links = new ArrayList<>();

    public AgentRevocationResponse() {
    }

    public AgentRevocationResponse agentId(@Nonnull UUID agentId) {
        this.agentId = agentId;
        return this;
    }

    /**
     * Get agentId
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

    public AgentRevocationResponse ansName(@Nonnull String ansName) {
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

    public AgentRevocationResponse status(@Nonnull AgentLifecycleStatus status) {
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
    public AgentLifecycleStatus getStatus() {
        return status;
    }

    @JsonProperty(value = JSON_PROPERTY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setStatus(@Nonnull AgentLifecycleStatus status) {
        this.status = status;
    }

    public AgentRevocationResponse revokedAt(@Nonnull OffsetDateTime revokedAt) {
        this.revokedAt = revokedAt;
        return this;
    }

    /**
     * Get revokedAt
     * @return revokedAt
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_REVOKED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getRevokedAt() {
        return revokedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_REVOKED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setRevokedAt(@Nonnull OffsetDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public AgentRevocationResponse reason(@Nonnull RevocationReason reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Get reason
     * @return reason
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_REASON, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public RevocationReason getReason() {
        return reason;
    }

    @JsonProperty(value = JSON_PROPERTY_REASON, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setReason(@Nonnull RevocationReason reason) {
        this.reason = reason;
    }

    public AgentRevocationResponse dnsRecordsToRemove(@Nullable List<DnsRecord> dnsRecordsToRemove) {
        this.dnsRecordsToRemove = dnsRecordsToRemove;
        return this;
    }

    public AgentRevocationResponse addDnsRecordsToRemoveItem(DnsRecord dnsRecordsToRemoveItem) {
        if (this.dnsRecordsToRemove == null) {
            this.dnsRecordsToRemove = new ArrayList<>();
        }
        this.dnsRecordsToRemove.add(dnsRecordsToRemoveItem);
        return this;
    }

    /**
     * Get dnsRecordsToRemove
     * @return dnsRecordsToRemove
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DNS_RECORDS_TO_REMOVE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<DnsRecord> getDnsRecordsToRemove() {
        return dnsRecordsToRemove;
    }

    @JsonProperty(value = JSON_PROPERTY_DNS_RECORDS_TO_REMOVE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDnsRecordsToRemove(@Nullable List<DnsRecord> dnsRecordsToRemove) {
        this.dnsRecordsToRemove = dnsRecordsToRemove;
    }

    public AgentRevocationResponse links(@Nonnull List<Link> links) {
        this.links = links;
        return this;
    }

    public AgentRevocationResponse addLinksItem(Link linksItem) {
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
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_LINKS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setLinks(@Nonnull List<Link> links) {
        this.links = links;
    }

    /**
     * Return true if this AgentRevocationResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentRevocationResponse agentRevocationResponse = (AgentRevocationResponse) o;
        return Objects.equals(this.agentId, agentRevocationResponse.agentId) &&
                Objects.equals(this.ansName, agentRevocationResponse.ansName) &&
                Objects.equals(this.status, agentRevocationResponse.status) &&
                Objects.equals(this.revokedAt, agentRevocationResponse.revokedAt) &&
                Objects.equals(this.reason, agentRevocationResponse.reason) &&
                Objects.equals(this.dnsRecordsToRemove, agentRevocationResponse.dnsRecordsToRemove) &&
                Objects.equals(this.links, agentRevocationResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, ansName, status, revokedAt, reason, dnsRecordsToRemove, links);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentRevocationResponse {\n");
        sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
        sb.append("    ansName: ").append(toIndentedString(ansName)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    revokedAt: ").append(toIndentedString(revokedAt)).append("\n");
        sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
        sb.append("    dnsRecordsToRemove: ").append(toIndentedString(dnsRecordsToRemove)).append("\n");
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
