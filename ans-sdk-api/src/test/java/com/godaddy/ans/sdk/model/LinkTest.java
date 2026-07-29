package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;

import org.junit.jupiter.api.Test;

class LinkTest {

    private static Link populated() {
        return new Link()
                .rel("x")
                .href(URI.create("https://example.com/x"));
    }

    @Test
    void gettersReflectFluentSetters() {
        Link a = populated();
        Link b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        Link a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new Link());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class Link");
        assertThat(new Link().toString()).contains("class Link");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        Link a = populated();
        String json = mapper.writeValueAsString(a);
        Link back = mapper.readValue(json, Link.class);
        assertThat(back).isEqualTo(a);
    }
}
