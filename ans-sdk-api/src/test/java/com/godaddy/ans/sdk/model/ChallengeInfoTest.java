package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;

import org.junit.jupiter.api.Test;

class ChallengeInfoTest {

    private static ChallengeInfo populated() {
        return new ChallengeInfo()
                .type(ChallengeInfo.TypeEnum.DNS_01)
                .token("x")
                .keyAuthorization("x")
                .dnsRecord(new ChallengeInfoDnsRecord())
                .httpPath("x")
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"));
    }

    @Test
    void gettersReflectFluentSetters() {
        ChallengeInfo a = populated();
        ChallengeInfo b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        ChallengeInfo a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new ChallengeInfo());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class ChallengeInfo");
        assertThat(new ChallengeInfo().toString()).contains("class ChallengeInfo");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        ChallengeInfo a = populated();
        String json = mapper.writeValueAsString(a);
        ChallengeInfo back = mapper.readValue(json, ChallengeInfo.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> ChallengeInfo.TypeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
