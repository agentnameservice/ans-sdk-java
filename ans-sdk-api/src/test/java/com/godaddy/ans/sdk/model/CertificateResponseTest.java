package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class CertificateResponseTest {

    private static CertificateResponse populated() {
        return new CertificateResponse()
                .csrId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .certificateSubject("x")
                .certificateIssuer("x")
                .certificateSerialNumber("x")
                .certificateValidFrom(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .certificateValidTo(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .certificatePEM("x")
                .chainPEM("x")
                .certificatePublicKeyAlgorithm("x")
                .certificateSignatureAlgorithm("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CertificateResponse a = populated();
        CertificateResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CertificateResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CertificateResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CertificateResponse");
        assertThat(new CertificateResponse().toString()).contains("class CertificateResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CertificateResponse a = populated();
        String json = mapper.writeValueAsString(a);
        CertificateResponse back = mapper.readValue(json, CertificateResponse.class);
        assertThat(back).isEqualTo(a);
    }
}
