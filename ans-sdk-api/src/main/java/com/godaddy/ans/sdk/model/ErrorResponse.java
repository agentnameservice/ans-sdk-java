package com.godaddy.ans.sdk.model;

import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * ErrorResponse
 */
@JsonPropertyOrder({
    ErrorResponse.JSON_PROPERTY_STATUS,
    ErrorResponse.JSON_PROPERTY_CODE,
    ErrorResponse.JSON_PROPERTY_MESSAGE,
    ErrorResponse.JSON_PROPERTY_DETAILS
})
public class ErrorResponse {
    /**
     * Gets or Sets status
     */
    public enum StatusEnum {
        ERROR("ERROR");

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

    public static final String JSON_PROPERTY_CODE = "code";


    @Nonnull
    private String code;

    public static final String JSON_PROPERTY_MESSAGE = "message";


    @Nonnull
    private String message;

    public static final String JSON_PROPERTY_DETAILS = "details";

    @Nullable
    private Map<String, Object> details = new HashMap<>();

    public ErrorResponse() {
    }

    public ErrorResponse status(@Nonnull StatusEnum status) {
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

    public ErrorResponse code(@Nonnull String code) {
        this.code = code;
        return this;
    }

    /**
     * Get code
     * @return code
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CODE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCode() {
        return code;
    }

    @JsonProperty(value = JSON_PROPERTY_CODE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCode(@Nonnull String code) {
        this.code = code;
    }

    public ErrorResponse message(@Nonnull String message) {
        this.message = message;
        return this;
    }

    /**
     * Get message
     * @return message
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_MESSAGE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = JSON_PROPERTY_MESSAGE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMessage(@Nonnull String message) {
        this.message = message;
    }

    public ErrorResponse details(@Nullable Map<String, Object> details) {
        this.details = details;
        return this;
    }

    public ErrorResponse putDetailsItem(String key, Object detailsItem) {
        if (this.details == null) {
            this.details = new HashMap<>();
        }
        this.details.put(key, detailsItem);
        return this;
    }

    /**
     * Get details
     * @return details
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DETAILS, required = false)
    @JsonInclude(content = JsonInclude.Include.ALWAYS, value = JsonInclude.Include.USE_DEFAULTS)
    public Map<String, Object> getDetails() {
        return details;
    }

    @JsonProperty(value = JSON_PROPERTY_DETAILS, required = false)
    @JsonInclude(content = JsonInclude.Include.ALWAYS, value = JsonInclude.Include.USE_DEFAULTS)
    public void setDetails(@Nullable Map<String, Object> details) {
        this.details = details;
    }

    /**
     * Return true if this ErrorResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorResponse errorResponse = (ErrorResponse) o;
        return Objects.equals(this.status, errorResponse.status) &&
                Objects.equals(this.code, errorResponse.code) &&
                Objects.equals(this.message, errorResponse.message) &&
                Objects.equals(this.details, errorResponse.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, message, details);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ErrorResponse {\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    details: ").append(toIndentedString(details)).append("\n");
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
