package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class AgentStatusTest {

    private static AgentStatus populated() {
        return new AgentStatus()
                .status(AgentLifecycleStatus.PENDING_VALIDATION)
                .phase(AgentStatus.PhaseEnum.INITIALIZATION)
                .completedSteps(List.of("x"))
                .pendingSteps(List.of("x"))
                .createdAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .updatedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentStatus a = populated();
        AgentStatus b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentStatus a = populated();
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentStatus());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentStatus");
        assertThat(new AgentStatus().toString()).contains("class AgentStatus");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentStatus a = populated();
        String json = mapper.writeValueAsString(a);
        AgentStatus back = mapper.readValue(json, AgentStatus.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addCompletedStepsItemInitialiseList() {
        AgentStatus o = new AgentStatus();
        o.setCompletedSteps(null);
        o.addCompletedStepsItem("x");
        assertThat(o.getCompletedSteps()).hasSize(1);
    }

    @Test
    void addPendingStepsItemInitialiseList() {
        AgentStatus o = new AgentStatus();
        o.setPendingSteps(null);
        o.addPendingStepsItem("x");
        assertThat(o.getPendingSteps()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> AgentStatus.PhaseEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
