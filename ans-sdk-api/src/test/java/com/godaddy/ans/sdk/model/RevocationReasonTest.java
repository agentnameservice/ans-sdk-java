package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RevocationReasonTest {

    @Test
    void valuesAndGetValue() {
        assertThat(RevocationReason.values()).hasSize(7);
        assertThat(RevocationReason.KEY_COMPROMISE.getValue()).isEqualTo("KEY_COMPROMISE");
    }

    @Test
    void fromValueRoundTrip() {
        for (RevocationReason r : RevocationReason.values()) {
            assertThat(RevocationReason.fromValue(r.getValue())).isEqualTo(r);
            assertThat(r.toString()).isEqualTo(r.getValue());
        }
    }

    @Test
    void fromValueRejectsUnknown() {
        assertThatThrownBy(() -> RevocationReason.fromValue("NOPE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("NOPE");
    }
}
