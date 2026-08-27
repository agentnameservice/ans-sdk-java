package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class RenewalStatusResponseTest {

    private static RenewalStatusResponse populated() {
        return new RenewalStatusResponse()
                .renewalType(RenewalStatusResponse.RenewalTypeEnum.SERVER_CSR)
                .status(RenewalStatusResponse.StatusEnum.PENDING_VALIDATION)
                .csrId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .challenges(new RenewalSubmissionResponseChallenges())
                .tlsaDnsRecord(new DnsRecord())
                .failureReason("x")
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .nextStep(new NextStep());
    }

    @Test
    void gettersReflectFluentSetters() {
        RenewalStatusResponse a = populated();
        RenewalStatusResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        RenewalStatusResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new RenewalStatusResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class RenewalStatusResponse");
        assertThat(new RenewalStatusResponse().toString()).contains("class RenewalStatusResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        RenewalStatusResponse a = populated();
        String json = mapper.writeValueAsString(a);
        RenewalStatusResponse back = mapper.readValue(json, RenewalStatusResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> RenewalStatusResponse.RenewalTypeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> RenewalStatusResponse.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
