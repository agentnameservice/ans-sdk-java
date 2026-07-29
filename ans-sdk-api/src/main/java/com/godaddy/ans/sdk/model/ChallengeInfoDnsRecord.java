package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * ChallengeInfoDnsRecord
 */
@JsonPropertyOrder({
    ChallengeInfoDnsRecord.JSON_PROPERTY_NAME,
    ChallengeInfoDnsRecord.JSON_PROPERTY_TYPE,
    ChallengeInfoDnsRecord.JSON_PROPERTY_VALUE
})
public class ChallengeInfoDnsRecord {
    public static final String JSON_PROPERTY_NAME = "name";

    @Nullable
    private String name;

    public static final String JSON_PROPERTY_TYPE = "type";

    @Nullable
    private String type;

    public static final String JSON_PROPERTY_VALUE = "value";

    @Nullable
    private String value;

    public ChallengeInfoDnsRecord() {
    }

    public ChallengeInfoDnsRecord name(@Nullable String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_NAME, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getName() {
        return name;
    }

    @JsonProperty(value = JSON_PROPERTY_NAME, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setName(@Nullable String name) {
        this.name = name;
    }

    public ChallengeInfoDnsRecord type(@Nullable String type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getType() {
        return type;
    }

    @JsonProperty(value = JSON_PROPERTY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setType(@Nullable String type) {
        this.type = type;
    }

    public ChallengeInfoDnsRecord value(@Nullable String value) {
        this.value = value;
        return this;
    }

    /**
     * Get value
     * @return value
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_VALUE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getValue() {
        return value;
    }

    @JsonProperty(value = JSON_PROPERTY_VALUE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setValue(@Nullable String value) {
        this.value = value;
    }

    /**
     * Return true if this ChallengeInfo_dnsRecord object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeInfoDnsRecord challengeInfoDnsRecord = (ChallengeInfoDnsRecord) o;
        return Objects.equals(this.name, challengeInfoDnsRecord.name) &&
                Objects.equals(this.type, challengeInfoDnsRecord.type) &&
                Objects.equals(this.value, challengeInfoDnsRecord.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChallengeInfoDnsRecord {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
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
