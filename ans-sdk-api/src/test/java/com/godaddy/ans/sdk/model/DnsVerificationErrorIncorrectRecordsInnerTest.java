package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class DnsVerificationErrorIncorrectRecordsInnerTest {

    private static DnsVerificationErrorIncorrectRecordsInner populated() {
        return new DnsVerificationErrorIncorrectRecordsInner()
                .record(new DnsRecord())
                .found("x")
                .expected("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        DnsVerificationErrorIncorrectRecordsInner a = populated();
        DnsVerificationErrorIncorrectRecordsInner b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        DnsVerificationErrorIncorrectRecordsInner a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new DnsVerificationErrorIncorrectRecordsInner());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class DnsVerificationErrorIncorrectRecordsInner");
        assertThat(new DnsVerificationErrorIncorrectRecordsInner().toString())
                .contains("class DnsVerificationErrorIncorrectRecordsInner");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        DnsVerificationErrorIncorrectRecordsInner a = populated();
        String json = mapper.writeValueAsString(a);
        DnsVerificationErrorIncorrectRecordsInner back =
                mapper.readValue(json, DnsVerificationErrorIncorrectRecordsInner.class);
        assertThat(back).isEqualTo(a);
    }
}
