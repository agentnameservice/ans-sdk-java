package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * One artifact&#39;s AI Catalog record, derived from a registration (AI Catalog draft 2026-06-11 §4.4). A bare entry is
 * served as application/json — it is not itself a catalog document and carries no &#x60;specVersion&#x60;. Exactly one
 * of &#x60;url&#x60; or &#x60;data&#x60; is present: a single-protocol entry carries &#x60;url&#x60;; a multi-protocol
 * agent carries &#x60;data&#x60; (an inline nested catalog of per-protocol children) with &#x60;mediaType&#x60;
 * &#x60;application/ai-catalog+json&#x60;.
 */
@JsonPropertyOrder({
    CatalogEntry.JSON_PROPERTY_IDENTIFIER,
    CatalogEntry.JSON_PROPERTY_DISPLAY_NAME,
    CatalogEntry.JSON_PROPERTY_DESCRIPTION,
    CatalogEntry.JSON_PROPERTY_VERSION,
    CatalogEntry.JSON_PROPERTY_MEDIA_TYPE,
    CatalogEntry.JSON_PROPERTY_URL,
    CatalogEntry.JSON_PROPERTY_DATA,
    CatalogEntry.JSON_PROPERTY_TAGS,
    CatalogEntry.JSON_PROPERTY_UPDATED_AT,
    CatalogEntry.JSON_PROPERTY_PUBLISHER,
    CatalogEntry.JSON_PROPERTY_METADATA,
    CatalogEntry.JSON_PROPERTY_TRUST_MANIFEST
})
public class CatalogEntry {
    public static final String JSON_PROPERTY_IDENTIFIER = "identifier";

    @Nonnull
    private String identifier;

    public static final String JSON_PROPERTY_DISPLAY_NAME = "displayName";

    @Nonnull
    private String displayName;

    public static final String JSON_PROPERTY_DESCRIPTION = "description";

    @Nullable
    private String description;

    public static final String JSON_PROPERTY_VERSION = "version";

    @Nullable
    private String version;

    public static final String JSON_PROPERTY_MEDIA_TYPE = "mediaType";

    @Nonnull
    private String mediaType;

    public static final String JSON_PROPERTY_URL = "url";

    @Nullable
    private URI url;

    public static final String JSON_PROPERTY_DATA = "data";

    @Nullable
    private CatalogNested data;

    public static final String JSON_PROPERTY_TAGS = "tags";

    @Nullable
    private List<String> tags = new ArrayList<>();

    public static final String JSON_PROPERTY_UPDATED_AT = "updatedAt";

    @Nullable
    private OffsetDateTime updatedAt;

    public static final String JSON_PROPERTY_PUBLISHER = "publisher";

    @Nullable
    private CatalogPublisher publisher;

    public static final String JSON_PROPERTY_METADATA = "metadata";

    @Nullable
    private CatalogEntryMetadata metadata;

    public static final String JSON_PROPERTY_TRUST_MANIFEST = "trustManifest";

    @Nullable
    private CatalogTrustManifest trustManifest;

    public CatalogEntry() {
    }

    public CatalogEntry identifier(@Nonnull String identifier) {
        this.identifier = identifier;
        return this;
    }

    /**
     * Stable, version-spanning lineage handle &#x60;urn:air:{agentHost}:agents:{label}&#x60;, where &#x60;{label}&#x60;
     * is the agent&#39;s display name with whitespace runs collapsed to single hyphens — the same derivation the ARD
     * discovery service (the Finder) mints from feed events, so search results and the published catalog carry one
     * identifier per agent. Never the per-version agentId.
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

    public CatalogEntry displayName(@Nonnull String displayName) {
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

    public CatalogEntry description(@Nullable String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     * @return description
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getDescription() {
        return description;
    }

    @JsonProperty(value = JSON_PROPERTY_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public CatalogEntry version(@Nullable String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     * @return version
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_VERSION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getVersion() {
        return version;
    }

    @JsonProperty(value = JSON_PROPERTY_VERSION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    public CatalogEntry mediaType(@Nonnull String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    /**
     * Artifact discriminator: &#x60;application/a2a-agent-card+json&#x60;,
     * &#x60;application/mcp-server-card+json&#x60;, or &#x60;application/ai-catalog+json&#x60; for a multi-protocol
     * nested entry.
     * @return mediaType
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_MEDIA_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getMediaType() {
        return mediaType;
    }

    @JsonProperty(value = JSON_PROPERTY_MEDIA_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setMediaType(@Nonnull String mediaType) {
        this.mediaType = mediaType;
    }

    public CatalogEntry url(@Nullable URI url) {
        this.url = url;
        return this;
    }

    /**
     * The endpoint&#39;s metaDataUrl (the protocol card location). Present on a single-protocol or nested-child entry.
     * Mutually exclusive with &#x60;data&#x60;.
     * @return url
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public URI getUrl() {
        return url;
    }

    @JsonProperty(value = JSON_PROPERTY_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUrl(@Nullable URI url) {
        this.url = url;
    }

    public CatalogEntry data(@Nullable CatalogNested data) {
        this.data = data;
        return this;
    }

    /**
     * Inline nested catalog of per-protocol children, present only on a multi-protocol outer entry. Mutually exclusive
     * with &#x60;url&#x60;.
     * @return data
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DATA, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public CatalogNested getData() {
        return data;
    }

    @JsonProperty(value = JSON_PROPERTY_DATA, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setData(@Nullable CatalogNested data) {
        this.data = data;
    }

    public CatalogEntry tags(@Nullable List<String> tags) {
        this.tags = tags;
        return this;
    }

    public CatalogEntry addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
        return this;
    }

    /**
     * Get tags
     * @return tags
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TAGS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty(value = JSON_PROPERTY_TAGS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTags(@Nullable List<String> tags) {
        this.tags = tags;
    }

    public CatalogEntry updatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Get updatedAt
     * @return updatedAt
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_UPDATED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty(value = JSON_PROPERTY_UPDATED_AT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CatalogEntry publisher(@Nullable CatalogPublisher publisher) {
        this.publisher = publisher;
        return this;
    }

    /**
     * Get publisher
     * @return publisher
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_PUBLISHER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public CatalogPublisher getPublisher() {
        return publisher;
    }

    @JsonProperty(value = JSON_PROPERTY_PUBLISHER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setPublisher(@Nullable CatalogPublisher publisher) {
        this.publisher = publisher;
    }

    public CatalogEntry metadata(@Nullable CatalogEntryMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Get metadata
     * @return metadata
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_METADATA, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public CatalogEntryMetadata getMetadata() {
        return metadata;
    }

    @JsonProperty(value = JSON_PROPERTY_METADATA, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setMetadata(@Nullable CatalogEntryMetadata metadata) {
        this.metadata = metadata;
    }

    public CatalogEntry trustManifest(@Nullable CatalogTrustManifest trustManifest) {
        this.trustManifest = trustManifest;
        return this;
    }

    /**
     * Get trustManifest
     * @return trustManifest
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TRUST_MANIFEST, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public CatalogTrustManifest getTrustManifest() {
        return trustManifest;
    }

    @JsonProperty(value = JSON_PROPERTY_TRUST_MANIFEST, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTrustManifest(@Nullable CatalogTrustManifest trustManifest) {
        this.trustManifest = trustManifest;
    }

    /**
     * Return true if this CatalogEntry object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogEntry catalogEntry = (CatalogEntry) o;
        return Objects.equals(this.identifier, catalogEntry.identifier) &&
                Objects.equals(this.displayName, catalogEntry.displayName) &&
                Objects.equals(this.description, catalogEntry.description) &&
                Objects.equals(this.version, catalogEntry.version) &&
                Objects.equals(this.mediaType, catalogEntry.mediaType) &&
                Objects.equals(this.url, catalogEntry.url) &&
                Objects.equals(this.data, catalogEntry.data) &&
                Objects.equals(this.tags, catalogEntry.tags) &&
                Objects.equals(this.updatedAt, catalogEntry.updatedAt) &&
                Objects.equals(this.publisher, catalogEntry.publisher) &&
                Objects.equals(this.metadata, catalogEntry.metadata) &&
                Objects.equals(this.trustManifest, catalogEntry.trustManifest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, displayName, description, version, mediaType, url, data, tags, updatedAt,
                publisher, metadata, trustManifest);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogEntry {\n");
        sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
        sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    mediaType: ").append(toIndentedString(mediaType)).append("\n");
        sb.append("    url: ").append(toIndentedString(url)).append("\n");
        sb.append("    data: ").append(toIndentedString(data)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
        sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
        sb.append("    publisher: ").append(toIndentedString(publisher)).append("\n");
        sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
        sb.append("    trustManifest: ").append(toIndentedString(trustManifest)).append("\n");
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
