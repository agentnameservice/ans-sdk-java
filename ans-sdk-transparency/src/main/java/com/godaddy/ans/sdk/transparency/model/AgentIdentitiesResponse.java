package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.godaddy.ans.sdk.model.LinkedIdentity;

import java.util.List;

/**
 * Paginated forward join: the identities an agent currently links to.
 *
 * <p>This is the response of {@code GET /v1/agents/{agentId}/identities}. It is the overflow read
 * target for the agent badge, which caps its inline {@code identities[]} at 25 entries. The
 * {@code total} field carries the full count before pagination, so a caller pages the whole set
 * even when a single page is capped by {@code limit}.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentIdentitiesResponse {

    @JsonProperty("identities")
    private List<LinkedIdentity> identities;

    @JsonProperty("total")
    private int total;

    public AgentIdentitiesResponse() {
    }

    /**
     * Returns the identities in this page of the forward join.
     *
     * @return the linked identities, or null if the response carried no list
     */
    public List<LinkedIdentity> getIdentities() {
        return identities;
    }

    public void setIdentities(List<LinkedIdentity> identities) {
        this.identities = identities;
    }

    /**
     * Returns the full count of linked identities before pagination.
     *
     * @return the total count
     */
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "AgentIdentitiesResponse{"
            + "identities=" + (identities != null ? identities.size() : 0)
            + ", total=" + total
            + '}';
    }
}
