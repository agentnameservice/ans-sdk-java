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
 * The 202 challenge round. Every entry shares the same anti-replay nonce and the same signingInput — the input is
 * key-independent; entries enumerate the keys the resolver could see in advance (a single unkeyed entry when it could
 * not — name keys via the JWS &#x60;kid&#x60; header at verify time).
 */
@JsonPropertyOrder({
    IdentityChallengeResponse.JSON_PROPERTY_IDENTITY_ID,
    IdentityChallengeResponse.JSON_PROPERTY_KIND,
    IdentityChallengeResponse.JSON_PROPERTY_VALUE,
    IdentityChallengeResponse.JSON_PROPERTY_STATUS,
    IdentityChallengeResponse.JSON_PROPERTY_NONCE,
    IdentityChallengeResponse.JSON_PROPERTY_EXPIRES_AT,
    IdentityChallengeResponse.JSON_PROPERTY_CHALLENGES,
    IdentityChallengeResponse.JSON_PROPERTY_PRESENTATION_STATUS
})
public class IdentityChallengeResponse {
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

    public static final String JSON_PROPERTY_NONCE = "nonce";


    @Nonnull
    private String nonce;

    public static final String JSON_PROPERTY_EXPIRES_AT = "expiresAt";


    @Nonnull
    private OffsetDateTime expiresAt;

    public static final String JSON_PROPERTY_CHALLENGES = "challenges";


    @Nonnull
    private List<IdentityProofChallenge> challenges = new ArrayList<>();

    /**
     * The lei register-time advisory authorization status from the vlei-verifier
     * (&#x60;AUTHORIZED&#x60; | &#x60;PENDING&#x60;). Omitted for kinds with no register-time presentation
     * (&#x60;did:web&#x60;, &#x60;did:key&#x60;). Advisory only — control is finally established by a LIVE
     * re-authorization at verify-control.
     */
    public enum PresentationStatusEnum {
        AUTHORIZED("AUTHORIZED"),

        PENDING("PENDING");

        private String value;

        PresentationStatusEnum(String value) {
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
        public static PresentationStatusEnum fromValue(String value) {
            for (PresentationStatusEnum b : PresentationStatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_PRESENTATION_STATUS = "presentationStatus";

    @Nullable
    private PresentationStatusEnum presentationStatus;

    public IdentityChallengeResponse() {
    }

    public IdentityChallengeResponse identityId(@Nonnull String identityId) {
        this.identityId = identityId;
        return this;
    }

    /**
     * RA-assigned UUIDv7 — the TL stream key
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

    public IdentityChallengeResponse kind(@Nonnull KindEnum kind) {
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

    public IdentityChallengeResponse value(@Nonnull String value) {
        this.value = value;
        return this;
    }

    /**
     * The canonical identifier this round proves
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

    public IdentityChallengeResponse status(@Nonnull IdentityLifecycleStatus status) {
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

    public IdentityChallengeResponse nonce(@Nonnull String nonce) {
        this.nonce = nonce;
        return this;
    }

    /**
     * Base64url 32-byte single-use anti-replay nonce
     * @return nonce
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_NONCE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getNonce() {
        return nonce;
    }

    @JsonProperty(value = JSON_PROPERTY_NONCE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setNonce(@Nonnull String nonce) {
        this.nonce = nonce;
    }

    public IdentityChallengeResponse expiresAt(@Nonnull OffsetDateTime expiresAt) {
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

    public IdentityChallengeResponse challenges(@Nonnull List<IdentityProofChallenge> challenges) {
        this.challenges = challenges;
        return this;
    }

    public IdentityChallengeResponse addChallengesItem(IdentityProofChallenge challengesItem) {
        if (this.challenges == null) {
            this.challenges = new ArrayList<>();
        }
        this.challenges.add(challengesItem);
        return this;
    }

    /**
     * Get challenges
     * @return challenges
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CHALLENGES, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<IdentityProofChallenge> getChallenges() {
        return challenges;
    }

    @JsonProperty(value = JSON_PROPERTY_CHALLENGES, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setChallenges(@Nonnull List<IdentityProofChallenge> challenges) {
        this.challenges = challenges;
    }

    public IdentityChallengeResponse presentationStatus(@Nullable PresentationStatusEnum presentationStatus) {
        this.presentationStatus = presentationStatus;
        return this;
    }

    /**
     * The lei register-time advisory authorization status from the vlei-verifier
     * (&#x60;AUTHORIZED&#x60; | &#x60;PENDING&#x60;). Omitted for kinds with no register-time presentation
     * (&#x60;did:web&#x60;, &#x60;did:key&#x60;). Advisory only — control is finally established by a LIVE
     * re-authorization at verify-control.
     * @return presentationStatus
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PRESENTATION_STATUS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public PresentationStatusEnum getPresentationStatus() {
        return presentationStatus;
    }

    @JsonProperty(value = JSON_PROPERTY_PRESENTATION_STATUS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPresentationStatus(@Nullable PresentationStatusEnum presentationStatus) {
        this.presentationStatus = presentationStatus;
    }

    /**
     * Return true if this IdentityChallengeResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityChallengeResponse identityChallengeResponse = (IdentityChallengeResponse) o;
        return Objects.equals(this.identityId, identityChallengeResponse.identityId) &&
                Objects.equals(this.kind, identityChallengeResponse.kind) &&
                Objects.equals(this.value, identityChallengeResponse.value) &&
                Objects.equals(this.status, identityChallengeResponse.status) &&
                Objects.equals(this.nonce, identityChallengeResponse.nonce) &&
                Objects.equals(this.expiresAt, identityChallengeResponse.expiresAt) &&
                Objects.equals(this.challenges, identityChallengeResponse.challenges) &&
                Objects.equals(this.presentationStatus, identityChallengeResponse.presentationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityId, kind, value, status, nonce, expiresAt, challenges, presentationStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityChallengeResponse {\n");
        sb.append("    identityId: ").append(toIndentedString(identityId)).append("\n");
        sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    nonce: ").append(toIndentedString(nonce)).append("\n");
        sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
        sb.append("    challenges: ").append(toIndentedString(challenges)).append("\n");
        sb.append("    presentationStatus: ").append(toIndentedString(presentationStatus)).append("\n");
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
