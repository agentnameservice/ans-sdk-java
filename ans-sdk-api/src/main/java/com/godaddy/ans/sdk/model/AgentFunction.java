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
 * AgentFunction
 */
@JsonPropertyOrder({
    AgentFunction.JSON_PROPERTY_ID,
    AgentFunction.JSON_PROPERTY_NAME,
    AgentFunction.JSON_PROPERTY_TAGS
})
public class AgentFunction {
    public static final String JSON_PROPERTY_ID = "id";

    @Nonnull
    private String id;

    public static final String JSON_PROPERTY_NAME = "name";

    @Nonnull
    private String name;

    public static final String JSON_PROPERTY_TAGS = "tags";

    @Nullable
    private List<String> tags = new ArrayList<>();

    public AgentFunction() {
    }

    public AgentFunction id(@Nonnull String id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     * @return id
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getId() {
        return id;
    }

    @JsonProperty(value = JSON_PROPERTY_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setId(@Nonnull String id) {
        this.id = id;
    }

    public AgentFunction name(@Nonnull String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     * @return name
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getName() {
        return name;
    }

    @JsonProperty(value = JSON_PROPERTY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public AgentFunction tags(@Nullable List<String> tags) {
        this.tags = tags;
        return this;
    }

    public AgentFunction addTagsItem(String tagsItem) {
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

    /**
     * Return true if this AgentFunction object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentFunction agentFunction = (AgentFunction) o;
        return Objects.equals(this.id, agentFunction.id) &&
                Objects.equals(this.name, agentFunction.name) &&
                Objects.equals(this.tags, agentFunction.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tags);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentFunction {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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
