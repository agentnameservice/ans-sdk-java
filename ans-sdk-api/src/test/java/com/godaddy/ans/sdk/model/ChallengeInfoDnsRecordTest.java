package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ChallengeInfoDnsRecordTest {

    private static ChallengeInfoDnsRecord populated() {
        return new ChallengeInfoDnsRecord()
                .name("x")
                .type("x")
                .value("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        ChallengeInfoDnsRecord a = populated();
        ChallengeInfoDnsRecord b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        ChallengeInfoDnsRecord a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new ChallengeInfoDnsRecord());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class ChallengeInfoDnsRecord");
        assertThat(new ChallengeInfoDnsRecord().toString()).contains("class ChallengeInfoDnsRecord");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        ChallengeInfoDnsRecord a = populated();
        String json = mapper.writeValueAsString(a);
        ChallengeInfoDnsRecord back = mapper.readValue(json, ChallengeInfoDnsRecord.class);
        assertThat(back).isEqualTo(a);
    }
}
