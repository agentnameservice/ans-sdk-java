package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class IdentityDetailsLinkedAgentsInnerTest {

    private static IdentityDetailsLinkedAgentsInner populated() {
        return new IdentityDetailsLinkedAgentsInner()
                .agentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .linkedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"));
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityDetailsLinkedAgentsInner a = populated();
        IdentityDetailsLinkedAgentsInner b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityDetailsLinkedAgentsInner a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityDetailsLinkedAgentsInner());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityDetailsLinkedAgentsInner");
        assertThat(new IdentityDetailsLinkedAgentsInner().toString())
                .contains("class IdentityDetailsLinkedAgentsInner");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityDetailsLinkedAgentsInner a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityDetailsLinkedAgentsInner back = mapper.readValue(json, IdentityDetailsLinkedAgentsInner.class);
        assertThat(back).isEqualTo(a);
    }
}
