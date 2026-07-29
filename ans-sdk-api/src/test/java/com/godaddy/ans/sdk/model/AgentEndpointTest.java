package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;

import org.junit.jupiter.api.Test;

class AgentEndpointTest {

    private static AgentEndpoint populated() {
        return new AgentEndpoint()
                .agentUrl(URI.create("https://example.com/x"))
                .metaDataUrl(URI.create("https://example.com/x"))
                .metaDataHash("x")
                .documentationUrl(URI.create("https://example.com/x"))
                .protocol(Protocol.A2_A)
                .functions(List.of(new AgentFunction()))
                .transports(List.of(AgentEndpoint.TransportsEnum.STREAMABLE_HTTP));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentEndpoint a = populated();
        AgentEndpoint b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentEndpoint a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentEndpoint());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentEndpoint");
        assertThat(new AgentEndpoint().toString()).contains("class AgentEndpoint");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentEndpoint a = populated();
        String json = mapper.writeValueAsString(a);
        AgentEndpoint back = mapper.readValue(json, AgentEndpoint.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addFunctionsItemInitialiseList() {
        AgentEndpoint o = new AgentEndpoint();
        o.setFunctions(null);
        o.addFunctionsItem(new AgentFunction());
        assertThat(o.getFunctions()).hasSize(1);
    }

    @Test
    void addTransportsItemInitialiseListTransports() {
        AgentEndpoint o = new AgentEndpoint();
        o.setTransports(null);
        o.addTransportsItem(AgentEndpoint.TransportsEnum.STREAMABLE_HTTP);
        assertThat(o.getTransports()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> AgentEndpoint.TransportsEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
