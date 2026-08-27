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
 * Ownership-scoped, cursor-paginated list of the caller&#39;s agents. Replaces v1&#39;s
 * &#x60;AgentSearchResponse&#x60;. Collection array is named &#x60;items&#x60; per standard REST collection-response
 * conventions.
 */
@JsonPropertyOrder({
    AgentListResponse.JSON_PROPERTY_ITEMS,
    AgentListResponse.JSON_PROPERTY_RETURNED_COUNT,
    AgentListResponse.JSON_PROPERTY_LIMIT,
    AgentListResponse.JSON_PROPERTY_NEXT_CURSOR,
    AgentListResponse.JSON_PROPERTY_HAS_MORE
})
public class AgentListResponse {
    public static final String JSON_PROPERTY_ITEMS = "items";

    @Nonnull
    private List<AgentListResponseItemsInner> items = new ArrayList<>();

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

    public AgentListResponse() {
    }

    public AgentListResponse items(@Nonnull List<AgentListResponseItemsInner> items) {
        this.items = items;
        return this;
    }

    public AgentListResponse addItemsItem(AgentListResponseItemsInner itemsItem) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(itemsItem);
        return this;
    }

    /**
     * List of agents owned by the caller
     * @return items
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ITEMS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<AgentListResponseItemsInner> getItems() {
        return items;
    }

    @JsonProperty(value = JSON_PROPERTY_ITEMS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setItems(@Nonnull List<AgentListResponseItemsInner> items) {
        this.items = items;
    }

    public AgentListResponse returnedCount(@Nonnull Integer returnedCount) {
        this.returnedCount = returnedCount;
        return this;
    }

    /**
     * Number of agents in this response
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

    public AgentListResponse limit(@Nonnull Integer limit) {
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

    public AgentListResponse nextCursor(@Nullable String nextCursor) {
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

    public AgentListResponse hasMore(@Nonnull Boolean hasMore) {
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
     * Return true if this AgentListResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentListResponse agentListResponse = (AgentListResponse) o;
        return Objects.equals(this.items, agentListResponse.items) &&
                Objects.equals(this.returnedCount, agentListResponse.returnedCount) &&
                Objects.equals(this.limit, agentListResponse.limit) &&
                Objects.equals(this.nextCursor, agentListResponse.nextCursor) &&
                Objects.equals(this.hasMore, agentListResponse.hasMore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, returnedCount, limit, nextCursor, hasMore);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentListResponse {\n");
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
