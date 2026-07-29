package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class IdentityLinkRequestTest {

    private static IdentityLinkRequest populated() {
        return new IdentityLinkRequest()
                .agentIds(List.of(UUID.fromString("11111111-1111-1111-1111-111111111111")));
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityLinkRequest a = populated();
        IdentityLinkRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityLinkRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityLinkRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityLinkRequest");
        assertThat(new IdentityLinkRequest().toString()).contains("class IdentityLinkRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityLinkRequest a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityLinkRequest back = mapper.readValue(json, IdentityLinkRequest.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addAgentIdsItemInitialiseList() {
        IdentityLinkRequest o = new IdentityLinkRequest();
        o.setAgentIds(null);
        o.addAgentIdsItem(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        assertThat(o.getAgentIds()).hasSize(1);
    }
}
