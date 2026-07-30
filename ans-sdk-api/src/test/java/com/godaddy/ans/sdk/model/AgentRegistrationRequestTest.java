package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class AgentRegistrationRequestTest {

    private static AgentRegistrationRequest populated() {
        return new AgentRegistrationRequest()
                .agentDisplayName("x")
                .agentDescription("x")
                .version("x")
                .agentHost("x")
                .endpoints(List.of(new AgentEndpoint()))
                .serverCsrPEM("x")
                .serverCertificatePEM("x")
                .serverCertificateChainPEM("x")
                .identityCsrPEM("x")
                .discoveryProfiles(Set.of(DiscoveryProfile.ANS_DNSAID));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentRegistrationRequest a = populated();
        AgentRegistrationRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentRegistrationRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentRegistrationRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentRegistrationRequest");
        assertThat(new AgentRegistrationRequest().toString()).contains("class AgentRegistrationRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentRegistrationRequest a = populated();
        String json = mapper.writeValueAsString(a);
        AgentRegistrationRequest back = mapper.readValue(json, AgentRegistrationRequest.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addEndpointsItemInitialiseList() {
        AgentRegistrationRequest o = new AgentRegistrationRequest();
        o.setEndpoints(null);
        o.addEndpointsItem(new AgentEndpoint());
        assertThat(o.getEndpoints()).hasSize(1);
    }
}
