package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * RenewalSubmissionResponse
 */
@JsonPropertyOrder({
    RenewalSubmissionResponse.JSON_PROPERTY_RENEWAL_TYPE,
    RenewalSubmissionResponse.JSON_PROPERTY_STATUS,
    RenewalSubmissionResponse.JSON_PROPERTY_CSR_ID,
    RenewalSubmissionResponse.JSON_PROPERTY_CHALLENGES,
    RenewalSubmissionResponse.JSON_PROPERTY_EXPIRES_AT,
    RenewalSubmissionResponse.JSON_PROPERTY_NEXT_STEP,
    RenewalSubmissionResponse.JSON_PROPERTY_LINKS
})
public class RenewalSubmissionResponse {
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

        ISSUING_CERTIFICATE("ISSUING_CERTIFICATE");

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

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";


    @Nonnull
    private OffsetDateTime expiresAt;

    public static final String JSON_PROPERTY_NEXT_STEP = "nextStep";


    @Nonnull
    private NextStep nextStep;

    public static final String JSON_PROPERTY_LINKS = "links";

    @Nullable
    private List<Link> links = new ArrayList<>();

    public RenewalSubmissionResponse() {
    }

    public RenewalSubmissionResponse renewalType(@Nonnull RenewalTypeEnum renewalType) {
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

    public RenewalSubmissionResponse status(@Nonnull StatusEnum status) {
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

    public RenewalSubmissionResponse csrId(@Nullable UUID csrId) {
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

    public RenewalSubmissionResponse challenges(@Nullable RenewalSubmissionResponseChallenges challenges) {
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

    public RenewalSubmissionResponse expiresAt(@Nonnull OffsetDateTime expiresAt) {
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

    public RenewalSubmissionResponse nextStep(@Nonnull NextStep nextStep) {
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

    public RenewalSubmissionResponse links(@Nullable List<Link> links) {
        this.links = links;
        return this;
    }

    public RenewalSubmissionResponse addLinksItem(Link linksItem) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(linksItem);
        return this;
    }

    /**
     * Get links
     * @return links
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LINKS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<Link> getLinks() {
        return links;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLinks(@Nullable List<Link> links) {
        this.links = links;
    }

    /**
     * Return true if this RenewalSubmissionResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RenewalSubmissionResponse renewalSubmissionResponse = (RenewalSubmissionResponse) o;
        return Objects.equals(this.renewalType, renewalSubmissionResponse.renewalType) &&
                Objects.equals(this.status, renewalSubmissionResponse.status) &&
                Objects.equals(this.csrId, renewalSubmissionResponse.csrId) &&
                Objects.equals(this.challenges, renewalSubmissionResponse.challenges) &&
                Objects.equals(this.expiresAt, renewalSubmissionResponse.expiresAt) &&
                Objects.equals(this.nextStep, renewalSubmissionResponse.nextStep) &&
                Objects.equals(this.links, renewalSubmissionResponse.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(renewalType, status, csrId, challenges, expiresAt, nextStep, links);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RenewalSubmissionResponse {\n");
        sb.append("    renewalType: ").append(toIndentedString(renewalType)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    csrId: ").append(toIndentedString(csrId)).append("\n");
        sb.append("    challenges: ").append(toIndentedString(challenges)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
        sb.append("    nextStep: ").append(toIndentedString(nextStep)).append("\n");
        sb.append("    links: ").append(toIndentedString(links)).append("\n");
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
