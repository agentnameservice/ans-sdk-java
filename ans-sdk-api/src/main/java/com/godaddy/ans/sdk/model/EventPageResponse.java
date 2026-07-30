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
 * Paginated response containing ANS events.
 */
@JsonPropertyOrder({
    EventPageResponse.JSON_PROPERTY_ITEMS,
    EventPageResponse.JSON_PROPERTY_LAST_LOG_ID
})
public class EventPageResponse {
    public static final String JSON_PROPERTY_ITEMS = "items";

    @Nonnull
    private List<EventItem> items = new ArrayList<>();

    public static final String JSON_PROPERTY_LAST_LOG_ID = "lastLogId";

    @Nullable
    private String lastLogId;

    public EventPageResponse() {
    }

    public EventPageResponse items(@Nonnull List<EventItem> items) {
        this.items = items;
        return this;
    }

    public EventPageResponse addItemsItem(EventItem itemsItem) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(itemsItem);
        return this;
    }

    /**
     * Array of event items (always present; &#x60;[]&#x60; when empty).
     * @return items
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ITEMS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<EventItem> getItems() {
        return items;
    }

    @JsonProperty(value = JSON_PROPERTY_ITEMS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setItems(@Nonnull List<EventItem> items) {
        this.items = items;
    }

    public EventPageResponse lastLogId(@Nullable String lastLogId) {
        this.lastLogId = lastLogId;
        return this;
    }

    /**
     * The logId of the last event in this page. Pass it as the next request&#39;s &#x60;lastLogId&#x60; to fetch the
     * following page. Omitted when there are no more results.
     * @return lastLogId
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_LAST_LOG_ID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getLastLogId() {
        return lastLogId;
    }

    @JsonProperty(value = JSON_PROPERTY_LAST_LOG_ID, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setLastLogId(@Nullable String lastLogId) {
        this.lastLogId = lastLogId;
    }

    /**
     * Return true if this EventPageResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventPageResponse eventPageResponse = (EventPageResponse) o;
        return Objects.equals(this.items, eventPageResponse.items) &&
                Objects.equals(this.lastLogId, eventPageResponse.lastLogId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, lastLogId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EventPageResponse {\n");
        sb.append("    items: ").append(toIndentedString(items)).append("\n");
        sb.append("    lastLogId: ").append(toIndentedString(lastLogId)).append("\n");
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
