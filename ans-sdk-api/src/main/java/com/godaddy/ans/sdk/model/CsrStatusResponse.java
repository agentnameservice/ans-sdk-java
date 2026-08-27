package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * CsrStatusResponse
 */
@JsonPropertyOrder({
    CsrStatusResponse.JSON_PROPERTY_CSR_ID,
    CsrStatusResponse.JSON_PROPERTY_TYPE,
    CsrStatusResponse.JSON_PROPERTY_STATUS,
    CsrStatusResponse.JSON_PROPERTY_SUBMITTED_AT,
    CsrStatusResponse.JSON_PROPERTY_UPDATED_AT,
    CsrStatusResponse.JSON_PROPERTY_FAILURE_REASON
})
public class CsrStatusResponse {

    public static final String JSON_PROPERTY_CSR_ID = "csrId";
    @Nonnull
    private UUID csrId;

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        SERVER("SERVER"),

        IDENTITY("IDENTITY");

        private String value;

        TypeEnum(String value) {
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
        public static TypeEnum fromValue(String value) {
            for (TypeEnum b : TypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_TYPE = "type";

    @Nonnull
    private TypeEnum type;

    /**
     * Gets or Sets status
     */
    public enum StatusEnum {
        PENDING("PENDING"),

        SIGNED("SIGNED"),

        REJECTED("REJECTED");

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

    public static final String JSON_PROPERTY_SUBMITTED_AT = "submittedAt";

    @Nonnull
    private OffsetDateTime submittedAt;

    public static final String JSON_PROPERTY_UPDATED_AT = "updatedAt";

    @Nonnull
    private OffsetDateTime updatedAt;

    public static final String JSON_PROPERTY_FAILURE_REASON = "failureReason";

    @Nullable
    private String failureReason;

    public CsrStatusResponse() {
    }

    public CsrStatusResponse csrId(@Nonnull UUID csrId) {
        this.csrId = csrId;
        return this;
    }

    /**
     * Get csrId
     * @return csrId
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CSR_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public UUID getCsrId() {
        return csrId;
    }

    @JsonProperty(value = JSON_PROPERTY_CSR_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCsrId(@Nonnull UUID csrId) {
        this.csrId = csrId;
    }

    public CsrStatusResponse type(@Nonnull TypeEnum type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public TypeEnum getType() {
        return type;
    }

    @JsonProperty(value = JSON_PROPERTY_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setType(@Nonnull TypeEnum type) {
        this.type = type;
    }

    public CsrStatusResponse status(@Nonnull StatusEnum status) {
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

    public CsrStatusResponse submittedAt(@Nonnull OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
        return this;
    }

    /**
     * Get submittedAt
     * @return submittedAt
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_SUBMITTED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getSubmittedAt() {
        return submittedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_SUBMITTED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setSubmittedAt(@Nonnull OffsetDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public CsrStatusResponse updatedAt(@Nonnull OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get updatedAt
     * @return updatedAt
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_UPDATED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_UPDATED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setUpdatedAt(@Nonnull OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CsrStatusResponse failureReason(@Nullable String failureReason) {
        this.failureReason = failureReason;
        return this;
    }

    /**
     * Get failureReason
     * @return failureReason
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_FAILURE_REASON, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getFailureReason() {
        return failureReason;
    }

    @JsonProperty(value = JSON_PROPERTY_FAILURE_REASON, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setFailureReason(@Nullable String failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * Return true if this CsrStatusResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CsrStatusResponse csrStatusResponse = (CsrStatusResponse) o;
        return Objects.equals(this.csrId, csrStatusResponse.csrId) &&
                Objects.equals(this.type, csrStatusResponse.type) &&
                Objects.equals(this.status, csrStatusResponse.status) &&
                Objects.equals(this.submittedAt, csrStatusResponse.submittedAt) &&
                Objects.equals(this.updatedAt, csrStatusResponse.updatedAt) &&
                Objects.equals(this.failureReason, csrStatusResponse.failureReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csrId, type, status, submittedAt, updatedAt, failureReason);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CsrStatusResponse {\n");
        sb.append("    csrId: ").append(toIndentedString(csrId)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    failureReason: ").append(toIndentedString(failureReason)).append("\n");
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
