package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * CsrSubmissionRequest
 */
@JsonPropertyOrder({
    CsrSubmissionRequest.JSON_PROPERTY_CSR_P_E_M
})
public class CsrSubmissionRequest {
    public static final String JSON_PROPERTY_CSR_P_E_M = "csrPEM";

    @Nonnull
    private String csrPEM;

    public CsrSubmissionRequest() {
    }

    public CsrSubmissionRequest csrPEM(@Nonnull String csrPEM) {
        this.csrPEM = csrPEM;
        return this;
    }

    /**
     * Get csrPEM
     * @return csrPEM
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CSR_P_E_M, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCsrPEM() {
        return csrPEM;
    }

    @JsonProperty(value = JSON_PROPERTY_CSR_P_E_M, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCsrPEM(@Nonnull String csrPEM) {
        this.csrPEM = csrPEM;
    }

    /**
     * Return true if this CsrSubmissionRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CsrSubmissionRequest csrSubmissionRequest = (CsrSubmissionRequest) o;
        return Objects.equals(this.csrPEM, csrSubmissionRequest.csrPEM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csrPEM);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CsrSubmissionRequest {\n");
        sb.append("    csrPEM: ").append(toIndentedString(csrPEM)).append("\n");
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
