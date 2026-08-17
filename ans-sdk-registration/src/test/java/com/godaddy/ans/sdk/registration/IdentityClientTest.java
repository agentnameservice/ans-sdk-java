package com.godaddy.ans.sdk.registration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.godaddy.ans.sdk.auth.ApiKeyCredentialsProvider;
import com.godaddy.ans.sdk.config.AnsConfiguration;
import com.godaddy.ans.sdk.config.ApiVersion;
import com.godaddy.ans.sdk.config.Environment;
import com.godaddy.ans.sdk.exception.AnsAuthenticationException;
import com.godaddy.ans.sdk.exception.AnsConflictException;
import com.godaddy.ans.sdk.exception.AnsNotFoundException;
import com.godaddy.ans.sdk.exception.AnsServerException;
import com.godaddy.ans.sdk.model.IdentityChallengeResponse;
import com.godaddy.ans.sdk.model.IdentityDetails;
import com.godaddy.ans.sdk.model.IdentityLifecycleStatus;
import com.godaddy.ans.sdk.model.IdentityLinkRequest;
import com.godaddy.ans.sdk.model.IdentityLinkResponse;
import com.godaddy.ans.sdk.model.IdentityListResponse;
import com.godaddy.ans.sdk.model.IdentityProofChallenge;
import com.godaddy.ans.sdk.model.IdentityRegistrationRequest;
import com.godaddy.ans.sdk.model.VLEIPresentation;
import com.godaddy.ans.sdk.model.VerifyControlRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WireMockTest
class IdentityClientTest {

    private static final String TEST_IDENTITY_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String TEST_AGENT_ID = "660e8400-e29b-41d4-a716-446655440111";
    private static final String TEST_OTHER_AGENT_ID = "770e8400-e29b-41d4-a716-446655440222";
    private static final String TEST_API_KEY = "123e4567-e89b-12d3-a456-426614174000";
    private static final String TEST_API_KEY_SECRET = "123e4567-e89b-12d3-a456-426614174000";
    private static final String TEST_NONCE = "abc123";
    private static final String DID_WEB_IDENTITY = "did:web:identity.acme-corp.com";

    // ==================== Builder Tests ====================

    @Test
    @DisplayName("Should build client with environment")
    void shouldBuildClientWithEnvironment() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        assertThat(client).isNotNull();
        assertThat(client.getConfiguration().getEnvironment()).isEqualTo(Environment.OTE);
        assertThat(client.getConfiguration().getBaseUrl()).isEqualTo("https://api.ote-godaddy.com");
    }

    @Test
    @DisplayName("Should build client with custom base URL")
    void shouldBuildClientWithCustomBaseUrl(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        IdentityClient client = IdentityClient.builder()
                .baseUrl(baseUrl)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        assertThat(client).isNotNull();
        assertThat(client.getConfiguration().getBaseUrl()).isEqualTo(baseUrl);
    }

    @Test
    @DisplayName("Should build client with custom timeouts")
    void shouldBuildClientWithCustomTimeouts() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(15))
                .build();

        assertThat(client.getConfiguration().getConnectTimeout()).isEqualTo(Duration.ofSeconds(5));
        assertThat(client.getConfiguration().getReadTimeout()).isEqualTo(Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Should build client with retry enabled")
    void shouldBuildClientWithRetryEnabled() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .enableRetry(5)
                .build();

        assertThat(client.getConfiguration().isRetryEnabled()).isTrue();
        assertThat(client.getConfiguration().getMaxRetries()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should use a pre-built configuration as-is")
    void shouldBuildClientWithPrebuiltConfiguration() {
        AnsConfiguration prebuilt = AnsConfiguration.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .apiVersion(ApiVersion.V1)
                .build();

        IdentityClient client = IdentityClient.builder()
                .baseUrl("https://ignored.example.com")
                .configuration(prebuilt)
                .build();

        // The pre-built configuration wins; the builder's own baseUrl is ignored.
        assertThat(client.getConfiguration()).isSameAs(prebuilt);
        assertThat(client.getConfiguration().getApiVersion()).isEqualTo(ApiVersion.V1);
    }

    @Test
    @DisplayName("Should build client with a custom API version lane")
    void shouldBuildClientWithApiVersion() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .apiVersion(ApiVersion.V1)
                .build();

        assertThat(client.getConfiguration().getApiVersion()).isEqualTo(ApiVersion.V1);
    }

    @Test
    @DisplayName("Should throw exception when credentials provider is null")
    void shouldThrowExceptionWhenCredentialsProviderIsNull() {
        assertThatThrownBy(() -> IdentityClient.builder()
                .environment(Environment.OTE)
                .build())
                .isInstanceOf(NullPointerException.class);
    }

    // ==================== Identity Registration Tests ====================

    @Test
    @DisplayName("Should register Identity successfully")
    void shouldRegisterIdentitySuccessfully(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        // Stub the initial registration POST
        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityRegistrationResponse())));

        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .baseUrl(baseUrl)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        IdentityRegistrationRequest request = new IdentityRegistrationRequest()
                .value(TEST_IDENTITY_ID);

        IdentityChallengeResponse result = client.registerIdentity(request);

        assertThat(result).isNotNull();
        assertThat(result.getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
        assertThat(result.getNonce()).isEqualTo(TEST_NONCE);
        assertThat(result.getExpiresAt()).isEqualTo("2024-01-15T12:00:00Z");
        assertThat(result.getValue()).isEqualTo(DID_WEB_IDENTITY);
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.PENDING_CONTROL);
        IdentityProofChallenge identityProofChallenge = result.getChallenges().get(0);
        assertThat(identityProofChallenge.getKid()).isEqualTo("#key-1");
        assertThat(identityProofChallenge.getSigningInput()).isEqualTo("abc123");

        verify(postRequestedFor(urlEqualTo("/v2/ans/identities"))
                .withRequestBody(containing("\"value\":\"" + TEST_IDENTITY_ID + "\""))
                .withHeader("Authorization", equalTo("sso-key " + TEST_API_KEY + ":" + TEST_API_KEY_SECRET)));
    }

    @Test
    @DisplayName("Should throw AnsServerException when challenge response missing identityId")
    void shouldThrowWhenChallengeMissingIdentityId(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"nonce\":\"abc123\"}")));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.registerIdentity(new IdentityRegistrationRequest().value(TEST_IDENTITY_ID)))
                .isInstanceOf(AnsServerException.class)
                .hasMessageContaining("missing 'identityId'");
    }

    @Test
    @DisplayName("Should throw AnsServerException when challenge response missing nonce")
    void shouldThrowWhenChallengeMissingNonce(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "identityId": "%s",
                                "kind": "did:web",
                                "value": "did:web:example.com",
                                "status": "PENDING_CONTROL",
                                "expiresAt": "2026-01-01T00:00:00Z",
                                "challenges": []
                            }
                            """.formatted(TEST_IDENTITY_ID))));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.registerIdentity(new IdentityRegistrationRequest().value(TEST_IDENTITY_ID)))
                .isInstanceOf(AnsServerException.class)
                .hasMessageContaining("missing 'nonce'");
    }

    @Test
    @DisplayName("Should register a lei identity carrying a vLEI/CESR presentation")
    void shouldRegisterLeiIdentityWithVleiPresentation(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityRegistrationResponse())));

        IdentityClient client = client(baseUrl);

        String cesrBytes = "-VAj-AABAAA-transport-only-cesr";
        IdentityRegistrationRequest request = new IdentityRegistrationRequest()
                .value("5493001KJTIIGC8Y1R12")
                .vleiPresentation(new VLEIPresentation().cesr(cesrBytes));

        IdentityChallengeResponse result = client.registerIdentity(request);

        assertThat(result).isNotNull();

        verify(postRequestedFor(urlEqualTo("/v2/ans/identities"))
                .withRequestBody(containing("\"vleiPresentation\""))
                .withRequestBody(containing("\"cesr\":\"" + cesrBytes + "\""))
                .withHeader("Authorization", equalTo("sso-key " + TEST_API_KEY + ":" + TEST_API_KEY_SECRET)));
    }

    @Test
    @DisplayName("Should surface AnsConflictException carrying IDENTIFIER_DUPLICATE on 409")
    void shouldSurfaceConflictOnDuplicateIdentifier(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(409)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"error\",\"code\":\"IDENTIFIER_DUPLICATE\","
                                + "\"message\":\"identifier already registered\"}")));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.registerIdentity(new IdentityRegistrationRequest().value(DID_WEB_IDENTITY)))
                .isInstanceOf(AnsConflictException.class)
                .hasMessageContaining("IDENTIFIER_DUPLICATE");
    }

    @Test
    @DisplayName("Should surface a retryable AnsServerException carrying TL_UNAVAILABLE on 503")
    void shouldSurfaceServerErrorOnTransparencyLogUnavailable(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"error\",\"code\":\"TL_UNAVAILABLE\","
                                + "\"message\":\"transparency log unavailable\"}")));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.registerIdentity(new IdentityRegistrationRequest().value(DID_WEB_IDENTITY)))
                .isInstanceOf(AnsServerException.class)
                .hasMessageContaining("TL_UNAVAILABLE")
                .satisfies(e -> assertThat(((AnsServerException) e).isRetryable()).isTrue());
    }

    // ==================== List Identities Tests ====================

    @Test
    @DisplayName("Should list identities with limit and cursor")
    void shouldListIdentitiesWithLimitAndCursor(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities?limit=10&cursor=page-2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityListResponse())));

        IdentityClient client = client(baseUrl);

        IdentityListResponse result = client.listIdentities(10, "page-2");

        assertThat(result).isNotNull();
        assertThat(result.getReturnedCount()).isEqualTo(1);
        assertThat(result.getLimit()).isEqualTo(10);
        assertThat(result.getNextCursor()).isEqualTo("page-3");
        assertThat(result.getHasMore()).isTrue();
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
    }

    @Test
    @DisplayName("Should list identities with server defaults when limit and cursor are null")
    void shouldListIdentitiesWithDefaults(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityListResponse())));

        IdentityClient client = client(baseUrl);

        IdentityListResponse result = client.listIdentities(null, null);

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw AnsAuthenticationException on 401 for listIdentities")
    void shouldThrowAuthExceptionForListIdentities(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"error\",\"code\":\"UNAUTHORIZED\",\"message\":\"Invalid key\"}")));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.listIdentities(null, null))
                .isInstanceOf(AnsAuthenticationException.class)
                .hasMessageContaining("Authentication failed");
    }

    // ==================== Get Identity Tests ====================

    @Test
    @DisplayName("Should get identity by ID successfully")
    void shouldGetIdentitySuccessfully(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.VERIFIED))));

        IdentityClient client = client(baseUrl);

        IdentityDetails result = client.getIdentity(TEST_IDENTITY_ID);

        assertThat(result).isNotNull();
        assertThat(result.getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
        assertThat(result.getKind()).isEqualTo(IdentityDetails.KindEnum.DID_WEB);
        assertThat(result.getValue()).isEqualTo(DID_WEB_IDENTITY);
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.VERIFIED);
        assertThat(result.getLinkedAgents()).hasSize(1);
    }

    @Test
    @DisplayName("Should throw AnsNotFoundException when identity not found")
    void shouldThrowNotFoundExceptionWhenIdentityNotFound(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"error\",\"code\":\"NOT_FOUND\",\"message\":\"Identity not found\"}")));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.getIdentity(TEST_IDENTITY_ID))
                .isInstanceOf(AnsNotFoundException.class)
                .hasMessageContaining("not found");
    }

    // ==================== Rotate Identity Tests ====================

    @Test
    @DisplayName("Should rotate identity and return a fresh challenge")
    void shouldRotateIdentitySuccessfully(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(put(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityRegistrationResponse())));

        IdentityClient client = client(baseUrl);

        IdentityRegistrationRequest request = new IdentityRegistrationRequest().value(DID_WEB_IDENTITY);

        IdentityChallengeResponse result = client.rotateIdentity(TEST_IDENTITY_ID, request);

        assertThat(result).isNotNull();
        assertThat(result.getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
        assertThat(result.getNonce()).isEqualTo(TEST_NONCE);

        verify(putRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID))
                .withRequestBody(containing("\"value\":\"" + DID_WEB_IDENTITY + "\""))
                .withHeader("Authorization", equalTo("sso-key " + TEST_API_KEY + ":" + TEST_API_KEY_SECRET)));
    }

    // ==================== Verify Control Tests ====================

    @Test
    @DisplayName("Should verify control with signed JWS proofs")
    void shouldVerifyControlWithSignedProofs(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/verify-control"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.VERIFIED))));

        IdentityClient client = client(baseUrl);

        VerifyControlRequest request = new VerifyControlRequest()
                .signedProofs(List.of("eyJhbGciOiJFZERTQSJ9.payload.sig"));

        IdentityDetails result = client.verifyControl(TEST_IDENTITY_ID, request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.VERIFIED);

        verify(postRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/verify-control"))
                .withRequestBody(containing("\"signedProofs\"")));
    }

    @Test
    @DisplayName("Should verify control with a CESR signature")
    void shouldVerifyControlWithCesrSignature(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/verify-control"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.VERIFIED))));

        IdentityClient client = client(baseUrl);

        VerifyControlRequest request = new VerifyControlRequest().cesrSignature("AABxyzsignature");

        IdentityDetails result = client.verifyControl(TEST_IDENTITY_ID, request);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.VERIFIED);

        verify(postRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/verify-control"))
                .withRequestBody(containing("\"cesrSignature\":\"AABxyzsignature\"")));
    }

    @Test
    @DisplayName("Should reject verify control carrying both proof families")
    void shouldRejectVerifyControlWithBothProofFamilies() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        VerifyControlRequest request = new VerifyControlRequest()
                .signedProofs(List.of("jws-proof"))
                .cesrSignature("cesr-sig");

        assertThatThrownBy(() -> client.verifyControl(TEST_IDENTITY_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly one proof family");
    }

    @Test
    @DisplayName("Should reject verify control carrying no proof family")
    void shouldRejectVerifyControlWithNoProofFamily() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        assertThatThrownBy(() -> client.verifyControl(TEST_IDENTITY_ID, new VerifyControlRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("exactly one proof family");
    }

    // ==================== Revoke Identity Tests ====================

    @Test
    @DisplayName("Should revoke identity successfully")
    void shouldRevokeIdentitySuccessfully(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/revoke"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.REVOKED))));

        IdentityClient client = client(baseUrl);

        IdentityDetails result = client.revokeIdentity(TEST_IDENTITY_ID);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.REVOKED);

        verify(postRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/revoke")));
    }

    // ==================== Link Agents Tests ====================

    @Test
    @DisplayName("Should link agents successfully")
    void shouldLinkAgentsSuccessfully(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"linked\":2}")));

        IdentityClient client = client(baseUrl);

        IdentityLinkRequest request = new IdentityLinkRequest()
                .agentIds(List.of(UUID.fromString(TEST_AGENT_ID), UUID.fromString(TEST_OTHER_AGENT_ID)));

        IdentityLinkResponse result = client.linkAgents(TEST_IDENTITY_ID, request);

        assertThat(result).isNotNull();
        assertThat(result.getLinked()).isEqualTo(2);

        verify(postRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links"))
                .withRequestBody(containing(TEST_AGENT_ID))
                .withHeader("Authorization", equalTo("sso-key " + TEST_API_KEY + ":" + TEST_API_KEY_SECRET)));
    }

    @Test
    @DisplayName("Should reject link request with an empty agent batch")
    void shouldRejectEmptyLinkBatch() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        assertThatThrownBy(() -> client.linkAgents(TEST_IDENTITY_ID, new IdentityLinkRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 1 and 256");
    }

    @Test
    @DisplayName("Should reject link request exceeding the 256 agent batch limit")
    void shouldRejectOversizedLinkBatch() {
        IdentityClient client = IdentityClient.builder()
                .environment(Environment.OTE)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();

        List<UUID> tooMany = java.util.stream.Stream.generate(UUID::randomUUID)
                .limit(257)
                .toList();

        assertThatThrownBy(() -> client.linkAgents(TEST_IDENTITY_ID, new IdentityLinkRequest().agentIds(tooMany)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("between 1 and 256");
    }

    // ==================== Unlink Agent Tests ====================

    @Test
    @DisplayName("Should unlink an agent successfully")
    void shouldUnlinkAgentSuccessfully(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(delete(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links/" + TEST_AGENT_ID))
                .willReturn(aResponse().withStatus(204)));

        IdentityClient client = client(baseUrl);

        client.unlinkAgent(TEST_IDENTITY_ID, TEST_AGENT_ID);

        verify(deleteRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links/" + TEST_AGENT_ID))
                .withHeader("Authorization", equalTo("sso-key " + TEST_API_KEY + ":" + TEST_API_KEY_SECRET)));
    }

    @Test
    @DisplayName("Should throw AnsNotFoundException when unlinking a missing link")
    void shouldThrowNotFoundExceptionForUnlink(WireMockRuntimeInfo wmRuntimeInfo) {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(delete(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links/" + TEST_AGENT_ID))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"error\",\"code\":\"NOT_FOUND\",\"message\":\"Link not found\"}")));

        IdentityClient client = client(baseUrl);

        assertThatThrownBy(() -> client.unlinkAgent(TEST_IDENTITY_ID, TEST_AGENT_ID))
                .isInstanceOf(AnsNotFoundException.class)
                .hasMessageContaining("not found");
    }

    // ==================== Async Tests ====================

    @Test
    @DisplayName("Should register identity asynchronously")
    void shouldRegisterIdentityAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityRegistrationResponse())));

        IdentityClient client = client(baseUrl);

        IdentityChallengeResponse result = client.registerIdentityAsync(
                new IdentityRegistrationRequest().value(TEST_IDENTITY_ID)).get();

        assertThat(result).isNotNull();
        assertThat(result.getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
    }

    @Test
    @DisplayName("Should list identities asynchronously")
    void shouldListIdentitiesAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityListResponse())));

        IdentityClient client = client(baseUrl);

        IdentityListResponse result = client.listIdentitiesAsync(null, null).get();

        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
    }

    @Test
    @DisplayName("Should get identity asynchronously")
    void shouldGetIdentityAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(get(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.VERIFIED))));

        IdentityClient client = client(baseUrl);

        IdentityDetails result = client.getIdentityAsync(TEST_IDENTITY_ID).get();

        assertThat(result).isNotNull();
        assertThat(result.getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
    }

    @Test
    @DisplayName("Should rotate identity asynchronously")
    void shouldRotateIdentityAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(put(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID))
                .willReturn(aResponse()
                        .withStatus(202)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityRegistrationResponse())));

        IdentityClient client = client(baseUrl);

        IdentityChallengeResponse result = client.rotateIdentityAsync(TEST_IDENTITY_ID,
                new IdentityRegistrationRequest().value(DID_WEB_IDENTITY)).get();

        assertThat(result).isNotNull();
        assertThat(result.getIdentityId()).isEqualTo(TEST_IDENTITY_ID);
    }

    @Test
    @DisplayName("Should verify control asynchronously")
    void shouldVerifyControlAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/verify-control"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.VERIFIED))));

        IdentityClient client = client(baseUrl);

        IdentityDetails result = client.verifyControlAsync(TEST_IDENTITY_ID,
                new VerifyControlRequest().cesrSignature("AABsig")).get();

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.VERIFIED);
    }

    @Test
    @DisplayName("Should revoke identity asynchronously")
    void shouldRevokeIdentityAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/revoke"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(identityDetailsResponse(IdentityLifecycleStatus.REVOKED))));

        IdentityClient client = client(baseUrl);

        IdentityDetails result = client.revokeIdentityAsync(TEST_IDENTITY_ID).get();

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(IdentityLifecycleStatus.REVOKED);
    }

    @Test
    @DisplayName("Should link agents asynchronously")
    void shouldLinkAgentsAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(post(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"linked\":1}")));

        IdentityClient client = client(baseUrl);

        IdentityLinkResponse result = client.linkAgentsAsync(TEST_IDENTITY_ID,
                new IdentityLinkRequest().agentIds(List.of(UUID.fromString(TEST_AGENT_ID)))).get();

        assertThat(result).isNotNull();
        assertThat(result.getLinked()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should unlink an agent asynchronously")
    void shouldUnlinkAgentAsync(WireMockRuntimeInfo wmRuntimeInfo) throws Exception {
        String baseUrl = wmRuntimeInfo.getHttpBaseUrl();

        stubFor(delete(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links/" + TEST_AGENT_ID))
                .willReturn(aResponse().withStatus(204)));

        IdentityClient client = client(baseUrl);

        client.unlinkAgentAsync(TEST_IDENTITY_ID, TEST_AGENT_ID).get();

        verify(deleteRequestedFor(urlEqualTo("/v2/ans/identities/" + TEST_IDENTITY_ID + "/links/" + TEST_AGENT_ID)));
    }

    // ==================== Helper Methods ====================

    private IdentityClient client(String baseUrl) {
        return IdentityClient.builder()
                .environment(Environment.OTE)
                .baseUrl(baseUrl)
                .credentialsProvider(new ApiKeyCredentialsProvider(TEST_API_KEY, TEST_API_KEY_SECRET))
                .build();
    }

    private String identityListResponse() {
        return """
                {
                    "items": [
                        {
                            "identityId": "550e8400-e29b-41d4-a716-446655440000",
                            "kind": "did:web",
                            "value": "did:web:identity.acme-corp.com",
                            "status": "VERIFIED",
                            "proofMethod": "did-web-sig",
                            "verifiedAt": "2024-01-15T12:00:00Z",
                            "createdAt": "2024-01-15T10:00:00Z"
                        }
                    ],
                    "returnedCount": 1,
                    "limit": 10,
                    "nextCursor": "page-3",
                    "hasMore": true
                }
                """;
    }

    private String identityDetailsResponse(IdentityLifecycleStatus status) {
        return String.format("""
                {
                    "identityId": "550e8400-e29b-41d4-a716-446655440000",
                    "kind": "did:web",
                    "value": "did:web:identity.acme-corp.com",
                    "status": "%s",
                    "proofMethod": "did-web-sig",
                    "verifiedAt": "2024-01-15T12:00:00Z",
                    "createdAt": "2024-01-15T10:00:00Z",
                    "linkedAgents": [
                        {
                            "agentId": "660e8400-e29b-41d4-a716-446655440111",
                            "linkedAt": "2024-01-15T11:00:00Z"
                        }
                    ]
                }
                """, status.getValue());
    }

    private String identityRegistrationResponse() {
        return """
                {
                    "identityId": "550e8400-e29b-41d4-a716-446655440000",
                    "kind": "did:web",
                    "value": "did:web:identity.acme-corp.com",
                    "status": "PENDING_CONTROL",
                    "nonce": "abc123",
                    "expiresAt": "2024-01-15T12:00:00Z",
                    "challenges": [
                        {
                            "kid": "#key-1",
                            "signingInput": "abc123"
                        }
                    ]
                }
                """;
    }
}