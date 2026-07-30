package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class AgentLifecycleStatusFilterTest {

    @Test
    void valuesAndGetValue() {
        assertThat(AgentLifecycleStatusFilter.values()).hasSize(5);
        assertThat(AgentLifecycleStatusFilter.ALL.getValue()).isEqualTo("ALL");
    }

    @Test
    void fromValueRoundTrip() {
        for (AgentLifecycleStatusFilter s : AgentLifecycleStatusFilter.values()) {
            assertThat(AgentLifecycleStatusFilter.fromValue(s.getValue())).isEqualTo(s);
            assertThat(s.toString()).isEqualTo(s.getValue());
        }
    }

    @Test
    void fromValueRejectsUnknown() {
        assertThatThrownBy(() -> AgentLifecycleStatusFilter.fromValue("NOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOPE");
    }
}
