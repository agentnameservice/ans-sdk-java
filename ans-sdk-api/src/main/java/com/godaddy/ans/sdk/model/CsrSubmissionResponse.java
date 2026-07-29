package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * CsrSubmissionResponse
 */
@JsonPropertyOrder({
    CsrSubmissionResponse.JSON_PROPERTY_CSR_ID,
    CsrSubmissionResponse.JSON_PROPERTY_MESSAGE
})
public class CsrSubmissionResponse {
    public static final String JSON_PROPERTY_CSR_ID = "csrId";

    @Nonnull
    private UUID csrId;

    public static final String JSON_PROPERTY_MESSAGE = "message";

    @Nullable
    private String message;

    public CsrSubmissionResponse() {
    }

    public CsrSubmissionResponse csrId(@Nonnull UUID csrId) {
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

    public CsrSubmissionResponse message(@Nullable String message) {
        this.message = message;
        return this;
    }

    /**
     * Get message
     * @return message
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_MESSAGE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getMessage() {
        return message;
    }

    @JsonProperty(value = JSON_PROPERTY_MESSAGE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    /**
     * Return true if this CsrSubmissionResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CsrSubmissionResponse csrSubmissionResponse = (CsrSubmissionResponse) o;
        return Objects.equals(this.csrId, csrSubmissionResponse.csrId) &&
                Objects.equals(this.message, csrSubmissionResponse.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csrId, message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CsrSubmissionResponse {\n");
        sb.append("    csrId: ").append(toIndentedString(csrId)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
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
