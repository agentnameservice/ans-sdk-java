package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;

import org.junit.jupiter.api.Test;

class CatalogAttestationTest {

    private static CatalogAttestation populated() {
        return new CatalogAttestation()
                .type("x")
                .uri(URI.create("https://example.com/x"))
                .mediaType("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogAttestation a = populated();
        CatalogAttestation b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogAttestation a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogAttestation());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogAttestation");
        assertThat(new CatalogAttestation().toString()).contains("class CatalogAttestation");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogAttestation a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogAttestation back = mapper.readValue(json, CatalogAttestation.class);
        assertThat(back).isEqualTo(a);
    }
}
