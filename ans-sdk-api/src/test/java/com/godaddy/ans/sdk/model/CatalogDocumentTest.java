package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class CatalogDocumentTest {

    private static CatalogDocument populated() {
        return new CatalogDocument()
                .specVersion("x")
                .host(new CatalogHostInfo())
                .entries(List.of(new CatalogEntry()));
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogDocument a = populated();
        CatalogDocument b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogDocument a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogDocument());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogDocument");
        assertThat(new CatalogDocument().toString()).contains("class CatalogDocument");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogDocument a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogDocument back = mapper.readValue(json, CatalogDocument.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addEntriesItemInitialiseList() {
        CatalogDocument o = new CatalogDocument();
        o.setEntries(null);
        o.addEntriesItem(new CatalogEntry());
        assertThat(o.getEntries()).hasSize(1);
    }
}
