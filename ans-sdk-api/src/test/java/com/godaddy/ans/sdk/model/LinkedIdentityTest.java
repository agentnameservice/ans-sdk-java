package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

class LinkedIdentityTest {

    private static LinkedIdentity populated() {
        return new LinkedIdentity()
                .identityId("x")
                .kind(LinkedIdentity.KindEnum.DID_WEB)
                .value("x")
                .identityStatus(LinkedIdentity.IdentityStatusEnum.VERIFIED)
                .linkedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"));
    }

    @Test
    void gettersReflectFluentSetters() {
        LinkedIdentity a = populated();
        LinkedIdentity b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        LinkedIdentity a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new LinkedIdentity());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class LinkedIdentity");
        assertThat(new LinkedIdentity().toString()).contains("class LinkedIdentity");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        LinkedIdentity a = populated();
        String json = mapper.writeValueAsString(a);
        LinkedIdentity back = mapper.readValue(json, LinkedIdentity.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> LinkedIdentity.KindEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> LinkedIdentity.IdentityStatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
