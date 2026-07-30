package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class IdentityLifecycleStatusTest {

    @Test
    void valuesAndGetValue() {
        assertThat(IdentityLifecycleStatus.values()).hasSize(3);
        assertThat(IdentityLifecycleStatus.VERIFIED.getValue()).isEqualTo("VERIFIED");
    }

    @Test
    void fromValueRoundTrip() {
        for (IdentityLifecycleStatus s : IdentityLifecycleStatus.values()) {
            assertThat(IdentityLifecycleStatus.fromValue(s.getValue())).isEqualTo(s);
            assertThat(s.toString()).isEqualTo(s.getValue());
        }
    }

    @Test
    void fromValueRejectsUnknown() {
        assertThatThrownBy(() -> IdentityLifecycleStatus.fromValue("NOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOPE");
    }
}
