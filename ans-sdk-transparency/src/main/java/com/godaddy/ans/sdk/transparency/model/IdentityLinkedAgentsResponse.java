package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Paginated reverse join: the agents an identity currently links to.
 *
 * <p>This is the response of {@code GET /v1/identities/{identityId}/agents}. The {@code total}
 * field carries the full count before pagination, so a caller pages the whole set even when a
 * single page is capped by {@code limit}.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentityLinkedAgentsResponse {

    @JsonProperty("agents")
    private List<LinkedAgentView> agents;

    @JsonProperty("total")
    private int total;

    public IdentityLinkedAgentsResponse() {
    }

    /**
     * Returns the agents in this page of the reverse join.
     *
     * @return the linked agents, or null if the response carried no list
     */
    public List<LinkedAgentView> getAgents() {
        return agents;
    }

    public void setAgents(List<LinkedAgentView> agents) {
        this.agents = agents;
    }

    /**
     * Returns the full count of linked agents before pagination.
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
        return "IdentityLinkedAgentsResponse{"
            + "agents=" + (agents != null ? agents.size() : 0)
            + ", total=" + total
            + '}';
    }
}
