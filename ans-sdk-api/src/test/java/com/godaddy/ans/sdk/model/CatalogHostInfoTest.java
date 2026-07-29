package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class CatalogHostInfoTest {

    private static CatalogHostInfo populated() {
        return new CatalogHostInfo()
                .identifier("x")
                .displayName("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogHostInfo a = populated();
        CatalogHostInfo b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogHostInfo a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogHostInfo());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogHostInfo");
        assertThat(new CatalogHostInfo().toString()).contains("class CatalogHostInfo");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogHostInfo a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogHostInfo back = mapper.readValue(json, CatalogHostInfo.class);
        assertThat(back).isEqualTo(a);
    }
}
