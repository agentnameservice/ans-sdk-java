package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * A verifiable claim (AI Catalog §5.4). The ANS-Registration attestation points at the agent&#39;s SCITT receipt on the
 * Transparency Log; it carries no digest (the receipt proves inclusion, not entry content).
 */
@JsonPropertyOrder({
    CatalogAttestation.JSON_PROPERTY_TYPE,
    CatalogAttestation.JSON_PROPERTY_URI,
    CatalogAttestation.JSON_PROPERTY_MEDIA_TYPE
})
public class CatalogAttestation {
    public static final String JSON_PROPERTY_TYPE = "type";

    @Nonnull
    private String type;

    public static final String JSON_PROPERTY_URI = "uri";

    @Nonnull
    private URI uri;

    public static final String JSON_PROPERTY_MEDIA_TYPE = "mediaType";

    @Nonnull
    private String mediaType;

    public CatalogAttestation() {
    }

    public CatalogAttestation type(@Nonnull String type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     * @return type
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getType() {
        return type;
    }

    @JsonProperty(value = JSON_PROPERTY_TYPE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setType(@Nonnull String type) {
        this.type = type;
    }

    public CatalogAttestation uri(@Nonnull URI uri) {
        this.uri = uri;
        return this;
    }

    /**
     * Get uri
     * @return uri
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_URI, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public URI getUri() {
        return uri;
    }

    @JsonProperty(value = JSON_PROPERTY_URI, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setUri(@Nonnull URI uri) {
        this.uri = uri;
    }

    public CatalogAttestation mediaType(@Nonnull String mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    /**
     * Get mediaType
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

    /**
     * Return true if this CatalogAttestation object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogAttestation catalogAttestation = (CatalogAttestation) o;
        return Objects.equals(this.type, catalogAttestation.type) &&
                Objects.equals(this.uri, catalogAttestation.uri) &&
                Objects.equals(this.mediaType, catalogAttestation.mediaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, uri, mediaType);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogAttestation {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    mediaType: ").append(toIndentedString(mediaType)).append("\n");
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
