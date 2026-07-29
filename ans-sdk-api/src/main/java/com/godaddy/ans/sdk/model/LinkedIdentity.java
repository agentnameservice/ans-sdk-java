package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * One computed identities[] entry on AgentDetails.
 */
@JsonPropertyOrder({
    LinkedIdentity.JSON_PROPERTY_IDENTITY_ID,
    LinkedIdentity.JSON_PROPERTY_KIND,
    LinkedIdentity.JSON_PROPERTY_VALUE,
    LinkedIdentity.JSON_PROPERTY_IDENTITY_STATUS,
    LinkedIdentity.JSON_PROPERTY_LINKED_AT
})
public class LinkedIdentity {
    public static final String JSON_PROPERTY_IDENTITY_ID = "identityId";


    @Nonnull
    private String identityId;

    /**
     * Gets or Sets kind
     */
    public enum KindEnum {
        DID_WEB("did:web"),

        DID_KEY("did:key"),

        LEI("lei");

        private String value;

        KindEnum(String value) {
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
        public static KindEnum fromValue(String value) {
            for (KindEnum b : KindEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_KIND = "kind";


    @Nonnull
    private KindEnum kind;

    public static final String JSON_PROPERTY_VALUE = "value";


    @Nonnull
    private String value;

    /**
     * The identity&#39;s CURRENT status — reflects its stream now
     */
    public enum IdentityStatusEnum {
        VERIFIED("VERIFIED"),

        REVOKED("REVOKED");

        private String value;

        IdentityStatusEnum(String value) {
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
        public static IdentityStatusEnum fromValue(String value) {
            for (IdentityStatusEnum b : IdentityStatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_IDENTITY_STATUS = "identityStatus";


    @Nonnull
    private IdentityStatusEnum identityStatus;

    public static final String JSON_PROPERTY_LINKED_AT = "linkedAt";

    @Nullable
    private OffsetDateTime linkedAt;

    public LinkedIdentity() {
    }

    public LinkedIdentity identityId(@Nonnull String identityId) {
        this.identityId = identityId;
        return this;
    }

    /**
     * Get identityId
     * @return identityId
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_IDENTITY_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getIdentityId() {
        return identityId;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTITY_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentityId(@Nonnull String identityId) {
        this.identityId = identityId;
    }

    public LinkedIdentity kind(@Nonnull KindEnum kind) {
        this.kind = kind;
        return this;
    }

    /**
     * Get kind
     * @return kind
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_KIND, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public KindEnum getKind() {
        return kind;
    }

    @JsonProperty(value = JSON_PROPERTY_KIND, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setKind(@Nonnull KindEnum kind) {
        this.kind = kind;
    }

    public LinkedIdentity value(@Nonnull String value) {
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

    public LinkedIdentity identityStatus(@Nonnull IdentityStatusEnum identityStatus) {
        this.identityStatus = identityStatus;
        return this;
    }

    /**
     * The identity&#39;s CURRENT status — reflects its stream now
     * @return identityStatus
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_IDENTITY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public IdentityStatusEnum getIdentityStatus() {
        return identityStatus;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTITY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentityStatus(@Nonnull IdentityStatusEnum identityStatus) {
        this.identityStatus = identityStatus;
    }

    public LinkedIdentity linkedAt(@Nullable OffsetDateTime linkedAt) {
        this.linkedAt = linkedAt;
        return this;
    }

    /**
     * Get linkedAt
     * @return linkedAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LINKED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getLinkedAt() {
        return linkedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLinkedAt(@Nullable OffsetDateTime linkedAt) {
        this.linkedAt = linkedAt;
    }

    /**
     * Return true if this LinkedIdentity object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkedIdentity linkedIdentity = (LinkedIdentity) o;
        return Objects.equals(this.identityId, linkedIdentity.identityId) &&
                Objects.equals(this.kind, linkedIdentity.kind) &&
                Objects.equals(this.value, linkedIdentity.value) &&
                Objects.equals(this.identityStatus, linkedIdentity.identityStatus) &&
                Objects.equals(this.linkedAt, linkedIdentity.linkedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityId, kind, value, identityStatus, linkedAt);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class LinkedIdentity {\n");
        sb.append("    identityId: ").append(toIndentedString(identityId)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    identityStatus: ").append(toIndentedString(identityStatus)).append("\n");
        sb.append("    linkedAt: ").append(toIndentedString(linkedAt)).append("\n");
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
