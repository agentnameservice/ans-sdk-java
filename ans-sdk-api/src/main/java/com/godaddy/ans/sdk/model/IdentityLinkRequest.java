package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * The batch of the caller&#39;s agents to bind — one owner-gated call, no challenge, no signature; the whole batch
 * seals as ONE IDENTITY_LINKED event on the identity stream.
 */
@JsonPropertyOrder({
    IdentityLinkRequest.JSON_PROPERTY_AGENT_IDS
})
public class IdentityLinkRequest {
    public static final String JSON_PROPERTY_AGENT_IDS = "agentIds";

    @Nonnull
    private List<UUID> agentIds = new ArrayList<>();

    public IdentityLinkRequest() {
    }

    public IdentityLinkRequest agentIds(@Nonnull List<UUID> agentIds) {
        this.agentIds = agentIds;
        return this;
    }

    public IdentityLinkRequest addAgentIdsItem(UUID agentIdsItem) {
        if (this.agentIds == null) {
            this.agentIds = new ArrayList<>();
        }
        this.agentIds.add(agentIdsItem);
        return this;
    }

    /**
     * Get agentIds
     * @return agentIds
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_IDS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<UUID> getAgentIds() {
        return agentIds;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_IDS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentIds(@Nonnull List<UUID> agentIds) {
        this.agentIds = agentIds;
    }

    /**
     * Return true if this IdentityLinkRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityLinkRequest identityLinkRequest = (IdentityLinkRequest) o;
        return Objects.equals(this.agentIds, identityLinkRequest.agentIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentIds);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityLinkRequest {\n");
        sb.append("    agentIds: ").append(toIndentedString(agentIds)).append("\n");
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
