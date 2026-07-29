package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * IdentityProofChallenge
 */
@JsonPropertyOrder({
    IdentityProofChallenge.JSON_PROPERTY_KID,
    IdentityProofChallenge.JSON_PROPERTY_SIGNING_INPUT
})
public class IdentityProofChallenge {
    public static final String JSON_PROPERTY_KID = "kid";

    @Nullable
    private String kid;

    public static final String JSON_PROPERTY_SIGNING_INPUT = "signingInput";

    @Nonnull
    private String signingInput;

    public IdentityProofChallenge() {
    }

    public IdentityProofChallenge kid(@Nullable String kid) {
        this.kid = kid;
        return this;
    }

    /**
     * Verification-method id eligible to sign this round (omitted when the resolver could not enumerate keys)
     * @return kid
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_KID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getKid() {
        return kid;
    }

    @JsonProperty(value = JSON_PROPERTY_KID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setKid(@Nullable String kid) {
        this.kid = kid;
    }

    public IdentityProofChallenge signingInput(@Nonnull String signingInput) {
        this.signingInput = signingInput;
        return this;
    }

    /**
     * Base64url of the exact RFC 8785 (JCS) canonical IdentityProofInput bytes — {identifier, identityId, nonce,
     * purpose:\&quot;ans:identity-proof:v1\&quot;, raId, scheme}. A compact JWS&#39;s payload segment MUST equal this
     * string verbatim; clients never canonicalize.
     * @return signingInput
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_SIGNING_INPUT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getSigningInput() {
        return signingInput;
    }

    @JsonProperty(value = JSON_PROPERTY_SIGNING_INPUT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setSigningInput(@Nonnull String signingInput) {
        this.signingInput = signingInput;
    }

    /**
     * Return true if this IdentityProofChallenge object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityProofChallenge identityProofChallenge = (IdentityProofChallenge) o;
        return Objects.equals(this.kid, identityProofChallenge.kid) &&
                Objects.equals(this.signingInput, identityProofChallenge.signingInput);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kid, signingInput);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityProofChallenge {\n");
        sb.append("    kid: ").append(toIndentedString(kid)).append("\n");
        sb.append("    signingInput: ").append(toIndentedString(signingInput)).append("\n");
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
