package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ServerCertificateRenewalRequestTest {

    private static ServerCertificateRenewalRequest populated() {
        return new ServerCertificateRenewalRequest()
                .serverCsrPEM("x")
                .serverCertificatePEM("x")
                .serverCertificateChainPEM("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        ServerCertificateRenewalRequest a = populated();
        ServerCertificateRenewalRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        ServerCertificateRenewalRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new ServerCertificateRenewalRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class ServerCertificateRenewalRequest");
        assertThat(new ServerCertificateRenewalRequest().toString()).contains("class ServerCertificateRenewalRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        ServerCertificateRenewalRequest a = populated();
        String json = mapper.writeValueAsString(a);
        ServerCertificateRenewalRequest back = mapper.readValue(json, ServerCertificateRenewalRequest.class);
        assertThat(back).isEqualTo(a);
    }
}
