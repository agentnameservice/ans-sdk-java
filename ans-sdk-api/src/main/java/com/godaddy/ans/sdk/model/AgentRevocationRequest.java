package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * AgentRevocationRequest
 */
@JsonPropertyOrder({
    AgentRevocationRequest.JSON_PROPERTY_REASON,
    AgentRevocationRequest.JSON_PROPERTY_COMMENTS
})
public class AgentRevocationRequest {
    public static final String JSON_PROPERTY_REASON = "reason";

    @Nonnull
    private RevocationReason reason;

    public static final String JSON_PROPERTY_COMMENTS = "comments";

    @Nullable
    private String comments;

    public AgentRevocationRequest() {
    }

    public AgentRevocationRequest reason(@Nonnull RevocationReason reason) {
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

    public AgentRevocationRequest comments(@Nullable String comments) {
        this.comments = comments;
        return this;
    }

    /**
     * Get comments
     * @return comments
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_COMMENTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getComments() {
        return comments;
    }

    @JsonProperty(value = JSON_PROPERTY_COMMENTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setComments(@Nullable String comments) {
        this.comments = comments;
    }

    /**
     * Return true if this AgentRevocationRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentRevocationRequest agentRevocationRequest = (AgentRevocationRequest) o;
        return Objects.equals(this.reason, agentRevocationRequest.reason) &&
                Objects.equals(this.comments, agentRevocationRequest.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason, comments);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentRevocationRequest {\n");
        sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
        sb.append("    comments: ").append(toIndentedString(comments)).append("\n");
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
