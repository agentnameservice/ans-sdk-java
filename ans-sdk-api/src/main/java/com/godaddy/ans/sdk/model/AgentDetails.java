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
 * AgentDetails
 */
@JsonPropertyOrder({
    AgentDetails.JSON_PROPERTY_AGENT_ID,
    AgentDetails.JSON_PROPERTY_AGENT_DISPLAY_NAME,
    AgentDetails.JSON_PROPERTY_AGENT_DESCRIPTION,
    AgentDetails.JSON_PROPERTY_VERSION,
    AgentDetails.JSON_PROPERTY_AGENT_HOST,
    AgentDetails.JSON_PROPERTY_ENDPOINTS,
    AgentDetails.JSON_PROPERTY_ANS_NAME,
    AgentDetails.JSON_PROPERTY_AGENT_STATUS,
    AgentDetails.JSON_PROPERTY_REGISTRATION_TIMESTAMP,
    AgentDetails.JSON_PROPERTY_LAST_RENEWAL_TIMESTAMP,
    AgentDetails.JSON_PROPERTY_REGISTRATION_PENDING,
    AgentDetails.JSON_PROPERTY_LINKS,
    AgentDetails.JSON_PROPERTY_IDENTITIES
})
public class AgentDetails {
    public static final String JSON_PROPERTY_AGENT_ID = "agentId";

    @Nonnull
    private UUID agentId;

    public static final String JSON_PROPERTY_AGENT_DISPLAY_NAME = "agentDisplayName";

    @Nonnull
    private String agentDisplayName;

    public static final String JSON_PROPERTY_AGENT_DESCRIPTION = "agentDescription";

    @Nullable
    private String agentDescription;

    public static final String JSON_PROPERTY_VERSION = "version";

    @Nonnull
    private String version;

    public static final String JSON_PROPERTY_AGENT_HOST = "agentHost";

    @Nonnull
    private String agentHost;

    public static final String JSON_PROPERTY_ENDPOINTS = "endpoints";

    @Nonnull
    private List<AgentEndpoint> endpoints = new ArrayList<>();

    public static final String JSON_PROPERTY_ANS_NAME = "ansName";

    @Nonnull
    private String ansName;

    public static final String JSON_PROPERTY_AGENT_STATUS = "agentStatus";

    @Nonnull
    private AgentLifecycleStatus agentStatus;

    public static final String JSON_PROPERTY_REGISTRATION_TIMESTAMP = "registrationTimestamp";

    @Nullable
    private OffsetDateTime registrationTimestamp;

    public static final String JSON_PROPERTY_LAST_RENEWAL_TIMESTAMP = "lastRenewalTimestamp";

    @Nullable
    private OffsetDateTime lastRenewalTimestamp;

    public static final String JSON_PROPERTY_REGISTRATION_PENDING = "registrationPending";

    @Nullable
    private RegistrationPending registrationPending;

    public static final String JSON_PROPERTY_LINKS = "links";

    @Nonnull
    private List<Link> links = new ArrayList<>();

    public static final String JSON_PROPERTY_IDENTITIES = "identities";

    @Nullable
    private List<LinkedIdentity> identities = new ArrayList<>();

    public AgentDetails() {
    }

    public AgentDetails agentId(@Nonnull UUID agentId) {
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

    public AgentDetails agentDisplayName(@Nonnull String agentDisplayName) {
        this.agentDisplayName = agentDisplayName;
        return this;
    }

    /**
     * Get agentDisplayName
     * @return agentDisplayName
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_DISPLAY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAgentDisplayName() {
        return agentDisplayName;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_DISPLAY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentDisplayName(@Nonnull String agentDisplayName) {
        this.agentDisplayName = agentDisplayName;
    }

    public AgentDetails agentDescription(@Nullable String agentDescription) {
        this.agentDescription = agentDescription;
        return this;
    }

    /**
     * Get agentDescription
     * @return agentDescription
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_AGENT_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getAgentDescription() {
        return agentDescription;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAgentDescription(@Nullable String agentDescription) {
        this.agentDescription = agentDescription;
    }

    public AgentDetails version(@Nonnull String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     * @return version
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_VERSION, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getVersion() {
        return version;
    }

    @JsonProperty(value = JSON_PROPERTY_VERSION, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setVersion(@Nonnull String version) {
        this.version = version;
    }

    public AgentDetails agentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
        return this;
    }

    /**
     * Get agentHost
     * @return agentHost
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_HOST, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAgentHost() {
        return agentHost;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_HOST, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
    }

    public AgentDetails endpoints(@Nonnull List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public AgentDetails addEndpointsItem(AgentEndpoint endpointsItem) {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<>();
        }
        this.endpoints.add(endpointsItem);
        return this;
    }

    /**
     * Get endpoints
     * @return endpoints
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ENDPOINTS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<AgentEndpoint> getEndpoints() {
        return endpoints;
    }

    @JsonProperty(value = JSON_PROPERTY_ENDPOINTS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setEndpoints(@Nonnull List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public AgentDetails ansName(@Nonnull String ansName) {
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

    public AgentDetails agentStatus(@Nonnull AgentLifecycleStatus agentStatus) {
        this.agentStatus = agentStatus;
        return this;
    }

    /**
     * Get agentStatus
     * @return agentStatus
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public AgentLifecycleStatus getAgentStatus() {
        return agentStatus;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentStatus(@Nonnull AgentLifecycleStatus agentStatus) {
        this.agentStatus = agentStatus;
    }

    public AgentDetails registrationTimestamp(@Nullable OffsetDateTime registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
        return this;
    }

    /**
     * Get registrationTimestamp
     * @return registrationTimestamp
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_REGISTRATION_TIMESTAMP, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    @JsonProperty(value = JSON_PROPERTY_REGISTRATION_TIMESTAMP, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRegistrationTimestamp(@Nullable OffsetDateTime registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    public AgentDetails lastRenewalTimestamp(@Nullable OffsetDateTime lastRenewalTimestamp) {
        this.lastRenewalTimestamp = lastRenewalTimestamp;
        return this;
    }

    /**
     * Get lastRenewalTimestamp
     * @return lastRenewalTimestamp
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LAST_RENEWAL_TIMESTAMP, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getLastRenewalTimestamp() {
        return lastRenewalTimestamp;
    }

    @JsonProperty(value = JSON_PROPERTY_LAST_RENEWAL_TIMESTAMP, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLastRenewalTimestamp(@Nullable OffsetDateTime lastRenewalTimestamp) {
        this.lastRenewalTimestamp = lastRenewalTimestamp;
    }

    public AgentDetails registrationPending(@Nullable RegistrationPending registrationPending) {
        this.registrationPending = registrationPending;
        return this;
    }

    /**
     * Get registrationPending
     * @return registrationPending
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_REGISTRATION_PENDING, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public RegistrationPending getRegistrationPending() {
        return registrationPending;
    }

    @JsonProperty(value = JSON_PROPERTY_REGISTRATION_PENDING, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRegistrationPending(@Nullable RegistrationPending registrationPending) {
        this.registrationPending = registrationPending;
    }

    public AgentDetails links(@Nonnull List<Link> links) {
        this.links = links;
        return this;
    }

    public AgentDetails addLinksItem(Link linksItem) {
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

    public AgentDetails identities(@Nullable List<LinkedIdentity> identities) {
        this.identities = identities;
        return this;
    }

    public AgentDetails addIdentitiesItem(LinkedIdentity identitiesItem) {
        if (this.identities == null) {
            this.identities = new ArrayList<>();
        }
        this.identities.add(identitiesItem);
        return this;
    }

    /**
     * Additive, optional, COMPUTED — the verified identities currently linked to this agent, joined from the link rows
     * at read time. Never stored on the registration; identity rotation/revocation is visible here immediately with
     * zero agent-side writes.
     * @return identities
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_IDENTITIES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<LinkedIdentity> getIdentities() {
        return identities;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTITIES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentities(@Nullable List<LinkedIdentity> identities) {
        this.identities = identities;
    }

    /**
     * Return true if this AgentDetails object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentDetails agentDetails = (AgentDetails) o;
        return Objects.equals(this.agentId, agentDetails.agentId) &&
                Objects.equals(this.agentDisplayName, agentDetails.agentDisplayName) &&
                Objects.equals(this.agentDescription, agentDetails.agentDescription) &&
                Objects.equals(this.version, agentDetails.version) &&
                Objects.equals(this.agentHost, agentDetails.agentHost) &&
                Objects.equals(this.endpoints, agentDetails.endpoints) &&
                Objects.equals(this.ansName, agentDetails.ansName) &&
                Objects.equals(this.agentStatus, agentDetails.agentStatus) &&
                Objects.equals(this.registrationTimestamp, agentDetails.registrationTimestamp) &&
                Objects.equals(this.lastRenewalTimestamp, agentDetails.lastRenewalTimestamp) &&
                Objects.equals(this.registrationPending, agentDetails.registrationPending) &&
                Objects.equals(this.links, agentDetails.links) &&
                Objects.equals(this.identities, agentDetails.identities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, agentDisplayName, agentDescription, version, agentHost, endpoints, ansName,
                agentStatus, registrationTimestamp, lastRenewalTimestamp, registrationPending, links, identities);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentDetails {\n");
        sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
        sb.append("    agentDisplayName: ").append(toIndentedString(agentDisplayName)).append("\n");
        sb.append("    agentDescription: ").append(toIndentedString(agentDescription)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    agentHost: ").append(toIndentedString(agentHost)).append("\n");
        sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
        sb.append("    ansName: ").append(toIndentedString(ansName)).append("\n");
        sb.append("    agentStatus: ").append(toIndentedString(agentStatus)).append("\n");
        sb.append("    registrationTimestamp: ").append(toIndentedString(registrationTimestamp)).append("\n");
        sb.append("    lastRenewalTimestamp: ").append(toIndentedString(lastRenewalTimestamp)).append("\n");
        sb.append("    registrationPending: ").append(toIndentedString(registrationPending)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
        sb.append("    identities: ").append(toIndentedString(identities)).append("\n");
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
