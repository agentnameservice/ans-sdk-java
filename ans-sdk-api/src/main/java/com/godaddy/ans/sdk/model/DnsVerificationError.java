package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * DnsVerificationError
 */
@JsonPropertyOrder({
    DnsVerificationError.JSON_PROPERTY_STATUS,
    DnsVerificationError.JSON_PROPERTY_MISSING_RECORDS,
    DnsVerificationError.JSON_PROPERTY_INCORRECT_RECORDS
})
public class DnsVerificationError {
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

    @Nullable
    private StatusEnum status;

    public static final String JSON_PROPERTY_MISSING_RECORDS = "missingRecords";

    @Nullable
    private List<DnsRecord> missingRecords = new ArrayList<>();

    public static final String JSON_PROPERTY_INCORRECT_RECORDS = "incorrectRecords";

    @Nullable
    private List<DnsVerificationErrorIncorrectRecordsInner> incorrectRecords = new ArrayList<>();

    public DnsVerificationError() {
    }

    public DnsVerificationError status(@Nullable StatusEnum status) {
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
    public StatusEnum getStatus() {
        return status;
    }

    @JsonProperty(value = JSON_PROPERTY_STATUS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setStatus(@Nullable StatusEnum status) {
        this.status = status;
    }

    public DnsVerificationError missingRecords(@Nullable List<DnsRecord> missingRecords) {
        this.missingRecords = missingRecords;
        return this;
    }

    public DnsVerificationError addMissingRecordsItem(DnsRecord missingRecordsItem) {
        if (this.missingRecords == null) {
            this.missingRecords = new ArrayList<>();
        }
        this.missingRecords.add(missingRecordsItem);
        return this;
    }

    /**
     * Get missingRecords
     * @return missingRecords
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_MISSING_RECORDS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<DnsRecord> getMissingRecords() {
        return missingRecords;
    }

    @JsonProperty(value = JSON_PROPERTY_MISSING_RECORDS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setMissingRecords(@Nullable List<DnsRecord> missingRecords) {
        this.missingRecords = missingRecords;
    }

    public DnsVerificationError incorrectRecords(
            @Nullable List<DnsVerificationErrorIncorrectRecordsInner> incorrectRecords) {
        this.incorrectRecords = incorrectRecords;
        return this;
    }

    public DnsVerificationError addIncorrectRecordsItem(
            DnsVerificationErrorIncorrectRecordsInner incorrectRecordsItem) {
        if (this.incorrectRecords == null) {
            this.incorrectRecords = new ArrayList<>();
        }
        this.incorrectRecords.add(incorrectRecordsItem);
        return this;
    }

    /**
     * Get incorrectRecords
     * @return incorrectRecords
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_INCORRECT_RECORDS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<DnsVerificationErrorIncorrectRecordsInner> getIncorrectRecords() {
        return incorrectRecords;
    }

    @JsonProperty(value = JSON_PROPERTY_INCORRECT_RECORDS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIncorrectRecords(@Nullable List<DnsVerificationErrorIncorrectRecordsInner> incorrectRecords) {
        this.incorrectRecords = incorrectRecords;
    }

    /**
     * Return true if this DnsVerificationError object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DnsVerificationError dnsVerificationError = (DnsVerificationError) o;
        return Objects.equals(this.status, dnsVerificationError.status) &&
                Objects.equals(this.missingRecords, dnsVerificationError.missingRecords) &&
                Objects.equals(this.incorrectRecords, dnsVerificationError.incorrectRecords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, missingRecords, incorrectRecords);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DnsVerificationError {\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    missingRecords: ").append(toIndentedString(missingRecords)).append("\n");
        sb.append("    incorrectRecords: ").append(toIndentedString(incorrectRecords)).append("\n");
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
