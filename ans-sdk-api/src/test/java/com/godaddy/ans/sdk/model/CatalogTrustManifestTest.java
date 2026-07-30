package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class CatalogTrustManifestTest {

    private static CatalogTrustManifest populated() {
        return new CatalogTrustManifest()
                .identity("x")
                .attestations(List.of(new CatalogAttestation()));
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogTrustManifest a = populated();
        CatalogTrustManifest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogTrustManifest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogTrustManifest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogTrustManifest");
        assertThat(new CatalogTrustManifest().toString()).contains("class CatalogTrustManifest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogTrustManifest a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogTrustManifest back = mapper.readValue(json, CatalogTrustManifest.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addAttestationsItemInitialiseList() {
        CatalogTrustManifest o = new CatalogTrustManifest();
        o.setAttestations(null);
        o.addAttestationsItem(new CatalogAttestation());
        assertThat(o.getAttestations()).hasSize(1);
    }
}
