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
 * Ownership-scoped, cursor-paginated list of the caller&#39;s verified identities. Mirrors
 * &#x60;AgentListResponse&#x60;: the collection array is named &#x60;items&#x60; per the standard REST
 * collection-response convention shared across the v2 surface.
 */
@JsonPropertyOrder({
    IdentityListResponse.JSON_PROPERTY_ITEMS,
    IdentityListResponse.JSON_PROPERTY_RETURNED_COUNT,
    IdentityListResponse.JSON_PROPERTY_LIMIT,
    IdentityListResponse.JSON_PROPERTY_NEXT_CURSOR,
    IdentityListResponse.JSON_PROPERTY_HAS_MORE
})public class IdentityListResponse {
    public static final String JSON_PROPERTY_ITEMS = "items";

    @Nonnull
    private List<IdentityDetails> items = new ArrayList<>();

    public static final String JSON_PROPERTY_RETURNED_COUNT = "returnedCount";

    @Nonnull
    private Integer returnedCount;

    public static final String JSON_PROPERTY_LIMIT = "limit";

    @Nonnull
    private Integer limit;

    public static final String JSON_PROPERTY_NEXT_CURSOR = "nextCursor";

    @Nullable
    private String nextCursor;

    public static final String JSON_PROPERTY_HAS_MORE = "hasMore";

    @Nonnull
    private Boolean hasMore;

    public IdentityListResponse() {
    }

    public IdentityListResponse items(@Nonnull List<IdentityDetails> items) {
        this.items = items;
        return this;
    }

    public IdentityListResponse addItemsItem(IdentityDetails itemsItem) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(itemsItem);
        return this;
    }

    /**
     * List of identities owned by the caller
     * @return items
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ITEMS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<IdentityDetails> getItems() {
        return items;
    }

    @JsonProperty(value = JSON_PROPERTY_ITEMS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setItems(@Nonnull List<IdentityDetails> items) {
        this.items = items;
    }

    public IdentityListResponse returnedCount(@Nonnull Integer returnedCount) {
        this.returnedCount = returnedCount;
        return this;
    }

    /**
     * Number of identities in this response
     * @return returnedCount
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_RETURNED_COUNT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getReturnedCount() {
        return returnedCount;
    }

    @JsonProperty(value = JSON_PROPERTY_RETURNED_COUNT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setReturnedCount(@Nonnull Integer returnedCount) {
        this.returnedCount = returnedCount;
    }

    public IdentityListResponse limit(@Nonnull Integer limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Limit that was applied
     * @return limit
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_LIMIT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getLimit() {
        return limit;
    }

    @JsonProperty(value = JSON_PROPERTY_LIMIT, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setLimit(@Nonnull Integer limit) {
        this.limit = limit;
    }

    public IdentityListResponse nextCursor(@Nullable String nextCursor) {
        this.nextCursor = nextCursor;
        return this;
    }

    /**
     * Opaque cursor for the next page. Pass as the &#x60;cursor&#x60; query parameter to retrieve the next page. Null
     * when there are no more results.
     * @return nextCursor
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_NEXT_CURSOR, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getNextCursor() {
        return nextCursor;
    }

    @JsonProperty(value = JSON_PROPERTY_NEXT_CURSOR, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setNextCursor(@Nullable String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public IdentityListResponse hasMore(@Nonnull Boolean hasMore) {
        this.hasMore = hasMore;
        return this;
    }

    /**
     * Whether more results exist beyond this page
     * @return hasMore
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_HAS_MORE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Boolean getHasMore() {
        return hasMore;
    }

    @JsonProperty(value = JSON_PROPERTY_HAS_MORE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setHasMore(@Nonnull Boolean hasMore) {
        this.hasMore = hasMore;
    }

    /**
     * Return true if this IdentityListResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdentityListResponse identityListResponse = (IdentityListResponse) o;
        return Objects.equals(this.items, identityListResponse.items) &&
                Objects.equals(this.returnedCount, identityListResponse.returnedCount) &&
                Objects.equals(this.limit, identityListResponse.limit) &&
                Objects.equals(this.nextCursor, identityListResponse.nextCursor) &&
                Objects.equals(this.hasMore, identityListResponse.hasMore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, returnedCount, limit, nextCursor, hasMore);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class IdentityListResponse {\n");
        sb.append("    items: ").append(toIndentedString(items)).append("\n");
        sb.append("    returnedCount: ").append(toIndentedString(returnedCount)).append("\n");
        sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
        sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
        sb.append("    hasMore: ").append(toIndentedString(hasMore)).append("\n");
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
