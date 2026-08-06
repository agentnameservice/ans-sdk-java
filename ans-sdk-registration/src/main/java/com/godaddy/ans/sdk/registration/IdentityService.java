package com.godaddy.ans.sdk.registration;

import com.godaddy.ans.sdk.exception.AnsServerException;
import com.godaddy.ans.sdk.model.IdentityChallengeResponse;
import com.godaddy.ans.sdk.model.IdentityDetails;
import com.godaddy.ans.sdk.model.IdentityLinkRequest;
import com.godaddy.ans.sdk.model.IdentityLinkResponse;
import com.godaddy.ans.sdk.model.IdentityListResponse;
import com.godaddy.ans.sdk.model.IdentityRegistrationRequest;
import com.godaddy.ans.sdk.model.VerifyControlRequest;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Internal service for ANS Verified-Identity management API calls.
 *
 * <p>Covers the eight RA management operations on the {@code /v2/ans/identities}
 * surface. All paths come from {@link IdentityPaths}; all HTTP work reuses
 * {@link AnsApiClient}, which maps error status codes to typed exceptions.</p>
 */
class IdentityService {

    /** Maximum number of agents that a single link request can carry. */
    private static final int MAX_LINK_AGENTS = 256;

    private final AnsApiClient httpClient;

    IdentityService(final AnsApiClient ansApiClient) {
        this.httpClient = ansApiClient;
    }

    /**
     * Registers a new identity and returns the 202 challenge round.
     *
     * <p>The identity is not sealed by this response. The caller must complete
     * the returned challenge and then submit a control proof to verify-control.</p>
     *
     * @param request the registration request (kind is inferred from the value)
     * @return the challenge round to complete
     */
    IdentityChallengeResponse register(IdentityRegistrationRequest request) {
        String requestBody = httpClient.serializeToJson(request);

        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.identitiesCollectionPath())
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return parseChallenge(response.body());
    }

    /**
     * Lists the caller's identities, cursor-paginated.
     *
     * @param limit optional page size (1..100), or {@code null} for the server default
     * @param cursor optional opaque page cursor, or {@code null} for the first page
     * @return the page of identities plus the next cursor
     */
    IdentityListResponse list(Integer limit, String cursor) {
        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.listPath(limit, cursor))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), IdentityListResponse.class);
    }

    /**
     * Gets the full details for a single identity.
     *
     * @param identityId the identity ID
     * @return the identity details
     */
    IdentityDetails getDetails(String identityId) {
        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.identityPath(identityId))
            .GET()
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), IdentityDetails.class);
    }

    /**
     * Rotates the key material for an identity and returns a fresh 202 challenge round.
     *
     * <p>Rotation is same-kind only. As with registration, the response is a
     * challenge to complete, not a verified state.</p>
     *
     * @param identityId the identity ID to rotate
     * @param request the rotation request
     * @return the new challenge round to complete
     */
    IdentityChallengeResponse rotate(String identityId, IdentityRegistrationRequest request) {
        String requestBody = httpClient.serializeToJson(request);

        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.identityPath(identityId))
            .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return parseChallenge(response.body());
    }

    /**
     * Submits a control proof for an identity.
     *
     * <p>Exactly one proof family may be present: {@code signedProofs} (JWS kinds)
     * or {@code cesrSignature} (lei). The SDK rejects both, or neither, before the
     * request leaves.</p>
     *
     * @param identityId the identity ID
     * @param request the control-proof request
     * @return the updated identity details
     * @throws IllegalArgumentException if the request carries both proof families or none
     */
    IdentityDetails verifyControl(String identityId, VerifyControlRequest request) {
        validateProofFamily(request);
        String requestBody = httpClient.serializeToJson(request);

        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.verifyControlPath(identityId))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), IdentityDetails.class);
    }

    /**
     * Revokes an identity.
     *
     * @param identityId the identity ID to revoke
     * @return the updated identity details
     */
    IdentityDetails revoke(String identityId) {
        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.revokePath(identityId))
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), IdentityDetails.class);
    }

    /**
     * Links an identity to one or more agents as an all-or-nothing batch.
     *
     * @param identityId the identity ID
     * @param request the link request carrying 1..256 agent IDs
     * @return the link response with the count of linked agents
     * @throws IllegalArgumentException if the batch is empty or exceeds 256 agents
     */
    IdentityLinkResponse link(String identityId, IdentityLinkRequest request) {
        validateLinkBatch(request);
        String requestBody = httpClient.serializeToJson(request);

        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.linksPath(identityId))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .build();

        HttpResponse<String> response = httpClient.sendRequest(httpRequest);
        return httpClient.parseResponse(response.body(), IdentityLinkResponse.class);
    }

    /**
     * Removes the link between an identity and a single agent.
     *
     * <p>The server returns 204 with no body. Nothing is parsed.</p>
     *
     * @param identityId the identity ID
     * @param agentId the linked agent ID to remove
     */
    void unlink(String identityId, String agentId) {
        HttpRequest httpRequest = httpClient.createRequestBuilder(IdentityPaths.linkPath(identityId, agentId))
            .DELETE()
            .build();

        httpClient.sendRequest(httpRequest);
    }

    /**
     * Parses a 202 challenge body and guards against missing required fields.
     *
     * <p>Jackson does not enforce {@code required} on deserialization, so a
     * malformed response can leave {@code identityId} or {@code nonce} null.
     * Such a response cannot drive the verify-control round and is a server fault.</p>
     */
    private IdentityChallengeResponse parseChallenge(String body) {
        IdentityChallengeResponse challenge =
            httpClient.parseResponse(body, IdentityChallengeResponse.class);

        if (challenge.getIdentityId() == null) {
            throw new AnsServerException("Identity challenge response missing 'identityId'", 0, null);
        }
        if (challenge.getNonce() == null) {
            throw new AnsServerException("Identity challenge response missing 'nonce'", 0, null);
        }
        return challenge;
    }

    /**
     * Enforces the exactly-one-proof-family rule before sending verify-control.
     */
    private void validateProofFamily(VerifyControlRequest request) {
        List<String> signedProofs = request.getSignedProofs();
        boolean hasJws = signedProofs != null && !signedProofs.isEmpty();
        boolean hasCesr = request.getCesrSignature() != null && !request.getCesrSignature().isEmpty();

        if (hasJws == hasCesr) {
            throw new IllegalArgumentException(
                "verify-control requires exactly one proof family: signedProofs (JWS) or cesrSignature (lei)");
        }
    }

    /**
     * Enforces the 1..256 agent-batch bound before sending a link request.
     */
    private void validateLinkBatch(IdentityLinkRequest request) {
        List<?> agentIds = request.getAgentIds();
        int count = agentIds == null ? 0 : agentIds.size();
        if (count < 1 || count > MAX_LINK_AGENTS) {
            throw new IllegalArgumentException(
                "link requires between 1 and " + MAX_LINK_AGENTS + " agentIds, got " + count);
        }
    }
}
