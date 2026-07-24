package com.godaddy.ans.sdk.registration;

import com.godaddy.ans.sdk.config.ApiVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Direct unit tests for {@link AgentPaths}, the single source of truth for
 * V1/V2 path branching.
 *
 * <p>Path logic was previously exercised only indirectly through WireMock
 * integration tests, where a typo in a path constant surfaced as an opaque
 * stub miss. These tests pin each method/version combination to an exact
 * string so a regression fails here, at the source.</p>
 */
class AgentPathsTest {

    private static final String AGENT_ID = "550e8400-e29b-41d4-a716-446655440000";

    @ParameterizedTest
    @CsvSource({
        "V1, /v1/agents",
        "V2, /v2/ans/agents",
    })
    @DisplayName("agentsCollectionPath returns the lane collection root")
    void agentsCollectionPath(ApiVersion version, String expected) {
        assertThat(AgentPaths.agentsCollectionPath(version)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "V1, /v1/agents/register",
        "V2, /v2/ans/agents",
    })
    @DisplayName("registerPath uses the /register verb on v1 and the collection POST on v2")
    void registerPath(ApiVersion version, String expected) {
        assertThat(AgentPaths.registerPath(version)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "V1, /v1/agents/" + AGENT_ID,
        "V2, /v2/ans/agents/" + AGENT_ID,
    })
    @DisplayName("agentPath with no trailing segments returns collection/{agentId}")
    void agentPathNoSegments(ApiVersion version, String expected) {
        assertThat(AgentPaths.agentPath(version, AGENT_ID)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "V1, /v1/agents/" + AGENT_ID + "/verify-acme",
        "V2, /v2/ans/agents/" + AGENT_ID + "/verify-acme",
    })
    @DisplayName("agentPath appends a single trailing segment")
    void agentPathSingleSegment(ApiVersion version, String expected) {
        assertThat(AgentPaths.agentPath(version, AGENT_ID, "verify-acme")).isEqualTo(expected);
    }

    @Test
    @DisplayName("agentPath joins multiple trailing segments in order")
    void agentPathMultipleSegments() {
        assertThat(AgentPaths.agentPath(ApiVersion.V2, AGENT_ID, "certificates", "identity"))
            .isEqualTo("/v2/ans/agents/" + AGENT_ID + "/certificates/identity");
    }

    @Test
    @DisplayName("agentPath preserves the v1 lane prefix with multiple segments")
    void agentPathMultipleSegmentsV1() {
        assertThat(AgentPaths.agentPath(ApiVersion.V1, AGENT_ID, "certificates", "server", "renewal"))
            .isEqualTo("/v1/agents/" + AGENT_ID + "/certificates/server/renewal");
    }
}
