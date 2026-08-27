package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class DiscoveryProfileTest {

    @Test
    void valuesAndGetValue() {
        assertThat(DiscoveryProfile.values()).containsExactly(
                DiscoveryProfile.ANS_DNSAID, DiscoveryProfile.ANS_TXT);
        assertThat(DiscoveryProfile.ANS_DNSAID.getValue()).isEqualTo("ANS_DNSAID");
    }

    @Test
    void fromValueRoundTrip() {
        for (DiscoveryProfile p : DiscoveryProfile.values()) {
            assertThat(DiscoveryProfile.fromValue(p.getValue())).isEqualTo(p);
            assertThat(p.toString()).isEqualTo(p.getValue());
        }
    }

    @Test
    void fromValueRejectsUnknown() {
        assertThatThrownBy(() -> DiscoveryProfile.fromValue("NOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOPE");
    }
}
