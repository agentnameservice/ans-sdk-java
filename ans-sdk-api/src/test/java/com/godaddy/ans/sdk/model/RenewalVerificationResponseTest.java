package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class RenewalVerificationResponseTest {

    private static RenewalVerificationResponse populated() {
        return new RenewalVerificationResponse()
                .status(RenewalVerificationResponse.StatusEnum.VERIFIED)
                .csrId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .tlsaDnsRecord(new DnsRecord())
                .nextStep(new NextStep());
    }

    @Test
    void gettersReflectFluentSetters() {
        RenewalVerificationResponse a = populated();
        RenewalVerificationResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        RenewalVerificationResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new RenewalVerificationResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class RenewalVerificationResponse");
        assertThat(new RenewalVerificationResponse().toString()).contains("class RenewalVerificationResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        RenewalVerificationResponse a = populated();
        String json = mapper.writeValueAsString(a);
        RenewalVerificationResponse back = mapper.readValue(json, RenewalVerificationResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> RenewalVerificationResponse.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
