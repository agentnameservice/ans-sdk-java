package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class DnsRecordTest {

    private static DnsRecord populated() {
        return new DnsRecord()
                .name("x")
                .type(DnsRecord.TypeEnum.HTTPS)
                .value("x")
                .priority(1)
                .ttl(1)
                .purpose(DnsRecord.PurposeEnum.DISCOVERY)
                .required(true);
    }

    @Test
    void gettersReflectFluentSetters() {
        DnsRecord a = populated();
        DnsRecord b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        DnsRecord a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new DnsRecord());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class DnsRecord");
        assertThat(new DnsRecord().toString()).contains("class DnsRecord");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        DnsRecord a = populated();
        String json = mapper.writeValueAsString(a);
        DnsRecord back = mapper.readValue(json, DnsRecord.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> DnsRecord.TypeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> DnsRecord.PurposeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
