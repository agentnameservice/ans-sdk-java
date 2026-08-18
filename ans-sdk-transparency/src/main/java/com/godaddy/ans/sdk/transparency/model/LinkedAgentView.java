package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One entry of the identity reverse join: an agent that an identity currently links to.
 *
 * <p>The transparency log computes this view at query time from the link index. Each entry
 * carries the linked agent's own computed badge status, so a reader checks both ends of the
 * link in one response.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedAgentView {

    @JsonProperty("ansId")
    private String ansId;

    @JsonProperty("linkedAt")
    private String linkedAt;

    @JsonProperty("agentStatus")
    private String agentStatus;

    public LinkedAgentView() {
    }

    /**
     * Returns the linked agent's ANS identifier.
     *
     * @return the agent ANS ID
     */
    public String getAnsId() {
        return ansId;
    }

    public void setAnsId(String ansId) {
        this.ansId = ansId;
    }

    /**
     * Returns the producer timestamp of the sealed link event that bound this agent.
     *
     * @return the link timestamp, or null if not provided
     */
    public String getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(String linkedAt) {
        this.linkedAt = linkedAt;
    }

    /**
     * Returns the linked agent's own computed badge status.
     *
     * @return the agent status, or null if not provided
     */
    public String getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(String agentStatus) {
        this.agentStatus = agentStatus;
    }

    @Override
    public String toString() {
        return "LinkedAgentView{"
            + "ansId='" + ansId + '\''
            + ", agentStatus='" + agentStatus + '\''
            + '}';
    }
}
