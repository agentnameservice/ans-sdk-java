package com.godaddy.ans.sdk.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.annotation.Nullable;

/**
 * RenewalSubmissionResponseChallenges
 */
@JsonPropertyOrder({
        RenewalSubmissionResponseChallenges.JSON_PROPERTY_DNS01,
        RenewalSubmissionResponseChallenges.JSON_PROPERTY_HTTP01
})
public class RenewalSubmissionResponseChallenges {
    public static final String JSON_PROPERTY_DNS01 = "dns01";

    @Nullable
    private ChallengeInfo dns01;

    public static final String JSON_PROPERTY_HTTP01 = "http01";

    @Nullable
    private ChallengeInfo http01;

    public RenewalSubmissionResponseChallenges() {
    }

    public RenewalSubmissionResponseChallenges dns01(@Nullable ChallengeInfo dns01) {
        this.dns01 = dns01;
        return this;
    }

    /**
     * Get dns01
     *
     * @return dns01
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DNS01, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public ChallengeInfo getDns01() {
        return dns01;
    }

    @JsonProperty(value = JSON_PROPERTY_DNS01, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDns01(@Nullable ChallengeInfo dns01) {
        this.dns01 = dns01;
    }

    public RenewalSubmissionResponseChallenges http01(@Nullable ChallengeInfo http01) {
        this.http01 = http01;
        return this;
    }

    /**
     * Get http01
     *
     * @return http01
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_HTTP01, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public ChallengeInfo getHttp01() {
        return http01;
    }

    @JsonProperty(value = JSON_PROPERTY_HTTP01, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setHttp01(@Nullable ChallengeInfo http01) {
        this.http01 = http01;
    }

    /**
     * Return true if this RenewalSubmissionResponse_challenges object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RenewalSubmissionResponseChallenges renewalSubmissionResponseChallenges =
                (RenewalSubmissionResponseChallenges) o;
        return Objects.equals(this.dns01, renewalSubmissionResponseChallenges.dns01) &&
                Objects.equals(this.http01, renewalSubmissionResponseChallenges.http01);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dns01, http01);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class RenewalSubmissionResponseChallenges {\n");
        sb.append("    dns01: ").append(toIndentedString(dns01)).append("\n");
        sb.append("    http01: ").append(toIndentedString(http01)).append("\n");
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
