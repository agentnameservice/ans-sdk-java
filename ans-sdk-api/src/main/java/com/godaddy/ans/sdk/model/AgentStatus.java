package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * AgentStatus
 */
@JsonPropertyOrder({
    AgentStatus.JSON_PROPERTY_STATUS,
    AgentStatus.JSON_PROPERTY_PHASE,
    AgentStatus.JSON_PROPERTY_COMPLETED_STEPS,
    AgentStatus.JSON_PROPERTY_PENDING_STEPS,
    AgentStatus.JSON_PROPERTY_CREATED_AT,
    AgentStatus.JSON_PROPERTY_UPDATED_AT,
    AgentStatus.JSON_PROPERTY_EXPIRES_AT
})
public class AgentStatus {
    public static final String JSON_PROPERTY_STATUS = "status";

    @Nullable
    private AgentLifecycleStatus status;

    /**
     * Gets or Sets phase
     */
    public enum PhaseEnum {
        INITIALIZATION("INITIALIZATION"),

        DOMAIN_VALIDATION("DOMAIN_VALIDATION"),

        CERTIFICATE_ISSUANCE("CERTIFICATE_ISSUANCE"),

        DNS_PROVISIONING("DNS_PROVISIONING"),

        COMPLETED("COMPLETED");

        private String value;

        PhaseEnum(String value) {
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
        public static PhaseEnum fromValue(String value) {
            for (PhaseEnum b : PhaseEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_PHASE = "phase";

    @Nullable
    private PhaseEnum phase;

    public static final String JSON_PROPERTY_COMPLETED_STEPS = "completedSteps";

    @Nullable
    private List<String> completedSteps = new ArrayList<>();

    public static final String JSON_PROPERTY_PENDING_STEPS = "pendingSteps";

    @Nullable
    private List<String> pendingSteps = new ArrayList<>();

    public static final String JSON_PROPERTY_CREATED_AT = "createdAt";

    @Nullable
    private OffsetDateTime createdAt;

    public static final String JSON_PROPERTY_UPDATED_AT = "updatedAt";

    @Nullable
    private OffsetDateTime updatedAt;

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";

    @Nullable
    private OffsetDateTime expiresAt;

    public AgentStatus() {
    }

    public AgentStatus status(@Nullable AgentLifecycleStatus status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * @return status
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_STATUS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public AgentLifecycleStatus getStatus() {
        return status;
    }

    @JsonProperty(value = JSON_PROPERTY_STATUS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setStatus(@Nullable AgentLifecycleStatus status) {
        this.status = status;
    }

    public AgentStatus phase(@Nullable PhaseEnum phase) {
        this.phase = phase;
        return this;
    }

    /**
     * Get phase
     * @return phase
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PHASE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public PhaseEnum getPhase() {
        return phase;
    }

    @JsonProperty(value = JSON_PROPERTY_PHASE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPhase(@Nullable PhaseEnum phase) {
        this.phase = phase;
    }

    public AgentStatus completedSteps(@Nullable List<String> completedSteps) {
        this.completedSteps = completedSteps;
        return this;
    }

    public AgentStatus addCompletedStepsItem(String completedStepsItem) {
        if (this.completedSteps == null) {
            this.completedSteps = new ArrayList<>();
        }
        this.completedSteps.add(completedStepsItem);
        return this;
    }

    /**
     * Get completedSteps
     * @return completedSteps
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_COMPLETED_STEPS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getCompletedSteps() {
        return completedSteps;
    }

    @JsonProperty(value = JSON_PROPERTY_COMPLETED_STEPS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCompletedSteps(@Nullable List<String> completedSteps) {
        this.completedSteps = completedSteps;
    }

    public AgentStatus pendingSteps(@Nullable List<String> pendingSteps) {
        this.pendingSteps = pendingSteps;
        return this;
    }

    public AgentStatus addPendingStepsItem(String pendingStepsItem) {
        if (this.pendingSteps == null) {
            this.pendingSteps = new ArrayList<>();
        }
        this.pendingSteps.add(pendingStepsItem);
        return this;
    }

    /**
     * Get pendingSteps
     * @return pendingSteps
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PENDING_STEPS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getPendingSteps() {
        return pendingSteps;
    }

    @JsonProperty(value = JSON_PROPERTY_PENDING_STEPS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPendingSteps(@Nullable List<String> pendingSteps) {
        this.pendingSteps = pendingSteps;
    }

    public AgentStatus createdAt(@Nullable OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get createdAt
     * @return createdAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CREATED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(value = JSON_PROPERTY_CREATED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AgentStatus updatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get updatedAt
     * @return updatedAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_UPDATED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_UPDATED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AgentStatus expiresAt(@Nullable OffsetDateTime expiresAt) {
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

    /**
     * Return true if this AgentStatus object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentStatus agentStatus = (AgentStatus) o;
        return Objects.equals(this.status, agentStatus.status) &&
                Objects.equals(this.phase, agentStatus.phase) &&
                Objects.equals(this.completedSteps, agentStatus.completedSteps) &&
                Objects.equals(this.pendingSteps, agentStatus.pendingSteps) &&
                Objects.equals(this.createdAt, agentStatus.createdAt) &&
                Objects.equals(this.updatedAt, agentStatus.updatedAt) &&
                Objects.equals(this.expiresAt, agentStatus.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, phase, completedSteps, pendingSteps, createdAt, updatedAt, expiresAt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentStatus {\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
        sb.append("    completedSteps: ").append(toIndentedString(completedSteps)).append("\n");
        sb.append("    pendingSteps: ").append(toIndentedString(pendingSteps)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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
