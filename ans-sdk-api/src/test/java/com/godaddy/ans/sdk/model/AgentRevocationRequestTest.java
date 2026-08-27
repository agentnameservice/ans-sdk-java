package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class AgentRevocationRequestTest {

    private static AgentRevocationRequest populated() {
        return new AgentRevocationRequest()
                .reason(RevocationReason.KEY_COMPROMISE)
                .comments("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentRevocationRequest a = populated();
        AgentRevocationRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentRevocationRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentRevocationRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentRevocationRequest");
        assertThat(new AgentRevocationRequest().toString()).contains("class AgentRevocationRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentRevocationRequest a = populated();
        String json = mapper.writeValueAsString(a);
        AgentRevocationRequest back = mapper.readValue(json, AgentRevocationRequest.class);
        assertThat(back).isEqualTo(a);
    }
}
