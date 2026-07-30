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
 * A host-complete AI Catalog document (AI Catalog §4.2), served as application/ai-catalog+json. The file an AHP
 * publishes at &#x60;/.well-known/ai-catalog.json&#x60;; &#x60;entries&#x60; carries one CatalogEntry per ACTIVE,
 * catalog-eligible agent on the host (sorted by identifier then version for a stable ETag).
 */
@JsonPropertyOrder({
    CatalogDocument.JSON_PROPERTY_SPEC_VERSION,
    CatalogDocument.JSON_PROPERTY_HOST,
    CatalogDocument.JSON_PROPERTY_ENTRIES
})
public class CatalogDocument {
    public static final String JSON_PROPERTY_SPEC_VERSION = "specVersion";

    @Nonnull
    private String specVersion;

    public static final String JSON_PROPERTY_HOST = "host";

    @Nullable
    private CatalogHostInfo host;

    public static final String JSON_PROPERTY_ENTRIES = "entries";

    @Nonnull
    private List<CatalogEntry> entries = new ArrayList<>();

    public CatalogDocument() {
    }

    public CatalogDocument specVersion(@Nonnull String specVersion) {
        this.specVersion = specVersion;
        return this;
    }

    /**
     * Get specVersion
     * @return specVersion
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_SPEC_VERSION, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getSpecVersion() {
        return specVersion;
    }

    @JsonProperty(value = JSON_PROPERTY_SPEC_VERSION, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setSpecVersion(@Nonnull String specVersion) {
        this.specVersion = specVersion;
    }

    public CatalogDocument host(@Nullable CatalogHostInfo host) {
        this.host = host;
        return this;
    }

    /**
     * Get host
     * @return host
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_HOST, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public CatalogHostInfo getHost() {
        return host;
    }

    @JsonProperty(value = JSON_PROPERTY_HOST, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setHost(@Nullable CatalogHostInfo host) {
        this.host = host;
    }

    public CatalogDocument entries(@Nonnull List<CatalogEntry> entries) {
        this.entries = entries;
        return this;
    }

    public CatalogDocument addEntriesItem(CatalogEntry entriesItem) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(entriesItem);
        return this;
    }

    /**
     * Get entries
     * @return entries
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ENTRIES, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<CatalogEntry> getEntries() {
        return entries;
    }

    @JsonProperty(value = JSON_PROPERTY_ENTRIES, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setEntries(@Nonnull List<CatalogEntry> entries) {
        this.entries = entries;
    }

    /**
     * Return true if this CatalogDocument object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogDocument catalogDocument = (CatalogDocument) o;
        return Objects.equals(this.specVersion, catalogDocument.specVersion) &&
                Objects.equals(this.host, catalogDocument.host) &&
                Objects.equals(this.entries, catalogDocument.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specVersion, host, entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogDocument {\n");
        sb.append("    specVersion: ").append(toIndentedString(specVersion)).append("\n");
        sb.append("    host: ").append(toIndentedString(host)).append("\n");
        sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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
