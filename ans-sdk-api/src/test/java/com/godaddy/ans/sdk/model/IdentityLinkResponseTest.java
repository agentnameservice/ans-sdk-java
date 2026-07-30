package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class IdentityLinkResponseTest {

    private static IdentityLinkResponse populated() {
        return new IdentityLinkResponse()
                .linked(1);
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityLinkResponse a = populated();
        IdentityLinkResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityLinkResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityLinkResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityLinkResponse");
        assertThat(new IdentityLinkResponse().toString()).contains("class IdentityLinkResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityLinkResponse a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityLinkResponse back = mapper.readValue(json, IdentityLinkResponse.class);
        assertThat(back).isEqualTo(a);
    }
}
