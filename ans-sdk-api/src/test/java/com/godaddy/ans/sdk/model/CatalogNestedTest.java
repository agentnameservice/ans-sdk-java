package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class CatalogNestedTest {

    private static CatalogNested populated() {
        return new CatalogNested()
                .specVersion("x")
                .entries(List.of(new CatalogEntry()));
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogNested a = populated();
        CatalogNested b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogNested a = populated();
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogNested());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogNested");
        assertThat(new CatalogNested().toString()).contains("class CatalogNested");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogNested a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogNested back = mapper.readValue(json, CatalogNested.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addEntriesItemInitialiseList() {
        CatalogNested o = new CatalogNested();
        o.setEntries(null);
        o.addEntriesItem(new CatalogEntry());
        assertThat(o.getEntries()).hasSize(1);
    }
}
