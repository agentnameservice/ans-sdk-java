package com.godaddy.ans.sdk.discovery;

/**
 * Request body for the deprecated {@code POST /v1/agents/resolution} endpoint.
 *
 * <p>Not generated from the OpenAPI spec: the v2 spec no longer documents this
 * v1-only endpoint, so this local DTO fills in for the removed
 * {@code AgentCapabilityRequest} model.</p>
 */
final class AgentCapabilityRequest {

    private String agentHost;
    private String version;

    AgentCapabilityRequest agentHost(String agentHost) {
        this.agentHost = agentHost;
        return this;
    }

    AgentCapabilityRequest version(String version) {
        this.version = version;
        return this;
    }

    public String getAgentHost() {
        return agentHost;
    }

    public String getVersion() {
        return version;
    }
}