package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * DnsVerificationErrorIncorrectRecordsInner
 */
@JsonPropertyOrder({
    DnsVerificationErrorIncorrectRecordsInner.JSON_PROPERTY_RECORD,
    DnsVerificationErrorIncorrectRecordsInner.JSON_PROPERTY_FOUND,
    DnsVerificationErrorIncorrectRecordsInner.JSON_PROPERTY_EXPECTED
})
public class DnsVerificationErrorIncorrectRecordsInner {
    public static final String JSON_PROPERTY_RECORD = "record";

    @Nullable
    private DnsRecord record;

    public static final String JSON_PROPERTY_FOUND = "found";

    @Nullable
    private String found;

    public static final String JSON_PROPERTY_EXPECTED = "expected";

    @Nullable
    private String expected;

    public DnsVerificationErrorIncorrectRecordsInner() {
    }

    public DnsVerificationErrorIncorrectRecordsInner record(@Nullable DnsRecord record) {
        this.record = record;
        return this;
    }

    /**
     * Get record
     * @return record
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_RECORD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public DnsRecord getRecord() {
        return record;
    }

    @JsonProperty(value = JSON_PROPERTY_RECORD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRecord(@Nullable DnsRecord record) {
        this.record = record;
    }

    public DnsVerificationErrorIncorrectRecordsInner found(@Nullable String found) {
        this.found = found;
        return this;
    }

    /**
     * The live record value observed when a record exists but does not match the required value.
     * @return found
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_FOUND, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getFound() {
        return found;
    }

    @JsonProperty(value = JSON_PROPERTY_FOUND, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setFound(@Nullable String found) {
        this.found = found;
    }

    public DnsVerificationErrorIncorrectRecordsInner expected(@Nullable String expected) {
        this.expected = expected;
        return this;
    }

    /**
     * The required value; equals record.value.
     * @return expected
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_EXPECTED, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getExpected() {
        return expected;
    }

    @JsonProperty(value = JSON_PROPERTY_EXPECTED, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setExpected(@Nullable String expected) {
        this.expected = expected;
    }

    /**
     * Return true if this DnsVerificationError_incorrectRecords_inner object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DnsVerificationErrorIncorrectRecordsInner dnsVerificationErrorIncorrectRecordsInner =
                (DnsVerificationErrorIncorrectRecordsInner) o;
        return Objects.equals(this.record, dnsVerificationErrorIncorrectRecordsInner.record) &&
                Objects.equals(this.found, dnsVerificationErrorIncorrectRecordsInner.found) &&
                Objects.equals(this.expected, dnsVerificationErrorIncorrectRecordsInner.expected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(record, found, expected);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DnsVerificationErrorIncorrectRecordsInner {\n");
        sb.append("    record: ").append(toIndentedString(record)).append("\n");
        sb.append("    found: ").append(toIndentedString(found)).append("\n");
        sb.append("    expected: ").append(toIndentedString(expected)).append("\n");
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
