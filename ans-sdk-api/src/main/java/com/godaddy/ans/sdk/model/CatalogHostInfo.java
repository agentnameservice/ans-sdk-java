package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Host Info object (AI Catalog §4.3) identifying the operator of a catalog document. &#x60;displayName&#x60; is
 * required within Host Info; for a per-host document both fields are the agentHost.
 */
@JsonPropertyOrder({
    CatalogHostInfo.JSON_PROPERTY_IDENTIFIER,
    CatalogHostInfo.JSON_PROPERTY_DISPLAY_NAME
})
public class CatalogHostInfo {
    public static final String JSON_PROPERTY_IDENTIFIER = "identifier";

    @Nullable
    private String identifier;

    public static final String JSON_PROPERTY_DISPLAY_NAME = "displayName";

    @Nonnull
    private String displayName;

    public CatalogHostInfo() {
    }

    public CatalogHostInfo identifier(@Nullable String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Get identifier
     * @return identifier
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_IDENTIFIER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getIdentifier() {
        return identifier;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTIFIER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentifier(@Nullable String identifier) {
        this.identifier = identifier;
    }

    public CatalogHostInfo displayName(@Nonnull String displayName) {
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

    /**
     * Return true if this CatalogHostInfo object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogHostInfo catalogHostInfo = (CatalogHostInfo) o;
        return Objects.equals(this.identifier, catalogHostInfo.identifier) &&
                Objects.equals(this.displayName, catalogHostInfo.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, displayName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogHostInfo {\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
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
