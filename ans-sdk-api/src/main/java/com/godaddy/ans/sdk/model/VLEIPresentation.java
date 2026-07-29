package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * The lei (vLEI) register-time credential presentation. REQUIRED for the &#x60;lei&#x60; kind and omitted for the JWS
 * kinds (&#x60;did:web&#x60;, &#x60;did:key&#x60;). The RA submits the CESR to its configured vlei-verifier, which
 * derives and pins the subject AID; the 202&#39;s &#x60;presentationStatus&#x60; reports the verifier&#39;s advisory
 * authorization decision.
 */
@JsonPropertyOrder({
    VLEIPresentation.JSON_PROPERTY_CESR
})
public class VLEIPresentation {
    public static final String JSON_PROPERTY_CESR = "cesr";

    @Nonnull
    private String cesr;

    public VLEIPresentation() {
    }

    public VLEIPresentation cesr(@Nonnull String cesr) {
        this.cesr = cesr;
        return this;
    }

    /**
     * The full-chain CESR export of the vLEI credential and its supporting KELs/ACDCs (the &#x60;credentials()
     * .get(said, true)&#x60; shape). The RA never parses KERI key state itself — the verifier is the authoritative
     * key-state oracle.
     * @return cesr
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CESR, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCesr() {
        return cesr;
    }

    @JsonProperty(value = JSON_PROPERTY_CESR, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCesr(@Nonnull String cesr) {
        this.cesr = cesr;
    }

    /**
     * Return true if this VLEIPresentation object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VLEIPresentation vlEIPresentation = (VLEIPresentation) o;
        return Objects.equals(this.cesr, vlEIPresentation.cesr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cesr);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class VLEIPresentation {\n");
        sb.append("    cesr: ").append(toIndentedString(cesr)).append("\n");
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
