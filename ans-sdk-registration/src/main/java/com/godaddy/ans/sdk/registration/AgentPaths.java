package com.godaddy.ans.sdk.registration;

import com.godaddy.ans.sdk.config.ApiVersion;

/**
 * Builds ANS agent API paths for the selected {@link ApiVersion} lane.
 *
 * <p>All version-dependent path branching lives here so registration and
 * certificate services never string-concatenate lane-specific paths inline.</p>
 */
final class AgentPaths {

    private static final String V2_COLLECTION = "/v2/ans/agents";
    private static final String V1_COLLECTION = "/v1/agents";

    private AgentPaths() {
    }

    /**
     * Returns the agents collection path for the given lane.
     *
     * @param apiVersion the selected API version
     * @return {@code /v2/ans/agents} for v2, {@code /v1/agents} for v1
     */
    static String agentsCollectionPath(ApiVersion apiVersion) {
        return apiVersion == ApiVersion.V1 ? V1_COLLECTION : V2_COLLECTION;
    }

    /**
     * Returns the registration path for the given lane.
     *
     * <p>v2 registers via a standard REST collection POST; v1 uses the legacy
     * {@code /register} verb suffix.</p>
     *
     * @param apiVersion the selected API version
     * @return {@code /v2/ans/agents} for v2, {@code /v1/agents/register} for v1
     */
    static String registerPath(ApiVersion apiVersion) {
        return apiVersion == ApiVersion.V1 ? V1_COLLECTION + "/register" : V2_COLLECTION;
    }

    /**
     * Builds an agent-scoped path: collection + agentId + any trailing segments.
     *
     * @param apiVersion the selected API version
     * @param agentId the agent ID
     * @param segments optional trailing path segments (e.g. {@code "certificates", "identity"})
     * @return the joined path, e.g. {@code /v2/ans/agents/{agentId}/certificates/identity}
     */
    static String agentPath(ApiVersion apiVersion, String agentId, String... segments) {
        StringBuilder path = new StringBuilder(agentsCollectionPath(apiVersion))
            .append('/')
            .append(agentId);
        for (String segment : segments) {
            path.append('/').append(segment);
        }
        return path.toString();
    }
}
