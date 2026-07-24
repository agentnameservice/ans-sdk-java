package com.godaddy.ans.sdk.registration;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.godaddy.ans.sdk.auth.JwtCredentialsProvider;
import com.godaddy.ans.sdk.config.ApiVersion;
import com.godaddy.ans.sdk.config.Environment;
import com.godaddy.ans.sdk.model.generated.AgentEndpoint;
import com.godaddy.ans.sdk.model.generated.AgentRegistrationRequest;
import com.godaddy.ans.sdk.model.generated.AgentRevocationRequest;
import com.godaddy.ans.sdk.model.generated.Protocol;
import com.godaddy.ans.sdk.model.generated.RevocationReason;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.absent;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies API-version path routing across the v1 and v2 lanes.
 *
 * <p>The v2 default lane is exercised by {@link RegistrationClientTest}; this
 * class pins the client to {@link ApiVersion#V1} and asserts the legacy
 * {@code /v1/agents/...} paths plus v1-specific wire-body behavior.</p>
 */
@WireMockTest
class ApiVersionRoutingTest {

    private static final String TEST_JWT_TOKEN = "test-jwt-token";
    private static final String TEST_AGENT_ID = "550e8400-e29b-41d4-a716-446655440000";

    private RegistrationClient v1Client(WireMockRuntimeInfo wm) {
        return RegistrationClient.builder()
            .environment(Environment.OTE)
            .baseUrl(wm.getHttpBaseUrl())
            .apiVersion(ApiVersion.V1)
            .credentialsProvider(new JwtCredentialsProvider(TEST_JWT_TOKEN))
            .build();
    }

    private AgentRegistrationRequest sampleRequest() {
        return new AgentRegistrationRequest()
            .agentDisplayName("Test Agent")
            .version("1.0.0")
            .agentHost("test-agent.example.com")
            .addEndpointsItem(new AgentEndpoint()
                .protocol(Protocol.A2_A)
                .agentUrl(URI.create("https://test-agent.example.com/a2a")))
            .identityCsrPEM("test-csr")
            .serverCsrPEM("test-csr");
    }

    @Test
    @DisplayName("v1 register posts to /v1/agents/register and follows the self link")
    void v1RegisterFollowsSelfLink(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/v1/agents/register"))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody(v1PendingWithSelfLink())));

        stubFor(get(urlEqualTo("/v1/agents/" + TEST_AGENT_ID))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(agentDetails())));

        assertThat(v1Client(wm).registerAgent(sampleRequest())).isNotNull();

        verify(postRequestedFor(urlEqualTo("/v1/agents/register")));
    }

    @Test
    @DisplayName("v1 register omits the v2-only discoveryProfiles field from the wire body")
    void v1RegisterStripsDiscoveryProfiles(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/v1/agents/register"))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody(v1PendingWithSelfLink())));

        stubFor(get(urlEqualTo("/v1/agents/" + TEST_AGENT_ID))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(agentDetails())));

        v1Client(wm).registerAgent(sampleRequest());

        verify(postRequestedFor(urlEqualTo("/v1/agents/register"))
            .withRequestBody(containing("\"agentDisplayName\":\"Test Agent\""))
            .withRequestBody(matchingJsonPath("$.discoveryProfiles", absent())));
    }

    @Test
    @DisplayName("v1 getAgent targets /v1/agents/{agentId}")
    void v1GetAgentPath(WireMockRuntimeInfo wm) {
        stubFor(get(urlEqualTo("/v1/agents/" + TEST_AGENT_ID))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(agentDetails())));

        assertThat(v1Client(wm).getAgent(TEST_AGENT_ID)).isNotNull();
    }

    @Test
    @DisplayName("v1 verifyAcme targets /v1/agents/{agentId}/verify-acme")
    void v1VerifyAcmePath(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/v1/agents/" + TEST_AGENT_ID + "/verify-acme"))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody(agentStatus())));

        assertThat(v1Client(wm).verifyAcme(TEST_AGENT_ID)).isNotNull();
    }

    @Test
    @DisplayName("v1 verifyDns targets /v1/agents/{agentId}/verify-dns")
    void v1VerifyDnsPath(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/v1/agents/" + TEST_AGENT_ID + "/verify-dns"))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody(agentStatus())));

        assertThat(v1Client(wm).verifyDns(TEST_AGENT_ID)).isNotNull();
    }

    @Test
    @DisplayName("v1 revoke targets /v1/agents/{agentId}/revoke")
    void v1RevokePath(WireMockRuntimeInfo wm) {
        stubFor(post(urlEqualTo("/v1/agents/" + TEST_AGENT_ID + "/revoke"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(revocation())));

        AgentRevocationRequest request = new AgentRevocationRequest()
            .reason(RevocationReason.CESSATION_OF_OPERATION);

        assertThat(v1Client(wm).revokeAgent(TEST_AGENT_ID, request)).isNotNull();
    }

    // ==================== Response bodies ====================

    private String v1PendingWithSelfLink() {
        return """
            {
                "status": "PENDING_VALIDATION",
                "ansName": "ans://v1.0.0.test-agent.example.com",
                "links": [
                    { "rel": "self", "href": "/v1/agents/550e8400-e29b-41d4-a716-446655440000" }
                ]
            }
            """;
    }

    private String agentDetails() {
        return """
            {
                "agentId": "550e8400-e29b-41d4-a716-446655440000",
                "agentDisplayName": "Test Agent",
                "version": "1.0.0",
                "agentHost": "test-agent.example.com",
                "ansName": "ans://v1.0.0.test-agent.example.com",
                "agentStatus": "PENDING_VALIDATION",
                "endpoints": [
                    { "protocol": "A2A", "agentUrl": "https://test-agent.example.com/a2a" }
                ]
            }
            """;
    }

    private String agentStatus() {
        return """
            {
                "status": "PENDING_DNS",
                "phase": "DOMAIN_VALIDATION",
                "completedSteps": ["REGISTRATION_SUBMITTED"],
                "pendingSteps": ["DNS_VERIFICATION"],
                "createdAt": "2024-01-15T10:00:00Z",
                "updatedAt": "2024-01-15T10:30:00Z"
            }
            """;
    }

    private String revocation() {
        return """
            {
                "agentId": "550e8400-e29b-41d4-a716-446655440000",
                "ansName": "ans://v1.0.0.test-agent.example.com",
                "status": "REVOKED",
                "revokedAt": "2024-01-15T12:00:00Z",
                "reason": "CESSATION_OF_OPERATION",
                "dnsRecordsToRemove": []
            }
            """;
    }
}
