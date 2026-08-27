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
 * RenewalStatusResponse
 */
@JsonPropertyOrder({
    RenewalStatusResponse.JSON_PROPERTY_RENEWAL_TYPE,
    RenewalStatusResponse.JSON_PROPERTY_STATUS,
    RenewalStatusResponse.JSON_PROPERTY_CSR_ID,
    RenewalStatusResponse.JSON_PROPERTY_CHALLENGES,
    RenewalStatusResponse.JSON_PROPERTY_TLSA_DNS_RECORD,
    RenewalStatusResponse.JSON_PROPERTY_FAILURE_REASON,
    RenewalStatusResponse.JSON_PROPERTY_EXPIRES_AT,
    RenewalStatusResponse.JSON_PROPERTY_NEXT_STEP
})
public class RenewalStatusResponse {
    /**
     * Gets or Sets renewalType
     */
    public enum RenewalTypeEnum {
        SERVER_CSR("SERVER_CSR"),

        SERVER_BYOC("SERVER_BYOC");

        private String value;

        RenewalTypeEnum(String value) {
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
        public static RenewalTypeEnum fromValue(String value) {
            for (RenewalTypeEnum b : RenewalTypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_RENEWAL_TYPE = "renewalType";


    @Nonnull
    private RenewalTypeEnum renewalType;

    /**
     * Gets or Sets status
     */
    public enum StatusEnum {
        PENDING_VALIDATION("PENDING_VALIDATION"),

        ISSUING_CERTIFICATE("ISSUING_CERTIFICATE"),

        COMPLETED("COMPLETED"),

        FAILED("FAILED"),

        EXPIRED("EXPIRED");

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

    public static final String JSON_PROPERTY_CHALLENGES = "challenges";

    @Nullable
    private RenewalSubmissionResponseChallenges challenges;

    public static final String JSON_PROPERTY_TLSA_DNS_RECORD = "tlsaDnsRecord";

    @Nullable
    private DnsRecord tlsaDnsRecord;

    public static final String JSON_PROPERTY_FAILURE_REASON = "failureReason";

    @Nullable
    private String failureReason;

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";


    @Nonnull
    private OffsetDateTime expiresAt;

    public static final String JSON_PROPERTY_NEXT_STEP = "nextStep";


    @Nonnull
    private NextStep nextStep;

    public RenewalStatusResponse() {
    }

    public RenewalStatusResponse renewalType(@Nonnull RenewalTypeEnum renewalType) {
        this.renewalType = renewalType;
        return this;
    }

    /**
     * Get renewalType
     * @return renewalType
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_RENEWAL_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public RenewalTypeEnum getRenewalType() {
        return renewalType;
    }

    @JsonProperty(value = JSON_PROPERTY_RENEWAL_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setRenewalType(@Nonnull RenewalTypeEnum renewalType) {
        this.renewalType = renewalType;
    }

    public RenewalStatusResponse status(@Nonnull StatusEnum status) {
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

    public RenewalStatusResponse csrId(@Nullable UUID csrId) {
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

    public RenewalStatusResponse challenges(@Nullable RenewalSubmissionResponseChallenges challenges) {
        this.challenges = challenges;
        return this;
    }

    /**
     * Get challenges
     * @return challenges
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CHALLENGES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public RenewalSubmissionResponseChallenges getChallenges() {
        return challenges;
    }

    @JsonProperty(value = JSON_PROPERTY_CHALLENGES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setChallenges(@Nullable RenewalSubmissionResponseChallenges challenges) {
        this.challenges = challenges;
    }

    public RenewalStatusResponse tlsaDnsRecord(@Nullable DnsRecord tlsaDnsRecord) {
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

    public RenewalStatusResponse failureReason(@Nullable String failureReason) {
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

    public RenewalStatusResponse expiresAt(@Nonnull OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    /**
     * Get expiresAt
     * @return expiresAt
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_EXPIRES_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    @JsonProperty(value = JSON_PROPERTY_EXPIRES_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setExpiresAt(@Nonnull OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public RenewalStatusResponse nextStep(@Nonnull NextStep nextStep) {
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
     * Return true if this RenewalStatusResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RenewalStatusResponse renewalStatusResponse = (RenewalStatusResponse) o;
        return Objects.equals(this.renewalType, renewalStatusResponse.renewalType) &&
                Objects.equals(this.status, renewalStatusResponse.status) &&
                Objects.equals(this.csrId, renewalStatusResponse.csrId) &&
                Objects.equals(this.challenges, renewalStatusResponse.challenges) &&
                Objects.equals(this.tlsaDnsRecord, renewalStatusResponse.tlsaDnsRecord) &&
                Objects.equals(this.failureReason, renewalStatusResponse.failureReason) &&
                Objects.equals(this.expiresAt, renewalStatusResponse.expiresAt) &&
                Objects.equals(this.nextStep, renewalStatusResponse.nextStep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(renewalType, status, csrId, challenges, tlsaDnsRecord, failureReason, expiresAt, nextStep);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RenewalStatusResponse {\n");
        sb.append("    renewalType: ").append(toIndentedString(renewalType)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    csrId: ").append(toIndentedString(csrId)).append("\n");
        sb.append("    challenges: ").append(toIndentedString(challenges)).append("\n");
        sb.append("    tlsaDnsRecord: ").append(toIndentedString(tlsaDnsRecord)).append("\n");
        sb.append("    failureReason: ").append(toIndentedString(failureReason)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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
