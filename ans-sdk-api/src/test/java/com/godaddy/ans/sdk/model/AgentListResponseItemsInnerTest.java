package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class AgentListResponseItemsInnerTest {

    private static AgentListResponseItemsInner populated() {
        return new AgentListResponseItemsInner()
                .ansName("x")
                .agentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .agentDisplayName("x")
                .agentDescription("x")
                .version("x")
                .agentHost("x")
                .status(AgentLifecycleStatus.PENDING_VALIDATION)
                .ttl(1)
                .registrationTimestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .endpoints(List.of(new AgentEndpoint()))
                .links(List.of(new Link()));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentListResponseItemsInner a = populated();
        AgentListResponseItemsInner b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentListResponseItemsInner a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentListResponseItemsInner());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentListResponseItemsInner");
        assertThat(new AgentListResponseItemsInner().toString()).contains("class AgentListResponseItemsInner");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentListResponseItemsInner a = populated();
        String json = mapper.writeValueAsString(a);
        AgentListResponseItemsInner back = mapper.readValue(json, AgentListResponseItemsInner.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addEndpointsItemInitialiseList() {
        AgentListResponseItemsInner o = new AgentListResponseItemsInner();
        o.setEndpoints(null);
        o.addEndpointsItem(new AgentEndpoint());
        assertThat(o.getEndpoints()).hasSize(1);
    }

    @Test
    void addLinksItemInitialiseList() {
        AgentListResponseItemsInner o = new AgentListResponseItemsInner();
        o.setLinks(null);
        o.addLinksItem(new Link());
        assertThat(o.getLinks()).hasSize(1);
    }
}
