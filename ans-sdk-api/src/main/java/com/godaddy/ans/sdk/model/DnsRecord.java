package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * DnsRecord
 */
@JsonPropertyOrder({
    DnsRecord.JSON_PROPERTY_NAME,
    DnsRecord.JSON_PROPERTY_TYPE,
    DnsRecord.JSON_PROPERTY_VALUE,
    DnsRecord.JSON_PROPERTY_PRIORITY,
    DnsRecord.JSON_PROPERTY_TTL,
    DnsRecord.JSON_PROPERTY_PURPOSE,
    DnsRecord.JSON_PROPERTY_REQUIRED
})
public class DnsRecord {
    public static final String JSON_PROPERTY_NAME = "name";


    @Nonnull
    private String name;

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        HTTPS("HTTPS"),

        SVCB("SVCB"),

        TLSA("TLSA"),

        TXT("TXT");

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

    public static final String JSON_PROPERTY_VALUE = "value";


    @Nonnull
    private String value;

    public static final String JSON_PROPERTY_PRIORITY = "priority";

    @Nullable
    private Integer priority;

    public static final String JSON_PROPERTY_TTL = "ttl";

    @Nullable
    private Integer ttl = 3600;

    /**
     * Gets or Sets purpose
     */
    public enum PurposeEnum {
        DISCOVERY("DISCOVERY"),

        TRUST("TRUST"),

        CERTIFICATE_BINDING("CERTIFICATE_BINDING"),

        BADGE("BADGE");

        private String value;

        PurposeEnum(String value) {
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
        public static PurposeEnum fromValue(String value) {
            for (PurposeEnum b : PurposeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_PURPOSE = "purpose";

    @Nullable
    private PurposeEnum purpose;

    public static final String JSON_PROPERTY_REQUIRED = "required";

    @Nullable
    private Boolean required = true;

    public DnsRecord() {
    }

    public DnsRecord name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getName() {
        return name;
    }

    @JsonProperty(value = JSON_PROPERTY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public DnsRecord type(@Nonnull TypeEnum type) {
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

    public DnsRecord value(@Nonnull String value) {
        this.value = value;
        return this;
    }

    /**
     * Get value
     * @return value
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_VALUE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getValue() {
        return value;
    }

    @JsonProperty(value = JSON_PROPERTY_VALUE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setValue(@Nonnull String value) {
        this.value = value;
    }

    public DnsRecord priority(@Nullable Integer priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Get priority
     * @return priority
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PRIORITY, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Integer getPriority() {
        return priority;
    }

    @JsonProperty(value = JSON_PROPERTY_PRIORITY, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPriority(@Nullable Integer priority) {
        this.priority = priority;
    }

    public DnsRecord ttl(@Nullable Integer ttl) {
        this.ttl = ttl;
        return this;
    }

    /**
     * Get ttl
     * @return ttl
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TTL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Integer getTtl() {
        return ttl;
    }

    @JsonProperty(value = JSON_PROPERTY_TTL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTtl(@Nullable Integer ttl) {
        this.ttl = ttl;
    }

    public DnsRecord purpose(@Nullable PurposeEnum purpose) {
        this.purpose = purpose;
        return this;
    }

    /**
     * Get purpose
     * @return purpose
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PURPOSE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public PurposeEnum getPurpose() {
        return purpose;
    }

    @JsonProperty(value = JSON_PROPERTY_PURPOSE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPurpose(@Nullable PurposeEnum purpose) {
        this.purpose = purpose;
    }

    public DnsRecord required(@Nullable Boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Get required
     * @return required
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_REQUIRED, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public Boolean getRequired() {
        return required;
    }

    @JsonProperty(value = JSON_PROPERTY_REQUIRED, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setRequired(@Nullable Boolean required) {
        this.required = required;
    }

    /**
     * Return true if this DnsRecord object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DnsRecord dnsRecord = (DnsRecord) o;
        return Objects.equals(this.name, dnsRecord.name) &&
                Objects.equals(this.type, dnsRecord.type) &&
                Objects.equals(this.value, dnsRecord.value) &&
                Objects.equals(this.priority, dnsRecord.priority) &&
                Objects.equals(this.ttl, dnsRecord.ttl) &&
                Objects.equals(this.purpose, dnsRecord.purpose) &&
                Objects.equals(this.required, dnsRecord.required);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value, priority, ttl, purpose, required);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DnsRecord {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
        sb.append("    ttl: ").append(toIndentedString(ttl)).append("\n");
        sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
        sb.append("    required: ").append(toIndentedString(required)).append("\n");
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
