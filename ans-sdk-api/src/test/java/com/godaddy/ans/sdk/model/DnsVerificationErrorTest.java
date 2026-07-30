package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class DnsVerificationErrorTest {

    private static DnsVerificationError populated() {
        return new DnsVerificationError()
                .status(DnsVerificationError.StatusEnum.ERROR)
                .missingRecords(List.of(new DnsRecord()))
                .incorrectRecords(List.of(new DnsVerificationErrorIncorrectRecordsInner()));
    }

    @Test
    void gettersReflectFluentSetters() {
        DnsVerificationError a = populated();
        DnsVerificationError b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        DnsVerificationError a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new DnsVerificationError());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class DnsVerificationError");
        assertThat(new DnsVerificationError().toString()).contains("class DnsVerificationError");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        DnsVerificationError a = populated();
        String json = mapper.writeValueAsString(a);
        DnsVerificationError back = mapper.readValue(json, DnsVerificationError.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addMissingRecordsItemInitialiseList() {
        DnsVerificationError o = new DnsVerificationError();
        o.setMissingRecords(null);
        o.addMissingRecordsItem(new DnsRecord());
        assertThat(o.getMissingRecords()).hasSize(1);
    }

    @Test
    void addIncorrectRecordsItemInitialiseLis() {
        DnsVerificationError o = new DnsVerificationError();
        o.setIncorrectRecords(null);
        o.addIncorrectRecordsItem(new DnsVerificationErrorIncorrectRecordsInner());
        assertThat(o.getIncorrectRecords()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> DnsVerificationError.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
