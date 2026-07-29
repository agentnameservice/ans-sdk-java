package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Verifiable identity + trust metadata (AI Catalog §5). &#x60;identity&#x60; MUST equal the containing entry&#39;s
 * &#x60;identifier&#x60;.
 */
@JsonPropertyOrder({
    CatalogTrustManifest.JSON_PROPERTY_IDENTITY,
    CatalogTrustManifest.JSON_PROPERTY_ATTESTATIONS
})
public class CatalogTrustManifest {
    public static final String JSON_PROPERTY_IDENTITY = "identity";

    @Nonnull
    private String identity;

    public static final String JSON_PROPERTY_ATTESTATIONS = "attestations";

    @Nullable
    private List<CatalogAttestation> attestations = new ArrayList<>();

    public CatalogTrustManifest() {
    }

    public CatalogTrustManifest identity(@Nonnull String identity) {
        this.identity = identity;
        return this;
    }

    /**
     * Get identity
     * @return identity
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_IDENTITY, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getIdentity() {
        return identity;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTITY, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentity(@Nonnull String identity) {
        this.identity = identity;
    }

    public CatalogTrustManifest attestations(@Nullable List<CatalogAttestation> attestations) {
        this.attestations = attestations;
        return this;
    }

    public CatalogTrustManifest addAttestationsItem(CatalogAttestation attestationsItem) {
        if (this.attestations == null) {
            this.attestations = new ArrayList<>();
        }
        this.attestations.add(attestationsItem);
        return this;
    }

    /**
     * Present only when a TL base URL is configured. The slice-1 attestation is the ANS-Registration SCITT receipt.
     * @return attestations
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_ATTESTATIONS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<CatalogAttestation> getAttestations() {
        return attestations;
    }

    @JsonProperty(value = JSON_PROPERTY_ATTESTATIONS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAttestations(@Nullable List<CatalogAttestation> attestations) {
        this.attestations = attestations;
    }

    /**
     * Return true if this CatalogTrustManifest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogTrustManifest catalogTrustManifest = (CatalogTrustManifest) o;
        return Objects.equals(this.identity, catalogTrustManifest.identity) &&
                Objects.equals(this.attestations, catalogTrustManifest.attestations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identity, attestations);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogTrustManifest {\n");
        sb.append("    identity: ").append(toIndentedString(identity)).append("\n");
        sb.append("    attestations: ").append(toIndentedString(attestations)).append("\n");
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
