package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class CatalogPublisherTest {

    private static CatalogPublisher populated() {
        return new CatalogPublisher()
                .identifier("x")
                .displayName("x")
                .identityType("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogPublisher a = populated();
        CatalogPublisher b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogPublisher a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogPublisher());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogPublisher");
        assertThat(new CatalogPublisher().toString()).contains("class CatalogPublisher");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogPublisher a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogPublisher back = mapper.readValue(json, CatalogPublisher.class);
        assertThat(back).isEqualTo(a);
    }
}
