package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * Link
 */
@JsonPropertyOrder({
    Link.JSON_PROPERTY_REL,
    Link.JSON_PROPERTY_HREF
})
public class Link {
    public static final String JSON_PROPERTY_REL = "rel";

    @Nonnull
    private String rel;

    public static final String JSON_PROPERTY_HREF = "href";

    @Nonnull
    private URI href;

    public Link() {
    }

    public Link rel(@Nonnull String rel) {
        this.rel = rel;
        return this;
    }

    /**
     * Get rel
     * @return rel
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_REL, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getRel() {
        return rel;
    }

    @JsonProperty(value = JSON_PROPERTY_REL, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setRel(@Nonnull String rel) {
        this.rel = rel;
    }

    public Link href(@Nonnull URI href) {
        this.href = href;
        return this;
    }

    /**
     * Get href
     * @return href
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_HREF, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public URI getHref() {
        return href;
    }

    @JsonProperty(value = JSON_PROPERTY_HREF, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setHref(@Nonnull URI href) {
        this.href = href;
    }

    /**
     * Return true if this Link object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(this.rel, link.rel) &&
                Objects.equals(this.href, link.href);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rel, href);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Link {\n");
        sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
        sb.append("    href: ").append(toIndentedString(href)).append("\n");
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
