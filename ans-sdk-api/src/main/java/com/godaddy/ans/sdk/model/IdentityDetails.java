package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * IdentityDetails
 */
@JsonPropertyOrder({
    IdentityDetails.JSON_PROPERTY_IDENTITY_ID,
    IdentityDetails.JSON_PROPERTY_KIND,
    IdentityDetails.JSON_PROPERTY_VALUE,
    IdentityDetails.JSON_PROPERTY_STATUS,
    IdentityDetails.JSON_PROPERTY_PROOF_METHOD,
    IdentityDetails.JSON_PROPERTY_PENDING_VALUE,
    IdentityDetails.JSON_PROPERTY_VERIFIED_AT,
    IdentityDetails.JSON_PROPERTY_CREATED_AT,
    IdentityDetails.JSON_PROPERTY_LINKED_AGENTS
})
public class IdentityDetails {
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

    public static final String JSON_PROPERTY_STATUS = "status";


    @Nonnull
    private IdentityLifecycleStatus status;

    /**
     * Control proof that verified this identity
     */
    public enum ProofMethodEnum {
        DID_WEB_SIG("did-web-sig"),

        DID_KEY_SIG("did-key-sig"),

        LEI_VLEI_ACDC("lei-vlei-acdc");

        private String value;

        ProofMethodEnum(String value) {
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
        public static ProofMethodEnum fromValue(String value) {
            for (ProofMethodEnum b : ProofMethodEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_PROOF_METHOD = "proofMethod";

    @Nullable
    private ProofMethodEnum proofMethod;

    public static final String JSON_PROPERTY_PENDING_VALUE = "pendingValue";

    @Nullable
    private String pendingValue;

    public static final String JSON_PROPERTY_VERIFIED_AT = "verifiedAt";

    @Nullable
    private OffsetDateTime verifiedAt;

    public static final String JSON_PROPERTY_CREATED_AT = "createdAt";


    @Nonnull
    private OffsetDateTime createdAt;

    public static final String JSON_PROPERTY_LINKED_AGENTS = "linkedAgents";

    @Nullable
    private List<IdentityDetailsLinkedAgentsInner> linkedAgents = new ArrayList<>();

    public IdentityDetails() {
    }

    public IdentityDetails identityId(@Nonnull String identityId) {
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

    public IdentityDetails kind(@Nonnull KindEnum kind) {
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

    public IdentityDetails value(@Nonnull String value) {
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

    public IdentityDetails status(@Nonnull IdentityLifecycleStatus status) {
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
    public IdentityLifecycleStatus getStatus() {
        return status;
    }

    @JsonProperty(value = JSON_PROPERTY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setStatus(@Nonnull IdentityLifecycleStatus status) {
        this.status = status;
    }

    public IdentityDetails proofMethod(@Nullable ProofMethodEnum proofMethod) {
        this.proofMethod = proofMethod;
        return this;
    }

    /**
     * Control proof that verified this identity
     * @return proofMethod
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PROOF_METHOD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public ProofMethodEnum getProofMethod() {
        return proofMethod;
    }

    @JsonProperty(value = JSON_PROPERTY_PROOF_METHOD, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setProofMethod(@Nullable ProofMethodEnum proofMethod) {
        this.proofMethod = proofMethod;
    }

    public IdentityDetails pendingValue(@Nullable String pendingValue) {
        this.pendingValue = pendingValue;
        return this;
    }

    /**
     * Staged rotation replacement (empty unless rotating)
     * @return pendingValue
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PENDING_VALUE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getPendingValue() {
        return pendingValue;
    }

    @JsonProperty(value = JSON_PROPERTY_PENDING_VALUE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPendingValue(@Nullable String pendingValue) {
        this.pendingValue = pendingValue;
    }

    public IdentityDetails verifiedAt(@Nullable OffsetDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
        return this;
    }

    /**
     * Get verifiedAt
     * @return verifiedAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_VERIFIED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getVerifiedAt() {
        return verifiedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_VERIFIED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setVerifiedAt(@Nullable OffsetDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public IdentityDetails createdAt(@Nonnull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Get createdAt
     * @return createdAt
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CREATED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty(value = JSON_PROPERTY_CREATED_AT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCreatedAt(@Nonnull OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public IdentityDetails linkedAgents(@Nullable List<IdentityDetailsLinkedAgentsInner> linkedAgents) {
        this.linkedAgents = linkedAgents;
        return this;
    }

    public IdentityDetails addLinkedAgentsItem(IdentityDetailsLinkedAgentsInner linkedAgentsItem) {
        if (this.linkedAgents == null) {
            this.linkedAgents = new ArrayList<>();
        }
        this.linkedAgents.add(linkedAgentsItem);
        return this;
    }

    /**
     * Live links (detail responses only)
     * @return linkedAgents
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LINKED_AGENTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<IdentityDetailsLinkedAgentsInner> getLinkedAgents() {
        return linkedAgents;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKED_AGENTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLinkedAgents(@Nullable List<IdentityDetailsLinkedAgentsInner> linkedAgents) {
        this.linkedAgents = linkedAgents;
    }

    /**
     * Return true if this IdentityDetails object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityDetails identityDetails = (IdentityDetails) o;
        return Objects.equals(this.identityId, identityDetails.identityId) &&
                Objects.equals(this.kind, identityDetails.kind) &&
                Objects.equals(this.value, identityDetails.value) &&
                Objects.equals(this.status, identityDetails.status) &&
                Objects.equals(this.proofMethod, identityDetails.proofMethod) &&
                Objects.equals(this.pendingValue, identityDetails.pendingValue) &&
                Objects.equals(this.verifiedAt, identityDetails.verifiedAt) &&
                Objects.equals(this.createdAt, identityDetails.createdAt) &&
                Objects.equals(this.linkedAgents, identityDetails.linkedAgents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityId, kind, value, status, proofMethod, pendingValue, verifiedAt, createdAt,
                linkedAgents);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityDetails {\n");
        sb.append("    identityId: ").append(toIndentedString(identityId)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    proofMethod: ").append(toIndentedString(proofMethod)).append("\n");
        sb.append("    pendingValue: ").append(toIndentedString(pendingValue)).append("\n");
        sb.append("    verifiedAt: ").append(toIndentedString(verifiedAt)).append("\n");
        sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
        sb.append("    linkedAgents: ").append(toIndentedString(linkedAgents)).append("\n");
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
