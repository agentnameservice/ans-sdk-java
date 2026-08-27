package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Entry-level identifiers a consumer verifies against (IMPL §5.3). No &#x60;logId&#x60; — the TL is keyed by agentId,
 * not logId.
 */
@JsonPropertyOrder({
    CatalogEntryMetadata.JSON_PROPERTY_ANS_NAME,
    CatalogEntryMetadata.JSON_PROPERTY_AGENT_HOST,
    CatalogEntryMetadata.JSON_PROPERTY_BADGE_URL
})
public class CatalogEntryMetadata {
    public static final String JSON_PROPERTY_ANS_NAME = "ansName";

    @Nonnull
    private String ansName;

    public static final String JSON_PROPERTY_AGENT_HOST = "agentHost";

    @Nonnull
    private String agentHost;

    public static final String JSON_PROPERTY_BADGE_URL = "badgeUrl";

    @Nullable
    private URI badgeUrl;

    public CatalogEntryMetadata() {
    }

    public CatalogEntryMetadata ansName(@Nonnull String ansName) {
        this.ansName = ansName;
        return this;
    }

    /**
     * Get ansName
     * @return ansName
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ANS_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAnsName() {
        return ansName;
    }

    @JsonProperty(value = JSON_PROPERTY_ANS_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAnsName(@Nonnull String ansName) {
        this.ansName = ansName;
    }

    public CatalogEntryMetadata agentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
        return this;
    }

    /**
     * Get agentHost
     * @return agentHost
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_HOST, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAgentHost() {
        return agentHost;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_HOST, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
    }

    public CatalogEntryMetadata badgeUrl(@Nullable URI badgeUrl) {
        this.badgeUrl = badgeUrl;
        return this;
    }

    /**
     * TL status / card-integrity surface (&#x60;&lt;tl&gt;/v1/agents/{agentId}&#x60;). Omitted when no TL base URL is
     * configured.
     * @return badgeUrl
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_BADGE_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public URI getBadgeUrl() {
        return badgeUrl;
    }

    @JsonProperty(value = JSON_PROPERTY_BADGE_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setBadgeUrl(@Nullable URI badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

    /**
     * Return true if this CatalogEntryMetadata object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CatalogEntryMetadata catalogEntryMetadata = (CatalogEntryMetadata) o;
        return Objects.equals(this.ansName, catalogEntryMetadata.ansName) &&
                Objects.equals(this.agentHost, catalogEntryMetadata.agentHost) &&
                Objects.equals(this.badgeUrl, catalogEntryMetadata.badgeUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ansName, agentHost, badgeUrl);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CatalogEntryMetadata {\n");
        sb.append("    ansName: ").append(toIndentedString(ansName)).append("\n");
        sb.append("    agentHost: ").append(toIndentedString(agentHost)).append("\n");
        sb.append("    badgeUrl: ").append(toIndentedString(badgeUrl)).append("\n");
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
