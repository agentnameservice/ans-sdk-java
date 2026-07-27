package com.godaddy.ans.sdk.registration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godaddy.ans.sdk.auth.ApiKeyCredentialsProvider;
import com.godaddy.ans.sdk.config.AnsConfiguration;
import com.godaddy.ans.sdk.config.Environment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link AnsApiClient} JSON serialization helpers.
 */
class AnsApiClientTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private AnsApiClient client() {
        AnsConfiguration config = AnsConfiguration.builder()
            .environment(Environment.OTE)
            .credentialsProvider(new ApiKeyCredentialsProvider("test-api-key", "test-api-secret"))
            .build();
        return new AnsApiClient(config);
    }

    @Test
    @DisplayName("serializeToJsonWithoutField removes the named top-level field")
    void removesNamedTopLevelField() throws Exception {
        Map<String, Object> object = new LinkedHashMap<>();
        object.put("agentDisplayName", "Test Agent");
        object.put("discoveryProfiles", "v2-only");

        String json = client().serializeToJsonWithoutField(object, "discoveryProfiles");

        JsonNode tree = MAPPER.readTree(json);
        assertThat(tree.has("discoveryProfiles")).isFalse();
        assertThat(tree.get("agentDisplayName").asText()).isEqualTo("Test Agent");
    }

    @Test
    @DisplayName("serializeToJsonWithoutField is a no-op when the named field is absent")
    void noOpWhenFieldAbsent() throws Exception {
        Map<String, Object> object = new LinkedHashMap<>();
        object.put("agentDisplayName", "Test Agent");
        object.put("version", "1.0.0");

        String json = client().serializeToJsonWithoutField(object, "discoveryProfiles");

        JsonNode tree = MAPPER.readTree(json);
        assertThat(tree.get("agentDisplayName").asText()).isEqualTo("Test Agent");
        assertThat(tree.get("version").asText()).isEqualTo("1.0.0");
    }

    @Test
    @DisplayName("serializeToJsonWithoutField only removes the top-level field, leaving nested fields intact")
    void leavesNestedFieldIntact() throws Exception {
        Map<String, Object> nested = new LinkedHashMap<>();
        nested.put("discoveryProfiles", "keep-me");

        Map<String, Object> object = new LinkedHashMap<>();
        object.put("discoveryProfiles", "remove-me");
        object.put("nested", nested);

        String json = client().serializeToJsonWithoutField(object, "discoveryProfiles");

        JsonNode tree = MAPPER.readTree(json);
        assertThat(tree.has("discoveryProfiles")).isFalse();
        assertThat(tree.get("nested").get("discoveryProfiles").asText()).isEqualTo("keep-me");
    }

    @Test
    @DisplayName("serializeToJsonWithoutField returns \"null\" for null input without throwing")
    void handlesNullInput() {
        String json = client().serializeToJsonWithoutField(null, "discoveryProfiles");

        assertThat(json).isEqualTo("null");
    }
}
