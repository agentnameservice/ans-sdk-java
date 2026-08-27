package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * RenewalVerificationResponse
 */
@JsonPropertyOrder({
    RenewalVerificationResponse.JSON_PROPERTY_STATUS,
    RenewalVerificationResponse.JSON_PROPERTY_CSR_ID,
    RenewalVerificationResponse.JSON_PROPERTY_TLSA_DNS_RECORD,
    RenewalVerificationResponse.JSON_PROPERTY_NEXT_STEP
})
public class RenewalVerificationResponse {
    /**
     * Gets or Sets status
     */
    public enum StatusEnum {
        VERIFIED("VERIFIED"),

        ISSUING_CERTIFICATE("ISSUING_CERTIFICATE"),

        COMPLETED("COMPLETED");

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

    public static final String JSON_PROPERTY_CSR_ID = "csrId";

    @Nullable
    private UUID csrId;

    public static final String JSON_PROPERTY_TLSA_DNS_RECORD = "tlsaDnsRecord";

    @Nullable
    private DnsRecord tlsaDnsRecord;

    public static final String JSON_PROPERTY_NEXT_STEP = "nextStep";


    @Nonnull
    private NextStep nextStep;

    public RenewalVerificationResponse() {
    }

    public RenewalVerificationResponse status(@Nonnull StatusEnum status) {
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

    public RenewalVerificationResponse csrId(@Nullable UUID csrId) {
        this.csrId = csrId;
        return this;
    }

    /**
     * Get csrId
     * @return csrId
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CSR_ID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public UUID getCsrId() {
        return csrId;
    }

    @JsonProperty(value = JSON_PROPERTY_CSR_ID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCsrId(@Nullable UUID csrId) {
        this.csrId = csrId;
    }

    public RenewalVerificationResponse tlsaDnsRecord(@Nullable DnsRecord tlsaDnsRecord) {
        this.tlsaDnsRecord = tlsaDnsRecord;
        return this;
    }

    /**
     * Get tlsaDnsRecord
     * @return tlsaDnsRecord
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TLSA_DNS_RECORD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public DnsRecord getTlsaDnsRecord() {
        return tlsaDnsRecord;
    }

    @JsonProperty(value = JSON_PROPERTY_TLSA_DNS_RECORD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTlsaDnsRecord(@Nullable DnsRecord tlsaDnsRecord) {
        this.tlsaDnsRecord = tlsaDnsRecord;
    }

    public RenewalVerificationResponse nextStep(@Nonnull NextStep nextStep) {
        this.nextStep = nextStep;
        return this;
    }

    /**
     * Get nextStep
     * @return nextStep
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_NEXT_STEP, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public NextStep getNextStep() {
        return nextStep;
    }

    @JsonProperty(value = JSON_PROPERTY_NEXT_STEP, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setNextStep(@Nonnull NextStep nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * Return true if this RenewalVerificationResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RenewalVerificationResponse renewalVerificationResponse = (RenewalVerificationResponse) o;
        return Objects.equals(this.status, renewalVerificationResponse.status) &&
                Objects.equals(this.csrId, renewalVerificationResponse.csrId) &&
                Objects.equals(this.tlsaDnsRecord, renewalVerificationResponse.tlsaDnsRecord) &&
                Objects.equals(this.nextStep, renewalVerificationResponse.nextStep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, csrId, tlsaDnsRecord, nextStep);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RenewalVerificationResponse {\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    csrId: ").append(toIndentedString(csrId)).append("\n");
        sb.append("    tlsaDnsRecord: ").append(toIndentedString(tlsaDnsRecord)).append("\n");
        sb.append("    nextStep: ").append(toIndentedString(nextStep)).append("\n");
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
