package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * ChallengeInfo
 */
@JsonPropertyOrder({
    ChallengeInfo.JSON_PROPERTY_TYPE,
    ChallengeInfo.JSON_PROPERTY_TOKEN,
    ChallengeInfo.JSON_PROPERTY_KEY_AUTHORIZATION,
    ChallengeInfo.JSON_PROPERTY_DNS_RECORD,
    ChallengeInfo.JSON_PROPERTY_HTTP_PATH,
    ChallengeInfo.JSON_PROPERTY_EXPIRES_AT
})
public class ChallengeInfo {
    /**
     * Gets or Sets type
     */
    public enum TypeEnum {
        DNS_01("DNS_01"),

        HTTP_01("HTTP_01");

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

    @Nullable
    private TypeEnum type;

    public static final String JSON_PROPERTY_TOKEN = "token";

    @Nullable
    private String token;

    public static final String JSON_PROPERTY_KEY_AUTHORIZATION = "keyAuthorization";

    @Nullable
    private String keyAuthorization;

    public static final String JSON_PROPERTY_DNS_RECORD = "dnsRecord";

    @Nullable
    private ChallengeInfoDnsRecord dnsRecord;

    public static final String JSON_PROPERTY_HTTP_PATH = "httpPath";

    @Nullable
    private String httpPath;

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";

    @Nullable
    private OffsetDateTime expiresAt;

    public ChallengeInfo() {
    }

    public ChallengeInfo type(@Nullable TypeEnum type) {
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
    public TypeEnum getType() {
        return type;
    }

    @JsonProperty(value = JSON_PROPERTY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setType(@Nullable TypeEnum type) {
        this.type = type;
    }

    public ChallengeInfo token(@Nullable String token) {
        this.token = token;
        return this;
    }

    /**
     * Get token
     * @return token
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TOKEN, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getToken() {
        return token;
    }

    @JsonProperty(value = JSON_PROPERTY_TOKEN, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setToken(@Nullable String token) {
        this.token = token;
    }

    public ChallengeInfo keyAuthorization(@Nullable String keyAuthorization) {
        this.keyAuthorization = keyAuthorization;
        return this;
    }

    /**
     * Get keyAuthorization
     * @return keyAuthorization
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_KEY_AUTHORIZATION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getKeyAuthorization() {
        return keyAuthorization;
    }

    @JsonProperty(value = JSON_PROPERTY_KEY_AUTHORIZATION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setKeyAuthorization(@Nullable String keyAuthorization) {
        this.keyAuthorization = keyAuthorization;
    }

    public ChallengeInfo dnsRecord(@Nullable ChallengeInfoDnsRecord dnsRecord) {
        this.dnsRecord = dnsRecord;
        return this;
    }

    /**
     * Get dnsRecord
     * @return dnsRecord
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DNS_RECORD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public ChallengeInfoDnsRecord getDnsRecord() {
        return dnsRecord;
    }

    @JsonProperty(value = JSON_PROPERTY_DNS_RECORD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDnsRecord(@Nullable ChallengeInfoDnsRecord dnsRecord) {
        this.dnsRecord = dnsRecord;
    }

    public ChallengeInfo httpPath(@Nullable String httpPath) {
        this.httpPath = httpPath;
        return this;
    }

    /**
     * Get httpPath
     * @return httpPath
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_HTTP_PATH, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getHttpPath() {
        return httpPath;
    }

    @JsonProperty(value = JSON_PROPERTY_HTTP_PATH, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setHttpPath(@Nullable String httpPath) {
        this.httpPath = httpPath;
    }

    public ChallengeInfo expiresAt(@Nullable OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    /**
     * Get expiresAt
     * @return expiresAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_EXPIRES_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    @JsonProperty(value = JSON_PROPERTY_EXPIRES_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Return true if this ChallengeInfo object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChallengeInfo challengeInfo = (ChallengeInfo) o;
        return Objects.equals(this.type, challengeInfo.type) &&
                Objects.equals(this.token, challengeInfo.token) &&
                Objects.equals(this.keyAuthorization, challengeInfo.keyAuthorization) &&
                Objects.equals(this.dnsRecord, challengeInfo.dnsRecord) &&
                Objects.equals(this.httpPath, challengeInfo.httpPath) &&
                Objects.equals(this.expiresAt, challengeInfo.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, token, keyAuthorization, dnsRecord, httpPath, expiresAt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ChallengeInfo {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    token: ").append(toIndentedString(token)).append("\n");
        sb.append("    keyAuthorization: ").append(toIndentedString(keyAuthorization)).append("\n");
        sb.append("    dnsRecord: ").append(toIndentedString(dnsRecord)).append("\n");
        sb.append("    httpPath: ").append(toIndentedString(httpPath)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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
