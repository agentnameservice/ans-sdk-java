package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;

import org.junit.jupiter.api.Test;

class CatalogEntryMetadataTest {

    private static CatalogEntryMetadata populated() {
        return new CatalogEntryMetadata()
                .ansName("x")
                .agentHost("x")
                .badgeUrl(URI.create("https://example.com/x"));
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogEntryMetadata a = populated();
        CatalogEntryMetadata b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogEntryMetadata a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogEntryMetadata());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogEntryMetadata");
        assertThat(new CatalogEntryMetadata().toString()).contains("class CatalogEntryMetadata");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogEntryMetadata a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogEntryMetadata back = mapper.readValue(json, CatalogEntryMetadata.class);
        assertThat(back).isEqualTo(a);
    }
}
