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
 * One ANS lifecycle event with agent details and event metadata.
 */
@JsonPropertyOrder({
    EventItem.JSON_PROPERTY_LOG_ID,
    EventItem.JSON_PROPERTY_EVENT_TYPE,
    EventItem.JSON_PROPERTY_CREATED_AT,
    EventItem.JSON_PROPERTY_EXPIRES_AT,
    EventItem.JSON_PROPERTY_AGENT_ID,
    EventItem.JSON_PROPERTY_ANS_NAME,
    EventItem.JSON_PROPERTY_AGENT_HOST,
    EventItem.JSON_PROPERTY_AGENT_DISPLAY_NAME,
    EventItem.JSON_PROPERTY_AGENT_DESCRIPTION,
    EventItem.JSON_PROPERTY_VERSION,
    EventItem.JSON_PROPERTY_PROVIDER_ID,
    EventItem.JSON_PROPERTY_ENDPOINTS
})
public class EventItem {
    public static final String JSON_PROPERTY_LOG_ID = "logId";


    @Nonnull
    private String logId;

    /**
     * Type of ANS event.
     */
    public enum EventTypeEnum {
        AGENT_DEPRECATED("AGENT_DEPRECATED"),

        AGENT_REGISTERED("AGENT_REGISTERED"),

        AGENT_REVOKED("AGENT_REVOKED"),

        AGENT_RENEWED("AGENT_RENEWED");

        private String value;

        EventTypeEnum(String value) {
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
        public static EventTypeEnum fromValue(String value) {
            for (EventTypeEnum b : EventTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_EVENT_TYPE = "eventType";


    @Nonnull
    private EventTypeEnum eventType;

    public static final String JSON_PROPERTY_CREATED_AT = "createdAt";


    @Nonnull
    private OffsetDateTime createdAt;

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";

    @Nullable
    private OffsetDateTime expiresAt;

    public static final String JSON_PROPERTY_AGENT_ID = "agentId";


    @Nonnull
    private UUID agentId;

    public static final String JSON_PROPERTY_ANS_NAME = "ansName";


    @Nonnull
    private String ansName;

    public static final String JSON_PROPERTY_AGENT_HOST = "agentHost";


    @Nonnull
    private String agentHost;

    public static final String JSON_PROPERTY_AGENT_DISPLAY_NAME = "agentDisplayName";

    @Nullable
    private String agentDisplayName;

    public static final String JSON_PROPERTY_AGENT_DESCRIPTION = "agentDescription";

    @Nullable
    private String agentDescription;

    public static final String JSON_PROPERTY_VERSION = "version";


    @Nonnull
    private String version;

    public static final String JSON_PROPERTY_PROVIDER_ID = "providerId";

    @Nullable
    private String providerId;

    public static final String JSON_PROPERTY_ENDPOINTS = "endpoints";

    @Nullable
    private List<AgentEndpoint> endpoints = new ArrayList<>();

    public EventItem() {
    }

    public EventItem logId(@Nonnull String logId) {
        this.logId = logId;
        return this;
    }

    /**
     * Unique identifier for this event in the stream.
     * @return logId
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_LOG_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getLogId() {
        return logId;
    }

    @JsonProperty(value = JSON_PROPERTY_LOG_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setLogId(@Nonnull String logId) {
        this.logId = logId;
    }

    public EventItem eventType(@Nonnull EventTypeEnum eventType) {
        this.eventType = eventType;
        return this;
    }

    /**
     * Type of ANS event.
     * @return eventType
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_EVENT_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public EventTypeEnum getEventType() {
        return eventType;
    }

    @JsonProperty(value = JSON_PROPERTY_EVENT_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setEventType(@Nonnull EventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public EventItem createdAt(@Nonnull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Timestamp when the event was created (producer time).
     * @return createdAt
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CREATED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(value = JSON_PROPERTY_CREATED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCreatedAt(@Nonnull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EventItem expiresAt(@Nullable OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    /**
     * When the agent&#39;s registration expires (if applicable).
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

    public EventItem agentId(@Nonnull UUID agentId) {
        this.agentId = agentId;
        return this;
    }

    /**
     * Unique identifier of the agent.
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

    public EventItem ansName(@Nonnull String ansName) {
        this.ansName = ansName;
        return this;
    }

    /**
     * Fully qualified ANS name (ans://{version}.{agentHost}).
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

    public EventItem agentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
        return this;
    }

    /**
     * The agent&#39;s hosting domain.
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

    public EventItem agentDisplayName(@Nullable String agentDisplayName) {
        this.agentDisplayName = agentDisplayName;
        return this;
    }

    /**
     * Human-readable display name for the agent.
     * @return agentDisplayName
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_AGENT_DISPLAY_NAME, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getAgentDisplayName() {
        return agentDisplayName;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_DISPLAY_NAME, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAgentDisplayName(@Nullable String agentDisplayName) {
        this.agentDisplayName = agentDisplayName;
    }

    public EventItem agentDescription(@Nullable String agentDescription) {
        this.agentDescription = agentDescription;
        return this;
    }

    /**
     * Description of the agent.
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

    public EventItem version(@Nonnull String version) {
        this.version = version;
        return this;
    }

    /**
     * Semantic version of the agent.
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

    public EventItem providerId(@Nullable String providerId) {
        this.providerId = providerId;
        return this;
    }

    /**
     * Provider identifier (production field). This OSS RA never emits it — the only principal id is the
     * registrant&#39;s owner id, which is not exposed.
     * @return providerId
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PROVIDER_ID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getProviderId() {
        return providerId;
    }

    @JsonProperty(value = JSON_PROPERTY_PROVIDER_ID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setProviderId(@Nullable String providerId) {
        this.providerId = providerId;
    }

    public EventItem endpoints(@Nullable List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public EventItem addEndpointsItem(AgentEndpoint endpointsItem) {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<>();
        }
        this.endpoints.add(endpointsItem);
        return this;
    }

    /**
     * Agent endpoints with protocol-specific configuration.
     * @return endpoints
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_ENDPOINTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<AgentEndpoint> getEndpoints() {
        return endpoints;
    }

    @JsonProperty(value = JSON_PROPERTY_ENDPOINTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEndpoints(@Nullable List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    /**
     * Return true if this EventItem object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventItem eventItem = (EventItem) o;
        return Objects.equals(this.logId, eventItem.logId) &&
                Objects.equals(this.eventType, eventItem.eventType) &&
                Objects.equals(this.createdAt, eventItem.createdAt) &&
                Objects.equals(this.expiresAt, eventItem.expiresAt) &&
                Objects.equals(this.agentId, eventItem.agentId) &&
                Objects.equals(this.ansName, eventItem.ansName) &&
                Objects.equals(this.agentHost, eventItem.agentHost) &&
                Objects.equals(this.agentDisplayName, eventItem.agentDisplayName) &&
                Objects.equals(this.agentDescription, eventItem.agentDescription) &&
                Objects.equals(this.version, eventItem.version) &&
                Objects.equals(this.providerId, eventItem.providerId) &&
                Objects.equals(this.endpoints, eventItem.endpoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, eventType, createdAt, expiresAt, agentId, ansName, agentHost, agentDisplayName,
                agentDescription, version, providerId, endpoints);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EventItem {\n");
        sb.append("    logId: ").append(toIndentedString(logId)).append("\n");
        sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
        sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
        sb.append("    ansName: ").append(toIndentedString(ansName)).append("\n");
        sb.append("    agentHost: ").append(toIndentedString(agentHost)).append("\n");
        sb.append("    agentDisplayName: ").append(toIndentedString(agentDisplayName)).append("\n");
        sb.append("    agentDescription: ").append(toIndentedString(agentDescription)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    providerId: ").append(toIndentedString(providerId)).append("\n");
        sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
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
