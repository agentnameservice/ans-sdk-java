package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class AgentDetailsTest {

    private static AgentDetails populated() {
        return new AgentDetails()
                .agentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .agentDisplayName("x")
                .agentDescription("x")
                .version("x")
                .agentHost("x")
                .endpoints(List.of(new AgentEndpoint()))
                .ansName("x")
                .agentStatus(AgentLifecycleStatus.PENDING_VALIDATION)
                .registrationTimestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .lastRenewalTimestamp(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .registrationPending(new RegistrationPending())
                .links(List.of(new Link()))
                .identities(List.of(new LinkedIdentity()));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentDetails a = populated();
        AgentDetails b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentDetails a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentDetails());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentDetails");
        assertThat(new AgentDetails().toString()).contains("class AgentDetails");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentDetails a = populated();
        String json = mapper.writeValueAsString(a);
        AgentDetails back = mapper.readValue(json, AgentDetails.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addEndpointsInitialisesList() {
            AgentDetails o = new AgentDetails();
            o.setEndpoints(null);
            o.addEndpointsItem(new AgentEndpoint());
            assertThat(o.getEndpoints()).hasSize(1);
    }

    @Test
    void addLinkInitialisesList() {
        AgentDetails o = new AgentDetails();
        o.setLinks(null);
        o.addLinksItem(new Link());
        assertThat(o.getLinks()).hasSize(1);
    }

    @Test
    void addIdentityInitialisesList() {
        AgentDetails o = new AgentDetails();
        o.setIdentities(null);
        o.addIdentitiesItem(new LinkedIdentity());
        assertThat(o.getIdentities()).hasSize(1);
    }
}
