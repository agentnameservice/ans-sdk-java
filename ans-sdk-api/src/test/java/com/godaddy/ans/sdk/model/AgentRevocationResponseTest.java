package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class AgentRevocationResponseTest {

    private static AgentRevocationResponse populated() {
        return new AgentRevocationResponse()
                .agentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .ansName("x")
                .status(AgentLifecycleStatus.PENDING_VALIDATION)
                .revokedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .reason(RevocationReason.KEY_COMPROMISE)
                .dnsRecordsToRemove(List.of(new DnsRecord()))
                .links(List.of(new Link()));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentRevocationResponse a = populated();
        AgentRevocationResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentRevocationResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentRevocationResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentRevocationResponse");
        assertThat(new AgentRevocationResponse().toString()).contains("class AgentRevocationResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentRevocationResponse a = populated();
        String json = mapper.writeValueAsString(a);
        AgentRevocationResponse back = mapper.readValue(json, AgentRevocationResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addDnsRecordsToRemoveItemInitialiseList() {
        AgentRevocationResponse o = new AgentRevocationResponse();
        o.setDnsRecordsToRemove(null);
        o.addDnsRecordsToRemoveItem(new DnsRecord());
        assertThat(o.getDnsRecordsToRemove()).hasSize(1);
    }

    @Test
    void addLinksItemInitialiseListLinks() {
        AgentRevocationResponse o = new AgentRevocationResponse();
        o.setLinks(null);
        o.addLinksItem(new Link());
        assertThat(o.getLinks()).hasSize(1);
    }
}
