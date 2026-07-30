package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Registers (POST) or rotates (PUT) an identifier. The kind is inferred from the value&#39;s lexical form —
 * &#x60;did:web:&#x60; prefix, &#x60;did:key:&#x60; prefix, or a 20-character LEI — never caller-asserted.
 */
@JsonPropertyOrder({
    IdentityRegistrationRequest.JSON_PROPERTY_VALUE,
    IdentityRegistrationRequest.JSON_PROPERTY_VLEI_PRESENTATION
})
public class IdentityRegistrationRequest {
    public static final String JSON_PROPERTY_VALUE = "value";

    @Nonnull
    private String value;

    public static final String JSON_PROPERTY_VLEI_PRESENTATION = "vleiPresentation";

    @Nullable
    private VLEIPresentation vleiPresentation;

    public IdentityRegistrationRequest() {
    }

    public IdentityRegistrationRequest value(@Nonnull String value) {
        this.value = value;
        return this;
    }

    /**
     * The identifier to prove control of
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

    public IdentityRegistrationRequest vleiPresentation(@Nullable VLEIPresentation vleiPresentation) {
        this.vleiPresentation = vleiPresentation;
        return this;
    }

    /**
     * Get vleiPresentation
     * @return vleiPresentation
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_VLEI_PRESENTATION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public VLEIPresentation getVleiPresentation() {
        return vleiPresentation;
    }

    @JsonProperty(value = JSON_PROPERTY_VLEI_PRESENTATION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setVleiPresentation(@Nullable VLEIPresentation vleiPresentation) {
        this.vleiPresentation = vleiPresentation;
    }

    /**
     * Return true if this IdentityRegistrationRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityRegistrationRequest identityRegistrationRequest = (IdentityRegistrationRequest) o;
        return Objects.equals(this.value, identityRegistrationRequest.value) &&
                Objects.equals(this.vleiPresentation, identityRegistrationRequest.vleiPresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, vleiPresentation);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityRegistrationRequest {\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    vleiPresentation: ").append(toIndentedString(vleiPresentation)).append("\n");
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
