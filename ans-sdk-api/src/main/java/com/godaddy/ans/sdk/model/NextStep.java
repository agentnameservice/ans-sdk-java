package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * NextStep
 */
@JsonPropertyOrder({
    NextStep.JSON_PROPERTY_ACTION,
    NextStep.JSON_PROPERTY_DESCRIPTION,
    NextStep.JSON_PROPERTY_ENDPOINT,
    NextStep.JSON_PROPERTY_ESTIMATED_TIME_MINUTES
})
public class NextStep {
    /**
     * Gets or Sets action
     */
    public enum ActionEnum {
        CONFIGURE_DNS("CONFIGURE_DNS"),

        CONFIGURE_HTTP("CONFIGURE_HTTP"),

        VERIFY_DNS("VERIFY_DNS"),

        VALIDATE_DOMAIN("VALIDATE_DOMAIN"),

        WAIT("WAIT"),

        CANCEL("CANCEL");

        private String value;

        ActionEnum(String value) {
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
        public static ActionEnum fromValue(String value) {
            for (ActionEnum b : ActionEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_ACTION = "action";

    @Nullable
    private ActionEnum action;

    public static final String JSON_PROPERTY_DESCRIPTION = "description";

    @Nullable
    private String description;

    public static final String JSON_PROPERTY_ENDPOINT = "endpoint";

    @Nullable
    private URI endpoint;

    public static final String JSON_PROPERTY_ESTIMATED_TIME_MINUTES = "estimatedTimeMinutes";

    @Nullable
    private Integer estimatedTimeMinutes;

    public NextStep() {
    }

    public NextStep action(@Nullable ActionEnum action) {
        this.action = action;
        return this;
    }

    /**
     * Get action
     * @return action
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_ACTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public ActionEnum getAction() {
        return action;
    }

    @JsonProperty(value = JSON_PROPERTY_ACTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAction(@Nullable ActionEnum action) {
        this.action = action;
    }

    public NextStep description(@Nullable String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     * @return description
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getDescription() {
        return description;
    }

    @JsonProperty(value = JSON_PROPERTY_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public NextStep endpoint(@Nullable URI endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Get endpoint
     * @return endpoint
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_ENDPOINT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public URI getEndpoint() {
        return endpoint;
    }

    @JsonProperty(value = JSON_PROPERTY_ENDPOINT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEndpoint(@Nullable URI endpoint) {
        this.endpoint = endpoint;
    }

    public NextStep estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
        this.estimatedTimeMinutes = estimatedTimeMinutes;
        return this;
    }

    /**
     * Get estimatedTimeMinutes
     * @return estimatedTimeMinutes
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_ESTIMATED_TIME_MINUTES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Integer getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }

    @JsonProperty(value = JSON_PROPERTY_ESTIMATED_TIME_MINUTES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
        this.estimatedTimeMinutes = estimatedTimeMinutes;
    }

    /**
     * Return true if this NextStep object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NextStep nextStep = (NextStep) o;
        return Objects.equals(this.action, nextStep.action) &&
                Objects.equals(this.description, nextStep.description) &&
                Objects.equals(this.endpoint, nextStep.endpoint) &&
                Objects.equals(this.estimatedTimeMinutes, nextStep.estimatedTimeMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, description, endpoint, estimatedTimeMinutes);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NextStep {\n");
        sb.append("    action: ").append(toIndentedString(action)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
        sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
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
