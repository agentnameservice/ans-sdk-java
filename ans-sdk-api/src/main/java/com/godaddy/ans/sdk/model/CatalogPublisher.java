package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Publishing entity (AI Catalog §4.6). DNS-anchored in this RA; &#x60;identifier&#x60; and &#x60;displayName&#x60; are
 * both the agentHost.
 */
@JsonPropertyOrder({
    CatalogPublisher.JSON_PROPERTY_IDENTIFIER,
    CatalogPublisher.JSON_PROPERTY_DISPLAY_NAME,
    CatalogPublisher.JSON_PROPERTY_IDENTITY_TYPE
})
public class CatalogPublisher {
    public static final String JSON_PROPERTY_IDENTIFIER = "identifier";

    @Nonnull
    private String identifier;

    public static final String JSON_PROPERTY_DISPLAY_NAME = "displayName";

    @Nonnull
    private String displayName;

    public static final String JSON_PROPERTY_IDENTITY_TYPE = "identityType";

    @Nullable
    private String identityType;

    public CatalogPublisher() {
    }

    public CatalogPublisher identifier(@Nonnull String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Get identifier
     * @return identifier
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_IDENTIFIER, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTIFIER, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setIdentifier(@Nonnull String identifier) {
        this.identifier = identifier;
    }

    public CatalogPublisher displayName(@Nonnull String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Get displayName
     * @return displayName
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_DISPLAY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty(value = JSON_PROPERTY_DISPLAY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setDisplayName(@Nonnull String displayName) {
        this.displayName = displayName;
    }

    public CatalogPublisher identityType(@Nullable String identityType) {
        this.identityType = identityType;
        return this;
    }

    /**
     * Get identityType
     * @return identityType
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_IDENTITY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getIdentityType() {
        return identityType;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTITY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentityType(@Nullable String identityType) {
        this.identityType = identityType;
    }

    /**
     * Return true if this CatalogPublisher object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogPublisher catalogPublisher = (CatalogPublisher) o;
        return Objects.equals(this.identifier, catalogPublisher.identifier) &&
                Objects.equals(this.displayName, catalogPublisher.displayName) &&
                Objects.equals(this.identityType, catalogPublisher.identityType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, displayName, identityType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogPublisher {\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    identityType: ").append(toIndentedString(identityType)).append("\n");
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
