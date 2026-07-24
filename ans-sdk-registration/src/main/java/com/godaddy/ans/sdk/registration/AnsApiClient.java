package com.godaddy.ans.sdk.registration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.godaddy.ans.sdk.auth.AnsCredentials;
import com.godaddy.ans.sdk.config.AnsConfiguration;
import com.godaddy.ans.sdk.config.ApiVersion;
import com.godaddy.ans.sdk.exception.AnsAuthenticationException;
import com.godaddy.ans.sdk.exception.AnsConflictException;
import com.godaddy.ans.sdk.exception.AnsNotFoundException;
import com.godaddy.ans.sdk.exception.AnsServerException;
import com.godaddy.ans.sdk.exception.AnsValidationException;
import com.godaddy.ans.sdk.http.HttpClientFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * API client helper for making authenticated requests to the ANS Registry API.
 *
 * <p>This class provides common HTTP functionality used by SDK services,
 * including request building, error handling, and JSON serialization.</p>
 */
class AnsApiClient {

    private final AnsConfiguration configuration;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    AnsApiClient(AnsConfiguration configuration) {
        this.configuration = configuration;
        this.httpClient = HttpClientFactory.create(configuration);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Creates an HTTP request builder with common headers (Authorization, Content-Type, Accept).
     *
     * @param path the API path (e.g., "/v1/agents/register")
     * @return a configured HttpRequest.Builder
     */
    HttpRequest.Builder createRequestBuilder(String path) {
        AnsCredentials credentials = configuration.getCredentialsProvider().resolveCredentials();

        return HttpRequest.newBuilder()
            .uri(URI.create(configuration.getBaseUrl() + path))
            .header("Authorization", credentials.toAuthorizationHeader())
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .timeout(configuration.getReadTimeout());
    }

    /**
     * Sends an HTTP request and handles common error responses.
     *
     * @param request the HTTP request to send
     * @return the HTTP response
     * @throws AnsServerException if a network error or interruption occurs
     */
    HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            handleErrorResponse(response);
            return response;
        } catch (IOException e) {
            throw new AnsServerException("Network error: " + e.getMessage(), 0, e, null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AnsServerException("Request interrupted", 0, e, null);
        }
    }

    /**
     * Handles error responses from the API by throwing appropriate exceptions.
     *
     * @param response the HTTP response to check
     * @throws AnsAuthenticationException for 401/403 responses
     * @throws AnsNotFoundException for 404 responses
     * @throws AnsConflictException for 409 responses
     * @throws AnsValidationException for 422 responses
     * @throws AnsServerException for 5xx and other unexpected responses
     */
    void handleErrorResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();

        if (statusCode >= 200 && statusCode < 300) {
            return; // Success
        }

        String requestId = response.headers().firstValue("X-Request-Id").orElse(null);
        String body = response.body();

        switch (statusCode) {
            case 401, 403 -> throw new AnsAuthenticationException(
                "Authentication failed: " + body, null, requestId);
            case 404 -> throw new AnsNotFoundException(
                "Resource not found: " + body, null, null, requestId);
            case 409 -> throw new AnsConflictException(
                "Conflict: " + body, requestId);
            case 422 -> throw new AnsValidationException(
                "Validation error: " + body, null, requestId);
            default -> {
                if (statusCode >= 500) {
                    throw new AnsServerException(
                        "Server error: " + body, statusCode, requestId);
                }
                throw new AnsServerException(
                    "Unexpected error (" + statusCode + "): " + body, statusCode, requestId);
            }
        }
    }

    /**
     * Parses a JSON response body into the specified type.
     *
     * @param responseBody the JSON response body
     * @param clazz the class to deserialize into
     * @param <T> the type to return
     * @return the deserialized object
     * @throws AnsServerException if parsing fails
     */
    <T> T parseResponse(String responseBody, Class<T> clazz) {
        try {
            return objectMapper.readValue(responseBody, clazz);
        } catch (IOException e) {
            throw new AnsServerException("Failed to parse response: " + e.getMessage(), 0, e, null);
        }
    }

    /**
     * Parses a JSON response body into the specified generic type.
     *
     * @param responseBody the JSON response body
     * @param typeReference the type reference for generic types
     * @param <T> the type to return
     * @return the deserialized object
     * @throws AnsServerException if parsing fails
     */
    <T> T parseResponse(String responseBody, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(responseBody, typeReference);
        } catch (IOException e) {
            throw new AnsServerException("Failed to parse response: " + e.getMessage(), 0, e, null);
        }
    }

    /**
     * Serializes an object to JSON.
     *
     * @param object the object to serialize
     * @return the JSON string
     * @throws AnsServerException if serialization fails
     */
    String serializeToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new AnsServerException("Failed to serialize request: " + e.getMessage(), 0, e, null);
        }
    }

    /**
     * Serializes an object to JSON with the named top-level field removed.
     *
     * <p>Used to strip v2-only fields (e.g. {@code discoveryProfiles}) from the
     * wire body when targeting the v1 lane, without mutating the caller's object.
     * Operates on the JSON tree and reuses this client's {@link ObjectMapper}.</p>
     *
     * @param object the object to serialize
     * @param fieldName the top-level field to remove
     * @return the JSON string without the named field
     * @throws AnsServerException if serialization fails
     */
    String serializeToJsonWithoutField(Object object, String fieldName) {
        try {
            JsonNode tree = objectMapper.valueToTree(object);
            if (tree instanceof ObjectNode objectNode) {
                objectNode.remove(fieldName);
            }

            return objectMapper.writeValueAsString(tree);
        } catch (IOException e) {
            throw new AnsServerException("Failed to serialize request: " + e.getMessage(), 0, e, null);
        }
    }

    ApiVersion getApiVersion() {
        return configuration.getApiVersion();
    }
}
