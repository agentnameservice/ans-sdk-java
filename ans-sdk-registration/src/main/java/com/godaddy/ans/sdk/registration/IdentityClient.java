package com.godaddy.ans.sdk.registration;

import com.godaddy.ans.sdk.auth.AnsCredentialsProvider;
import com.godaddy.ans.sdk.concurrent.AnsExecutors;
import com.godaddy.ans.sdk.config.AnsConfiguration;
import com.godaddy.ans.sdk.config.ApiVersion;
import com.godaddy.ans.sdk.config.Environment;
import com.godaddy.ans.sdk.model.IdentityChallengeResponse;
import com.godaddy.ans.sdk.model.IdentityDetails;
import com.godaddy.ans.sdk.model.IdentityLinkRequest;
import com.godaddy.ans.sdk.model.IdentityLinkResponse;
import com.godaddy.ans.sdk.model.IdentityListResponse;
import com.godaddy.ans.sdk.model.IdentityRegistrationRequest;
import com.godaddy.ans.sdk.model.VerifyControlRequest;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Client for ANS Verified-Identity management operations.
 *
 * <p>An identity is a first-class object with its own lifecycle, separate from
 * agent registration. This client covers the eight RA management operations on
 * the {@code /v2/ans/identities} surface: register, list, get details, rotate,
 * verify control, revoke, link to agents, and unlink.</p>
 *
 * <p>Register and rotate return a {@link IdentityChallengeResponse} (a 202 async
 * challenge round). The identity is not sealed on that response. The caller must
 * complete the challenge and then submit a control proof to verify-control.</p>
 *
 * <p>Example identity flow:</p>
 * <pre>{@code
 * IdentityClient client = IdentityClient.builder()
 *     .environment(Environment.OTE)
 *     .credentialsProvider(new JwtCredentialsProvider(jwtToken))
 *     .build();
 *
 * IdentityChallengeResponse challenge = client.registerIdentity(request);
 * // Complete the challenge, build the proof, then:
 * IdentityDetails identity = client.verifyControl(challenge.getIdentityId(), proofRequest);
 * }</pre>
 *
 * <p>Example link and unlink:</p>
 * <pre>{@code
 * IdentityLinkResponse linked = client.linkAgents(identityId,
 *     new IdentityLinkRequest().agentIds(List.of(agentId)));
 * client.unlinkAgent(identityId, agentId);
 * }</pre>
 */
public final class IdentityClient {

    private final AnsConfiguration configuration;
    private final IdentityService identityService;

    private IdentityClient(AnsConfiguration configuration, AnsApiClient ansApiClient) {
        this.configuration = configuration;
        this.identityService = new IdentityService(ansApiClient);
    }

    /**
     * Creates a new builder for constructing an IdentityClient.
     *
     * @return a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    // ==================== Identity Operations (Sync) ====================

    /**
     * Registers a new identity and returns the 202 challenge round.
     *
     * <p>The kind ({@code did:web}, {@code did:key}, or {@code lei}) is inferred
     * from the value. The identity is not sealed by this response. Complete the
     * challenge and submit a control proof to verify-control.</p>
     *
     * @param request the registration request
     * @return the challenge round to complete
     * @throws com.godaddy.ans.sdk.exception.AnsValidationException if the request is invalid
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     * @throws com.godaddy.ans.sdk.exception.AnsServerException if a server error occurs
     */
    public IdentityChallengeResponse registerIdentity(IdentityRegistrationRequest request) {
        return identityService.register(request);
    }

    /**
     * Lists the caller's identities, cursor-paginated.
     *
     * @param limit optional page size (1..100), or {@code null} for the server default
     * @param cursor optional opaque page cursor, or {@code null} for the first page
     * @return the page of identities plus the next cursor
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public IdentityListResponse listIdentities(Integer limit, String cursor) {
        return identityService.list(limit, cursor);
    }

    /**
     * Gets the full details for a single identity.
     *
     * @param identityId the identity ID
     * @return the identity details
     * @throws com.godaddy.ans.sdk.exception.AnsNotFoundException if the identity is not found
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public IdentityDetails getIdentity(String identityId) {
        return identityService.getDetails(identityId);
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
     * @throws com.godaddy.ans.sdk.exception.AnsNotFoundException if the identity is not found
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public IdentityChallengeResponse rotateIdentity(String identityId, IdentityRegistrationRequest request) {
        return identityService.rotate(identityId, request);
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
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public IdentityDetails verifyControl(String identityId, VerifyControlRequest request) {
        return identityService.verifyControl(identityId, request);
    }

    /**
     * Revokes an identity.
     *
     * @param identityId the identity ID to revoke
     * @return the updated identity details
     * @throws com.godaddy.ans.sdk.exception.AnsNotFoundException if the identity is not found
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public IdentityDetails revokeIdentity(String identityId) {
        return identityService.revoke(identityId);
    }

    /**
     * Links an identity to one or more agents as an all-or-nothing batch.
     *
     * @param identityId the identity ID
     * @param request the link request carrying 1..256 agent IDs
     * @return the link response with the count of linked agents
     * @throws IllegalArgumentException if the batch is empty or exceeds 256 agents
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public IdentityLinkResponse linkAgents(String identityId, IdentityLinkRequest request) {
        return identityService.link(identityId, request);
    }

    /**
     * Removes the link between an identity and a single agent.
     *
     * @param identityId the identity ID
     * @param agentId the linked agent ID to remove
     * @throws com.godaddy.ans.sdk.exception.AnsNotFoundException if the link is not found
     * @throws com.godaddy.ans.sdk.exception.AnsAuthenticationException if authentication fails
     */
    public void unlinkAgent(String identityId, String agentId) {
        identityService.unlink(identityId, agentId);
    }

    // ==================== Identity Operations (Async) ====================

    /**
     * Registers a new identity asynchronously.
     *
     * @param request the registration request
     * @return a CompletableFuture with the challenge round
     */
    public CompletableFuture<IdentityChallengeResponse> registerIdentityAsync(IdentityRegistrationRequest request) {
        return CompletableFuture.supplyAsync(() -> registerIdentity(request), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Lists the caller's identities asynchronously.
     *
     * @param limit optional page size (1..100), or {@code null} for the server default
     * @param cursor optional opaque page cursor, or {@code null} for the first page
     * @return a CompletableFuture with the page of identities
     */
    public CompletableFuture<IdentityListResponse> listIdentitiesAsync(Integer limit, String cursor) {
        return CompletableFuture.supplyAsync(() -> listIdentities(limit, cursor), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Gets the full details for a single identity asynchronously.
     *
     * @param identityId the identity ID
     * @return a CompletableFuture with the identity details
     */
    public CompletableFuture<IdentityDetails> getIdentityAsync(String identityId) {
        return CompletableFuture.supplyAsync(() -> getIdentity(identityId), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Rotates the key material for an identity asynchronously.
     *
     * @param identityId the identity ID to rotate
     * @param request the rotation request
     * @return a CompletableFuture with the new challenge round
     */
    public CompletableFuture<IdentityChallengeResponse> rotateIdentityAsync(String identityId,
                                                                            IdentityRegistrationRequest request) {
        return CompletableFuture.supplyAsync(
            () -> rotateIdentity(identityId, request), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Submits a control proof for an identity asynchronously.
     *
     * @param identityId the identity ID
     * @param request the control-proof request
     * @return a CompletableFuture with the updated identity details
     */
    public CompletableFuture<IdentityDetails> verifyControlAsync(String identityId, VerifyControlRequest request) {
        return CompletableFuture.supplyAsync(() -> verifyControl(identityId, request), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Revokes an identity asynchronously.
     *
     * @param identityId the identity ID to revoke
     * @return a CompletableFuture with the updated identity details
     */
    public CompletableFuture<IdentityDetails> revokeIdentityAsync(String identityId) {
        return CompletableFuture.supplyAsync(() -> revokeIdentity(identityId), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Links an identity to one or more agents asynchronously.
     *
     * @param identityId the identity ID
     * @param request the link request carrying 1..256 agent IDs
     * @return a CompletableFuture with the link response
     */
    public CompletableFuture<IdentityLinkResponse> linkAgentsAsync(String identityId, IdentityLinkRequest request) {
        return CompletableFuture.supplyAsync(() -> linkAgents(identityId, request), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Removes the link between an identity and a single agent asynchronously.
     *
     * @param identityId the identity ID
     * @param agentId the linked agent ID to remove
     * @return a CompletableFuture that completes when the link is removed
     */
    public CompletableFuture<Void> unlinkAgentAsync(String identityId, String agentId) {
        return CompletableFuture.runAsync(() -> unlinkAgent(identityId, agentId), AnsExecutors.sharedIoExecutor());
    }

    /**
     * Returns the current configuration.
     *
     * @return the configuration
     */
    public AnsConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Builder for constructing an IdentityClient.
     */
    public static final class Builder {

        private final AnsConfiguration.Builder configBuilder = AnsConfiguration.builder();
        private AnsConfiguration prebuiltConfiguration;

        private Builder() {
        }

        /**
         * Uses a pre-built configuration directly.
         *
         * <p>When set, this configuration is used as-is and any values set via
         * other builder methods are ignored.</p>
         *
         * @param configuration the pre-built configuration
         * @return this builder
         */
        public Builder configuration(AnsConfiguration configuration) {
            this.prebuiltConfiguration = configuration;
            return this;
        }

        /**
         * Sets the environment.
         *
         * @param environment the environment
         * @return this builder
         */
        public Builder environment(Environment environment) {
            configBuilder.environment(environment);
            return this;
        }

        /**
         * Sets a custom base URL.
         *
         * @param baseUrl the base URL
         * @return this builder
         */
        public Builder baseUrl(String baseUrl) {
            configBuilder.baseUrl(baseUrl);
            return this;
        }

        /**
         * Sets the credentials provider.
         *
         * @param credentialsProvider the credentials provider
         * @return this builder
         */
        public Builder credentialsProvider(AnsCredentialsProvider credentialsProvider) {
            configBuilder.credentialsProvider(credentialsProvider);
            return this;
        }

        /**
         * Sets the connection timeout.
         *
         * @param timeout the connection timeout
         * @return this builder
         */
        public Builder connectTimeout(Duration timeout) {
            configBuilder.connectTimeout(timeout);
            return this;
        }

        /**
         * Sets the read timeout.
         *
         * @param timeout the read timeout
         * @return this builder
         */
        public Builder readTimeout(Duration timeout) {
            configBuilder.readTimeout(timeout);
            return this;
        }

        /**
         * Enables retry with the specified maximum number of attempts.
         *
         * @param maxRetries the maximum number of retry attempts
         * @return this builder
         */
        public Builder enableRetry(int maxRetries) {
            configBuilder.enableRetry(maxRetries);
            return this;
        }

        /**
         * Sets the API version lane. Defaults to {@link ApiVersion#V2}.
         *
         * @param apiVersion the API version
         * @return this builder
         */
        public Builder apiVersion(ApiVersion apiVersion) {
            configBuilder.apiVersion(apiVersion);
            return this;
        }

        /**
         * Builds the IdentityClient.
         *
         * @return a new IdentityClient instance
         */
        public IdentityClient build() {
            AnsConfiguration config = (prebuiltConfiguration != null)
                ? prebuiltConfiguration
                : configBuilder.build();
            return new IdentityClient(config, new AnsApiClient(config));
        }
    }
}
