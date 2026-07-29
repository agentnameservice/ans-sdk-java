package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;

/**
 * IdentityLinkResponse
 */
@JsonPropertyOrder({
    IdentityLinkResponse.JSON_PROPERTY_LINKED
})
public class IdentityLinkResponse {
    public static final String JSON_PROPERTY_LINKED = "linked";

    @Nonnull
    private Integer linked;

    public IdentityLinkResponse() {
    }

    public IdentityLinkResponse linked(@Nonnull Integer linked) {
        this.linked = linked;
        return this;
    }

    /**
     * Newly-created links (already-linked agents are skipped)
     * @return linked
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_LINKED, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getLinked() {
        return linked;
    }

    @JsonProperty(value = JSON_PROPERTY_LINKED, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setLinked(@Nonnull Integer linked) {
        this.linked = linked;
    }

    /**
     * Return true if this IdentityLinkResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityLinkResponse identityLinkResponse = (IdentityLinkResponse) o;
        return Objects.equals(this.linked, identityLinkResponse.linked);
    }

    @Override
    public int hashCode() {
        return Objects.hash(linked);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityLinkResponse {\n");
        sb.append("    linked: ").append(toIndentedString(linked)).append("\n");
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
