package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class CatalogEntryTest {

    private static CatalogEntry populated() {
        return new CatalogEntry()
                .identifier("x")
                .displayName("x")
                .description("x")
                .version("x")
                .mediaType("x")
                .url(URI.create("https://example.com/x"))
                .data(new CatalogNested())
                .tags(List.of("x"))
                .updatedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .publisher(new CatalogPublisher())
                .metadata(new CatalogEntryMetadata())
                .trustManifest(new CatalogTrustManifest());
    }

    @Test
    void gettersReflectFluentSetters() {
        CatalogEntry a = populated();
        CatalogEntry b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CatalogEntry a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CatalogEntry());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CatalogEntry");
        assertThat(new CatalogEntry().toString()).contains("class CatalogEntry");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CatalogEntry a = populated();
        String json = mapper.writeValueAsString(a);
        CatalogEntry back = mapper.readValue(json, CatalogEntry.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addTagsItemInitialiseList() {
        CatalogEntry o = new CatalogEntry();
        o.setTags(null);
        o.addTagsItem("x");
        assertThat(o.getTags()).hasSize(1);
    }
}
