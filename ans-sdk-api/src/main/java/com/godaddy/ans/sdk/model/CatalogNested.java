package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * Inline nested catalog held in a multi-protocol entry&#39;s &#x60;data&#x60; (AI Catalog §6.1 / IMPL §3.5).
 */
@JsonPropertyOrder({
    CatalogNested.JSON_PROPERTY_SPEC_VERSION,
    CatalogNested.JSON_PROPERTY_ENTRIES
})
public class CatalogNested {
    public static final String JSON_PROPERTY_SPEC_VERSION = "specVersion";

    @Nonnull
    private String specVersion;

    public static final String JSON_PROPERTY_ENTRIES = "entries";

    @Nonnull
    private List<CatalogEntry> entries = new ArrayList<>();

    public CatalogNested() {
    }

    public CatalogNested specVersion(@Nonnull String specVersion) {
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

    public CatalogNested entries(@Nonnull List<CatalogEntry> entries) {
        this.entries = entries;
        return this;
    }

    public CatalogNested addEntriesItem(CatalogEntry entriesItem) {
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
     * Return true if this CatalogNested object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogNested catalogNested = (CatalogNested) o;
        return Objects.equals(this.specVersion, catalogNested.specVersion) &&
                Objects.equals(this.entries, catalogNested.entries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specVersion, entries);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogNested {\n");
        sb.append("    specVersion: ").append(toIndentedString(specVersion)).append("\n");
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
