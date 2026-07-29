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
 * AgentListResponseItemsInner
 */
@JsonPropertyOrder({
    AgentListResponseItemsInner.JSON_PROPERTY_ANS_NAME,
    AgentListResponseItemsInner.JSON_PROPERTY_AGENT_ID,
    AgentListResponseItemsInner.JSON_PROPERTY_AGENT_DISPLAY_NAME,
    AgentListResponseItemsInner.JSON_PROPERTY_AGENT_DESCRIPTION,
    AgentListResponseItemsInner.JSON_PROPERTY_VERSION,
    AgentListResponseItemsInner.JSON_PROPERTY_AGENT_HOST,
    AgentListResponseItemsInner.JSON_PROPERTY_STATUS,
    AgentListResponseItemsInner.JSON_PROPERTY_TTL,
    AgentListResponseItemsInner.JSON_PROPERTY_REGISTRATION_TIMESTAMP,
    AgentListResponseItemsInner.JSON_PROPERTY_ENDPOINTS,
    AgentListResponseItemsInner.JSON_PROPERTY_LINKS
})
public class AgentListResponseItemsInner {
    public static final String JSON_PROPERTY_ANS_NAME = "ansName";

    @Nonnull
    private String ansName;

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

    public static final String JSON_PROPERTY_STATUS = "status";

    @Nonnull
    private AgentLifecycleStatus status;

    public static final String JSON_PROPERTY_TTL = "ttl";

    @Nullable
    private Integer ttl = 300;

    public static final String JSON_PROPERTY_REGISTRATION_TIMESTAMP = "registrationTimestamp";

    @Nullable
    private OffsetDateTime registrationTimestamp;

    public static final String JSON_PROPERTY_ENDPOINTS = "endpoints";

    @Nonnull
    private List<AgentEndpoint> endpoints = new ArrayList<>();

    public static final String JSON_PROPERTY_LINKS = "links";

    @Nonnull
    private List<Link> links = new ArrayList<>();

    public AgentListResponseItemsInner() {
    }

    public AgentListResponseItemsInner ansName(@Nonnull String ansName) {
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

    public AgentListResponseItemsInner agentId(@Nonnull UUID agentId) {
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

    public AgentListResponseItemsInner agentDisplayName(@Nonnull String agentDisplayName) {
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

    public AgentListResponseItemsInner agentDescription(@Nullable String agentDescription) {
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

    public AgentListResponseItemsInner version(@Nonnull String version) {
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

    public AgentListResponseItemsInner agentHost(@Nonnull String agentHost) {
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

    public AgentListResponseItemsInner status(@Nonnull AgentLifecycleStatus status) {
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

    public AgentListResponseItemsInner ttl(@Nullable Integer ttl) {
        this.ttl = ttl;
        return this;
    }

    /**
     * Get ttl
     * @return ttl
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TTL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Integer getTtl() {
        return ttl;
    }

    @JsonProperty(value = JSON_PROPERTY_TTL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTtl(@Nullable Integer ttl) {
        this.ttl = ttl;
    }

    public AgentListResponseItemsInner registrationTimestamp(@Nullable OffsetDateTime registrationTimestamp) {
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

    public AgentListResponseItemsInner endpoints(@Nonnull List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public AgentListResponseItemsInner addEndpointsItem(AgentEndpoint endpointsItem) {
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

    public AgentListResponseItemsInner links(@Nonnull List<Link> links) {
        this.links = links;
        return this;
    }

    public AgentListResponseItemsInner addLinksItem(Link linksItem) {
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
     * Return true if this AgentListResponse_items_inner object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentListResponseItemsInner agentListResponseItemsInner = (AgentListResponseItemsInner) o;
        return Objects.equals(this.ansName, agentListResponseItemsInner.ansName) &&
                Objects.equals(this.agentId, agentListResponseItemsInner.agentId) &&
                Objects.equals(this.agentDisplayName, agentListResponseItemsInner.agentDisplayName) &&
                Objects.equals(this.agentDescription, agentListResponseItemsInner.agentDescription) &&
                Objects.equals(this.version, agentListResponseItemsInner.version) &&
                Objects.equals(this.agentHost, agentListResponseItemsInner.agentHost) &&
                Objects.equals(this.status, agentListResponseItemsInner.status) &&
                Objects.equals(this.ttl, agentListResponseItemsInner.ttl) &&
                Objects.equals(this.registrationTimestamp, agentListResponseItemsInner.registrationTimestamp) &&
                Objects.equals(this.endpoints, agentListResponseItemsInner.endpoints) &&
                Objects.equals(this.links, agentListResponseItemsInner.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ansName, agentId, agentDisplayName, agentDescription, version, agentHost, status, ttl,
                registrationTimestamp, endpoints, links);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentListResponseItemsInner {\n");
        sb.append("    ansName: ").append(toIndentedString(ansName)).append("\n");
        sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
        sb.append("    agentDisplayName: ").append(toIndentedString(agentDisplayName)).append("\n");
        sb.append("    agentDescription: ").append(toIndentedString(agentDescription)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    agentHost: ").append(toIndentedString(agentHost)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    ttl: ").append(toIndentedString(ttl)).append("\n");
        sb.append("    registrationTimestamp: ").append(toIndentedString(registrationTimestamp)).append("\n");
        sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
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
