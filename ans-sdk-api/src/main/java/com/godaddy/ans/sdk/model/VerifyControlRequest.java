package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * The control proof. Its members are additive per identifier kind, and EXACTLY ONE family is set per request:
 * - JWS kinds (&#x60;did:web&#x60;, &#x60;did:key&#x60;) submit &#x60;signedProofs&#x60; —     one compact JWS per
 * proven key.   - The &#x60;lei&#x60; kind submits &#x60;cesrSignature&#x60; — a single CESR     signature over the
 * served signingInput by the subject     AID&#39;s current key.  Supported JWS algorithms match what the verifier
 * implements: EdDSA (Ed25519), ES256 (ECDSA P-256), and RS256 (RSA &gt;&#x3D; 2048). Key-agreement keys (X25519) and
 * curves without a verifier (secp256k1, P-384/521) are rejected with a precise error.
 */
@JsonPropertyOrder({
    VerifyControlRequest.JSON_PROPERTY_SIGNED_PROOFS,
    VerifyControlRequest.JSON_PROPERTY_CESR_SIGNATURE
})
public class VerifyControlRequest {
    public static final String JSON_PROPERTY_SIGNED_PROOFS = "signedProofs";

    @Nullable
    private List<String> signedProofs = new ArrayList<>();

    public static final String JSON_PROPERTY_CESR_SIGNATURE = "cesrSignature";

    @Nullable
    private String cesrSignature;

    public VerifyControlRequest() {
    }

    public VerifyControlRequest signedProofs(@Nullable List<String> signedProofs) {
        this.signedProofs = signedProofs;
        return this;
    }

    public VerifyControlRequest addSignedProofsItem(String signedProofsItem) {
        if (this.signedProofs == null) {
            this.signedProofs = new ArrayList<>();
        }
        this.signedProofs.add(signedProofsItem);
        return this;
    }

    /**
     * Get signedProofs
     * @return signedProofs
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_SIGNED_PROOFS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getSignedProofs() {
        return signedProofs;
    }

    @JsonProperty(value = JSON_PROPERTY_SIGNED_PROOFS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setSignedProofs(@Nullable List<String> signedProofs) {
        this.signedProofs = signedProofs;
    }

    public VerifyControlRequest cesrSignature(@Nullable String cesrSignature) {
        this.cesrSignature = cesrSignature;
        return this;
    }

    /**
     * The lei proof: a single CESR signature over the served signingInput by the subject AID&#39;s current key. Set
     * only for the &#x60;lei&#x60; kind. The RA forwards it to the vlei-verifier, which resolves the AID&#39;s key
     * state from its KEL and checks the signature over the exact signingInput bytes (the same payload the JWS kinds
     * sign).
     * @return cesrSignature
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CESR_SIGNATURE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCesrSignature() {
        return cesrSignature;
    }

    @JsonProperty(value = JSON_PROPERTY_CESR_SIGNATURE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCesrSignature(@Nullable String cesrSignature) {
        this.cesrSignature = cesrSignature;
    }

    /**
     * Return true if this VerifyControlRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VerifyControlRequest verifyControlRequest = (VerifyControlRequest) o;
        return Objects.equals(this.signedProofs, verifyControlRequest.signedProofs) &&
                Objects.equals(this.cesrSignature, verifyControlRequest.cesrSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(signedProofs, cesrSignature);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class VerifyControlRequest {\n");
        sb.append("    signedProofs: ").append(toIndentedString(signedProofs)).append("\n");
        sb.append("    cesrSignature: ").append(toIndentedString(cesrSignature)).append("\n");
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
