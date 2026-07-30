package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ProtocolTest {

    @Test
    void valuesAndGetValue() {
        assertThat(Protocol.values()).containsExactly(
                Protocol.A2_A, Protocol.MCP, Protocol.HTTP_API);
        assertThat(Protocol.A2_A.getValue()).isEqualTo("A2A");
        assertThat(Protocol.MCP.getValue()).isEqualTo("MCP");
        assertThat(Protocol.HTTP_API.getValue()).isEqualTo("HTTP_API");
    }

    @Test
    void fromValueRoundTrip() {
        for (Protocol p : Protocol.values()) {
            assertThat(Protocol.fromValue(p.getValue())).isEqualTo(p);
            assertThat(p.toString()).isEqualTo(p.getValue());
        }
    }

    @Test
    void fromValueRejectsUnknown() {
        assertThatThrownBy(() -> Protocol.fromValue("NOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOPE");
    }
}
