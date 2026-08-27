package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AgentLifecycleStatusTest {

    @Test
    void valuesAndGetValue() {
        assertThat(AgentLifecycleStatus.values()).hasSize(7);
        assertThat(AgentLifecycleStatus.ACTIVE.getValue()).isEqualTo("ACTIVE");
    }

    @Test
    void fromValueRoundTrip() {
        for (AgentLifecycleStatus s : AgentLifecycleStatus.values()) {
            assertThat(AgentLifecycleStatus.fromValue(s.getValue())).isEqualTo(s);
            assertThat(s.toString()).isEqualTo(s.getValue());
        }
    }

    @Test
    void fromValueRejectsUnknown() {
        assertThatThrownBy(() -> AgentLifecycleStatus.fromValue("NOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOPE");
    }
}
