package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * IdentityDetailsLinkedAgentsInner
 */
@JsonPropertyOrder({
    IdentityDetailsLinkedAgentsInner.JSON_PROPERTY_AGENT_ID,
    IdentityDetailsLinkedAgentsInner.JSON_PROPERTY_LINKED_AT
})
public class IdentityDetailsLinkedAgentsInner {
    public static final String JSON_PROPERTY_AGENT_ID = "agentId";

    @Nonnull
    private UUID agentId;

    public static final String JSON_PROPERTY_LINKED_AT = "linkedAt";

    @Nullable
    private OffsetDateTime linkedAt;

    public IdentityDetailsLinkedAgentsInner() {
    }

    public IdentityDetailsLinkedAgentsInner agentId(@Nonnull UUID agentId) {
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

    public IdentityDetailsLinkedAgentsInner linkedAt(@Nullable OffsetDateTime linkedAt) {
        this.linkedAt = linkedAt;
        return this;
    }

    /**
     * Get linkedAt
     * @return linkedAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LINKED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getLinkedAt() {
        return linkedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLinkedAt(@Nullable OffsetDateTime linkedAt) {
        this.linkedAt = linkedAt;
    }

    /**
     * Return true if this IdentityDetails_linkedAgents_inner object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityDetailsLinkedAgentsInner identityDetailsLinkedAgentsInner = (IdentityDetailsLinkedAgentsInner) o;
        return Objects.equals(this.agentId, identityDetailsLinkedAgentsInner.agentId) &&
                Objects.equals(this.linkedAt, identityDetailsLinkedAgentsInner.linkedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId, linkedAt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityDetailsLinkedAgentsInner {\n");
        sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
        sb.append("    linkedAt: ").append(toIndentedString(linkedAt)).append("\n");
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
