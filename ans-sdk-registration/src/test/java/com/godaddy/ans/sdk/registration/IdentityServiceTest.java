package com.godaddy.ans.sdk.registration;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.godaddy.ans.sdk.auth.ApiKeyCredentialsProvider;
import com.godaddy.ans.sdk.config.AnsConfiguration;
import com.godaddy.ans.sdk.config.Environment;
import com.godaddy.ans.sdk.exception.AnsAuthenticationException;
import com.godaddy.ans.sdk.exception.AnsNotFoundException;
import com.godaddy.ans.sdk.exception.AnsServerException;
import com.godaddy.ans.sdk.exception.AnsValidationException;
import com.godaddy.ans.sdk.model.IdentityChallengeResponse;
import com.godaddy.ans.sdk.model.IdentityDetails;
import com.godaddy.ans.sdk.model.IdentityLinkRequest;
import com.godaddy.ans.sdk.model.IdentityLinkResponse;
import com.godaddy.ans.sdk.model.IdentityListResponse;
import com.godaddy.ans.sdk.model.IdentityRegistrationRequest;
import com.godaddy.ans.sdk.model.VerifyControlRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link IdentityService}, the eight RA Verified-Identity
 * management operations.
 *
 * <p>Paths are pinned by {@link IdentityPathsTest}; here the focus is the
 * service behaviour: wire method + body, response parsing, the 202
 * challenge-parse guards, and the two pre-flight validators
 * ({@code verify-control} proof family and the {@code link} batch bound).</p>
 */
@WireMockTest
class IdentityServiceTest {

    private static final String IDENTITY_ID = "id-550e8400-e29b-41d4-a716-446655440000";
    private static final String AGENT_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String COLLECTION = "/v2/ans/identities";
    private static final String API_KEY = "test-api-key";
    private static final String API_SECRET = "test-api-secret";

    private IdentityService createIdentityService(WireMockRuntimeInfo wmRuntimeInfo) {
        AnsConfiguration config = AnsConfiguration.builder()
            .environment(Environment.OTE)
            .baseUrl(wmRuntimeInfo.getHttpBaseUrl())
            .credentialsProvider(new ApiKeyCredentialsProvider(API_KEY, API_SECRET))
            .build();
        return new IdentityService(new AnsApiClient(config));
    }

    private String challengeBody() {
        return """
            {
                "identityId": "%s",
                "kind": "did:web",
                "value": "did:web:example.com",
                "status": "PENDING_CONTROL",
                "nonce": "dGVzdC1ub25jZQ",
                "expiresAt": "2026-01-01T00:00:00Z",
                "challenges": []
            }
            """.formatted(IDENTITY_ID);
    }

    private String identityDetailsBody(String status) {
        return """
            {
                "identityId": "%s",
                "kind": "did:web",
                "value": "did:web:example.com",
                "status": "%s",
                "proofMethod": "did-web-sig",
                "createdAt": "2026-01-01T00:00:00Z",
                "linkedAgents": []
            }
            """.formatted(IDENTITY_ID, status);
    }

    // ==================== register ====================

    @Test
    @DisplayName("register POSTs to the collection and returns the 202 challenge")
    void registerReturnsChallenge(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody(challengeBody())));

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value("did:web:example.com");
        IdentityChallengeResponse challenge = createIdentityService(wmRuntimeInfo).register(request);

        assertThat(challenge.getIdentityId()).isEqualTo(IDENTITY_ID);
        assertThat(challenge.getNonce()).isEqualTo("dGVzdC1ub25jZQ");
        verify(postRequestedFor(urlEqualTo(COLLECTION))
            .withHeader("Authorization", containing("sso-key"))
            .withRequestBody(equalToJson("{\"value\":\"did:web:example.com\"}", true, true)));
    }

    @Test
    @DisplayName("register throws AnsServerException when the challenge omits identityId")
    void registerRejectsMissingIdentityId(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"nonce\": \"dGVzdC1ub25jZQ\"}")));

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value("did:web:example.com");

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).register(request))
            .isInstanceOf(AnsServerException.class)
            .hasMessageContaining("identityId");
    }

    @Test
    @DisplayName("register throws AnsServerException when the challenge omits nonce")
    void registerRejectsMissingNonce(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"identityId\": \"" + IDENTITY_ID + "\"}")));

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value("did:web:example.com");

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).register(request))
            .isInstanceOf(AnsServerException.class)
            .hasMessageContaining("nonce");
    }

    @Test
    @DisplayName("register throws AnsValidationException on 422")
    void registerThrowsValidationOn422(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION))
            .willReturn(aResponse()
                .withStatus(422)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\": \"unrecognized identifier form\"}")));

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value("not-an-identifier");

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).register(request))
            .isInstanceOf(AnsValidationException.class);
    }

    // ==================== list ====================

    @Test
    @DisplayName("list with no paging arguments GETs the bare collection")
    void listNoArguments(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get(urlEqualTo(COLLECTION))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {"items": [], "returnedCount": 0, "limit": 100, "hasMore": false}
                    """)));

        IdentityListResponse page = createIdentityService(wmRuntimeInfo).list(null, null);

        assertThat(page.getItems()).isEmpty();
        assertThat(page.getReturnedCount()).isZero();
        assertThat(page.getHasMore()).isFalse();
        verify(getRequestedFor(urlEqualTo(COLLECTION)));
    }

    @Test
    @DisplayName("list carries limit and URL-encoded cursor into the query string")
    void listWithLimitAndCursor(WireMockRuntimeInfo wmRuntimeInfo) {
        String path = COLLECTION + "?limit=25&cursor=next%2Fpage";
        stubFor(get(urlEqualTo(path))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {"items": [], "returnedCount": 0, "limit": 25, "nextCursor": null, "hasMore": true}
                    """)));

        IdentityListResponse page = createIdentityService(wmRuntimeInfo).list(25, "next/page");

        assertThat(page.getLimit()).isEqualTo(25);
        assertThat(page.getHasMore()).isTrue();
        verify(getRequestedFor(urlEqualTo(path)));
    }

    // ==================== getDetails ====================

    @Test
    @DisplayName("getDetails GETs the identity resource and returns its details")
    void getDetailsReturnsDetails(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get(urlEqualTo(COLLECTION + "/" + IDENTITY_ID))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(identityDetailsBody("VERIFIED"))));

        IdentityDetails details = createIdentityService(wmRuntimeInfo).getDetails(IDENTITY_ID);

        assertThat(details.getIdentityId()).isEqualTo(IDENTITY_ID);
        assertThat(details.getValue()).isEqualTo("did:web:example.com");
        verify(getRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID)));
    }

    @Test
    @DisplayName("getDetails throws AnsNotFoundException on 404")
    void getDetailsThrowsNotFoundOn404(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get(urlEqualTo(COLLECTION + "/" + IDENTITY_ID))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\": \"identity not found\"}")));

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).getDetails(IDENTITY_ID))
            .isInstanceOf(AnsNotFoundException.class);
    }

    // ==================== rotate ====================

    @Test
    @DisplayName("rotate PUTs to the identity resource and returns a fresh challenge")
    void rotateReturnsChallenge(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(put(urlEqualTo(COLLECTION + "/" + IDENTITY_ID))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody(challengeBody())));

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value("did:web:example.com");
        IdentityChallengeResponse challenge = createIdentityService(wmRuntimeInfo).rotate(IDENTITY_ID, request);

        assertThat(challenge.getIdentityId()).isEqualTo(IDENTITY_ID);
        verify(putRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID))
            .withRequestBody(equalToJson("{\"value\":\"did:web:example.com\"}", true, true)));
    }

    @Test
    @DisplayName("rotate applies the same challenge-parse guard as register")
    void rotateRejectsMissingNonce(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(put(urlEqualTo(COLLECTION + "/" + IDENTITY_ID))
            .willReturn(aResponse()
                .withStatus(202)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"identityId\": \"" + IDENTITY_ID + "\"}")));

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value("did:web:example.com");

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).rotate(IDENTITY_ID, request))
            .isInstanceOf(AnsServerException.class)
            .hasMessageContaining("nonce");
    }

    // ==================== verifyControl ====================

    @Test
    @DisplayName("verifyControl accepts a JWS-only request and POSTs to verify-control")
    void verifyControlWithJws(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/verify-control"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(identityDetailsBody("VERIFIED"))));

        VerifyControlRequest request = new VerifyControlRequest().addSignedProofsItem("eyJhbGciOiJFZERTQSJ9..sig");
        IdentityDetails details = createIdentityService(wmRuntimeInfo).verifyControl(IDENTITY_ID, request);

        assertThat(details.getStatus()).hasToString("VERIFIED");
        verify(postRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/verify-control")));
    }

    @Test
    @DisplayName("verifyControl accepts a CESR-only request")
    void verifyControlWithCesr(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/verify-control"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(identityDetailsBody("VERIFIED"))));

        VerifyControlRequest request = new VerifyControlRequest().signedProofs(null).cesrSignature("AABcesr...");
        IdentityDetails details = createIdentityService(wmRuntimeInfo).verifyControl(IDENTITY_ID, request);

        assertThat(details.getIdentityId()).isEqualTo(IDENTITY_ID);
    }

    @Test
    @DisplayName("verifyControl rejects a request carrying both proof families")
    void verifyControlRejectsBothFamilies(WireMockRuntimeInfo wmRuntimeInfo) {
        VerifyControlRequest request = new VerifyControlRequest()
            .addSignedProofsItem("eyJhbGciOiJFZERTQSJ9..sig")
            .cesrSignature("AABcesr...");

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).verifyControl(IDENTITY_ID, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exactly one proof family");
    }

    @Test
    @DisplayName("verifyControl rejects a request with neither proof family")
    void verifyControlRejectsNeitherFamily(WireMockRuntimeInfo wmRuntimeInfo) {
        IdentityService service = createIdentityService(wmRuntimeInfo);

        assertThatThrownBy(() -> service.verifyControl(IDENTITY_ID, new VerifyControlRequest()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exactly one proof family");
    }

    @Test
    @DisplayName("verifyControl treats an empty signedProofs list as absent")
    void verifyControlRejectsEmptySignedProofs(WireMockRuntimeInfo wmRuntimeInfo) {
        VerifyControlRequest request = new VerifyControlRequest().signedProofs(new ArrayList<>());

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).verifyControl(IDENTITY_ID, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exactly one proof family");
    }

    @Test
    @DisplayName("verifyControl treats a blank cesrSignature as absent")
    void verifyControlRejectsBlankCesr(WireMockRuntimeInfo wmRuntimeInfo) {
        VerifyControlRequest request = new VerifyControlRequest().cesrSignature("");

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).verifyControl(IDENTITY_ID, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("exactly one proof family");
    }

    // ==================== revoke ====================

    @Test
    @DisplayName("revoke POSTs an empty body to revoke and returns the updated details")
    void revokeReturnsDetails(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/revoke"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(identityDetailsBody("REVOKED"))));

        IdentityDetails details = createIdentityService(wmRuntimeInfo).revoke(IDENTITY_ID);

        assertThat(details.getStatus()).hasToString("REVOKED");
        verify(postRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/revoke")));
    }

    // ==================== link ====================

    @Test
    @DisplayName("link POSTs the batch to links and returns the linked count")
    void linkReturnsCount(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"linked\": 2}")));

        IdentityLinkRequest request = new IdentityLinkRequest()
            .addAgentIdsItem(UUID.fromString(AGENT_ID))
            .addAgentIdsItem(UUID.randomUUID());
        IdentityLinkResponse response = createIdentityService(wmRuntimeInfo).link(IDENTITY_ID, request);

        assertThat(response.getLinked()).isEqualTo(2);
        verify(postRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links")));
    }

    @Test
    @DisplayName("link rejects an empty batch")
    void linkRejectsEmptyBatch(WireMockRuntimeInfo wmRuntimeInfo) {
        IdentityLinkRequest request = new IdentityLinkRequest().agentIds(new ArrayList<>());

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).link(IDENTITY_ID, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("between 1 and 256");
    }

    @Test
    @DisplayName("link rejects a null agentIds list")
    void linkRejectsNullBatch(WireMockRuntimeInfo wmRuntimeInfo) {
        IdentityLinkRequest request = new IdentityLinkRequest();
        request.setAgentIds(null);

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).link(IDENTITY_ID, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("between 1 and 256");
    }

    @Test
    @DisplayName("link rejects a batch larger than 256 agents")
    void linkRejectsOversizedBatch(WireMockRuntimeInfo wmRuntimeInfo) {
        List<UUID> agentIds = new ArrayList<>();
        for (int i = 0; i < 257; i++) {
            agentIds.add(UUID.randomUUID());
        }
        IdentityLinkRequest request = new IdentityLinkRequest().agentIds(agentIds);

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).link(IDENTITY_ID, request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("got 257");
    }

    @Test
    @DisplayName("link accepts the single-agent lower boundary")
    void linkAcceptsSingleAgent(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"linked\": 1}")));

        IdentityLinkRequest request = new IdentityLinkRequest().addAgentIdsItem(UUID.fromString(AGENT_ID));
        IdentityLinkResponse response = createIdentityService(wmRuntimeInfo).link(IDENTITY_ID, request);

        assertThat(response.getLinked()).isEqualTo(1);
        verify(postRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links")));
    }

    @Test
    @DisplayName("link accepts the 256-agent boundary")
    void linkAcceptsMaxBatch(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(post(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"linked\": 256}")));

        List<UUID> agentIds = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            agentIds.add(UUID.randomUUID());
        }
        IdentityLinkRequest request = new IdentityLinkRequest().agentIds(agentIds);

        IdentityLinkResponse response = createIdentityService(wmRuntimeInfo).link(IDENTITY_ID, request);

        assertThat(response.getLinked()).isEqualTo(256);
    }

    // ==================== unlink ====================

    @Test
    @DisplayName("unlink DELETEs the single link and parses no body on 204")
    void unlinkDeletesLink(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(delete(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links/" + AGENT_ID))
            .willReturn(aResponse().withStatus(204)));

        createIdentityService(wmRuntimeInfo).unlink(IDENTITY_ID, AGENT_ID);

        verify(deleteRequestedFor(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links/" + AGENT_ID))
            .withHeader("Authorization", containing("sso-key")));
    }

    @Test
    @DisplayName("unlink surfaces AnsNotFoundException on 404")
    void unlinkThrowsNotFoundOn404(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(delete(urlEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links/" + AGENT_ID))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\": \"link not found\"}")));

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).unlink(IDENTITY_ID, AGENT_ID))
            .isInstanceOf(AnsNotFoundException.class);
    }

    // ==================== shared error mapping ====================

    @Test
    @DisplayName("a 401 from any operation surfaces as AnsAuthenticationException")
    void authenticationErrorSurfaces(WireMockRuntimeInfo wmRuntimeInfo) {
        stubFor(get(urlEqualTo(COLLECTION + "/" + IDENTITY_ID))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"message\": \"invalid credentials\"}")));

        assertThatThrownBy(() -> createIdentityService(wmRuntimeInfo).getDetails(IDENTITY_ID))
            .isInstanceOf(AnsAuthenticationException.class);
    }
}
