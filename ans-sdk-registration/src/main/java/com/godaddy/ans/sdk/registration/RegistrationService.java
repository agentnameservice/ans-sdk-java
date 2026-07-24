package com.godaddy.ans.sdk.registration;

import com.godaddy.ans.sdk.config.ApiVersion;
import com.godaddy.ans.sdk.exception.AnsServerException;
import com.godaddy.ans.sdk.model.generated.AgentDetails;
import com.godaddy.ans.sdk.model.generated.AgentRegistrationRequest;
import com.godaddy.ans.sdk.model.generated.AgentRevocationRequest;
import com.godaddy.ans.sdk.model.generated.AgentRevocationResponse;
import com.godaddy.ans.sdk.model.generated.AgentStatus;
import com.godaddy.ans.sdk.model.generated.Link;
import com.godaddy.ans.sdk.model.generated.RegistrationPending;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

/**
 * Internal service for handling registration API calls.
 */
class RegistrationService {

    private final AnsApiClient httpClient;
    private final ApiVersion apiVersion;

    RegistrationService(final AnsApiClient ansApiClient) {
        this.httpClient = ansApiClient;
        this.apiVersion = ansApiClient.getApiVersion();
    }

    /**
     * Registers a new agent and returns full agent details.
     *
     * <p>On the v2 lane the {@code agentId} returned in the registration response
     * is used directly to fetch the complete {@link AgentDetails}. On the v1 lane
     * the 'self' link is followed instead (HATEOAS).</p>
     */
    AgentDetails register(AgentRegistrationRequest request) {
        // discoveryProfiles is a v2-only field; strip it from the wire body on v1.
        String requestBody = (apiVersion == ApiVersion.V1)
            ? httpClient.serializeToJsonWithoutField(request, "discoveryProfiles")
            : httpClient.serializeToJson(request);

        HttpRequest httpRequest = httpClient.createRequestBuilder(AgentPaths.registerPath(apiVersion))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        RegistrationPending pending = httpClient.parseResponse(response.body(), RegistrationPending.class);

        return getAgentDetails(resolveAgentDetailsPath(pending));
    }

    /**
     * Resolves the path to fetch full agent details after registration.
     *
     * <p>v2 uses {@code pending.getAgentId()} directly; v1 follows the 'self' link.</p>
     */
    private String resolveAgentDetailsPath(RegistrationPending pending) {
        if (apiVersion == ApiVersion.V1) {
            String selfPath = extractSelfLink(pending);
            if (selfPath == null) {
                throw new AnsServerException("Registration response missing 'self' link", 0, null);
            }
            return selfPath;
        }

        UUID agentId = pending.getAgentId();
        if (agentId == null) {
            throw new AnsServerException("Registration response missing 'agentId'", 0, null);
        }
        return AgentPaths.agentPath(apiVersion, agentId.toString());
    }

    /**
     * Gets agent details by path.
     */
    AgentDetails getAgentDetails(String path) {
        HttpRequest httpRequest = httpClient.createRequestBuilder(path)
            .GET()
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), AgentDetails.class);
    }

    /**
     * Gets agent details by agent ID.
     */
    AgentDetails getAgent(String agentId) {
        return getAgentDetails(AgentPaths.agentPath(apiVersion, agentId));
    }

    /**
     * Triggers ACME verification.
     */
    AgentStatus verifyAcme(String agentId) {
        HttpRequest request = httpClient.createRequestBuilder(
                AgentPaths.agentPath(apiVersion, agentId, "verify-acme"))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        HttpResponse<String> response = httpClient.sendRequest(request);
        return httpClient.parseResponse(response.body(), AgentStatus.class);
    }

    /**
     * Triggers DNS verification.
     */
    AgentStatus verifyDns(String agentId) {
        HttpRequest request = httpClient.createRequestBuilder(
                AgentPaths.agentPath(apiVersion, agentId, "verify-dns"))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        HttpResponse<String> response = httpClient.sendRequest(request);
        return httpClient.parseResponse(response.body(), AgentStatus.class);
    }

    /**
     * Revokes an agent registration.
     *
     * <p>For ACTIVE agents, this revokes the agent's certificates and marks the
     * registration as REVOKED. For PENDING registrations (after ACME verification),
     * this cancels the registration and revokes any already-issued certificates.</p>
     *
     * @param agentId the agent ID to revoke
     * @param request the revocation request with reason and optional comments
     * @return the revocation response with details about DNS records to remove
     */
    AgentRevocationResponse revoke(String agentId, AgentRevocationRequest request) {
        String requestBody = httpClient.serializeToJson(request);

        HttpRequest httpRequest = httpClient.createRequestBuilder(
                AgentPaths.agentPath(apiVersion, agentId, "revoke"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), AgentRevocationResponse.class);
    }

    /**
     * Extracts the 'self' link path from a RegistrationPending response.
     */
    private String extractSelfLink(RegistrationPending pending) {
        if (pending.getLinks() == null) {
            return null;
        }
        for (Link link : pending.getLinks()) {
            if ("self".equals(link.getRel()) && link.getHref() != null) {
                return link.getHref().getPath();
            }
        }
        return null;
    }
}