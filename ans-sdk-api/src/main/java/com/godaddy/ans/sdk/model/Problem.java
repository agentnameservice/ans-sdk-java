package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * RFC 7807 Problem Details — the actual error shape every RA failure path emits, served as
 * &#x60;application/problem+json&#x60;. Clients read &#x60;code&#x60; for programmatic handling and &#x60;detail&#x60;
 * for the human-readable explanation.  (The legacy &#x60;ErrorResponse&#x60; schema below is retained only for the
 * pre-existing routes still declared against it; new routes point here. Migrating the remaining declarations is tracked
 * separately.)
 */
@JsonPropertyOrder({
    Problem.JSON_PROPERTY_TYPE,
    Problem.JSON_PROPERTY_TITLE,
    Problem.JSON_PROPERTY_STATUS,
    Problem.JSON_PROPERTY_DETAIL,
    Problem.JSON_PROPERTY_CODE
})
public class Problem {
    public static final String JSON_PROPERTY_TYPE = "type";

    @Nullable
    private String type;

    public static final String JSON_PROPERTY_TITLE = "title";

    @Nonnull
    private String title;

    public static final String JSON_PROPERTY_STATUS = "status";

    @Nonnull
    private Integer status;

    public static final String JSON_PROPERTY_DETAIL = "detail";

    @Nullable
    private String detail;

    public static final String JSON_PROPERTY_CODE = "code";

    @Nonnull
    private String code;

    public Problem() {
    }

    public Problem type(@Nullable String type) {
        this.type = type;
        return this;
    }

    /**
     * URI reference identifying the problem type.
     * @return type
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getType() {
        return type;
    }

    @JsonProperty(value = JSON_PROPERTY_TYPE, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setType(@Nullable String type) {
        this.type = type;
    }

    public Problem title(@Nonnull String title) {
        this.title = title;
        return this;
    }

    /**
     * Short, human-readable summary of the problem type.
     * @return title
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_TITLE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getTitle() {
        return title;
    }

    @JsonProperty(value = JSON_PROPERTY_TITLE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setTitle(@Nonnull String title) {
        this.title = title;
    }

    public Problem status(@Nonnull Integer status) {
        this.status = status;
        return this;
    }

    /**
     * HTTP status code.
     * @return status
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Integer getStatus() {
        return status;
    }

    @JsonProperty(value = JSON_PROPERTY_STATUS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setStatus(@Nonnull Integer status) {
        this.status = status;
    }

    public Problem detail(@Nullable String detail) {
        this.detail = detail;
        return this;
    }

    /**
     * Human-readable explanation specific to this occurrence.
     * @return detail
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DETAIL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getDetail() {
        return detail;
    }

    @JsonProperty(value = JSON_PROPERTY_DETAIL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDetail(@Nullable String detail) {
        this.detail = detail;
    }

    public Problem code(@Nonnull String code) {
        this.code = code;
        return this;
    }

    /**
     * Stable, machine-readable error code for programmatic handling.
     * @return code
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CODE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCode() {
        return code;
    }

    @JsonProperty(value = JSON_PROPERTY_CODE, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCode(@Nonnull String code) {
        this.code = code;
    }

    /**
     * Return true if this Problem object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Problem problem = (Problem) o;
        return Objects.equals(this.type, problem.type) &&
                Objects.equals(this.title, problem.title) &&
                Objects.equals(this.status, problem.status) &&
                Objects.equals(this.detail, problem.detail) &&
                Objects.equals(this.code, problem.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, status, detail, code);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Problem {\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
        sb.append("    code: ").append(toIndentedString(code)).append("\n");
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
