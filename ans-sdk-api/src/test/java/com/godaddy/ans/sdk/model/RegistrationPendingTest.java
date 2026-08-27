package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class RegistrationPendingTest {

    private static RegistrationPending populated() {
        return new RegistrationPending()
                .agentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .status(RegistrationPending.StatusEnum.PENDING_VALIDATION)
                .ansName("x")
                .nextSteps(List.of(new NextStep()))
                .challenges(List.of(new ChallengeInfo()))
                .dnsRecords(List.of(new DnsRecord()))
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .links(List.of(new Link()));
    }

    @Test
    void gettersReflectFluentSetters() {
        RegistrationPending a = populated();
        RegistrationPending b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        RegistrationPending a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new RegistrationPending());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class RegistrationPending");
        assertThat(new RegistrationPending().toString()).contains("class RegistrationPending");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        RegistrationPending a = populated();
        String json = mapper.writeValueAsString(a);
        RegistrationPending back = mapper.readValue(json, RegistrationPending.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addNextStepsItemInitialiseList() {
        RegistrationPending o = new RegistrationPending();
        o.setNextSteps(null);
        o.addNextStepsItem(new NextStep());
        assertThat(o.getNextSteps()).hasSize(1);
    }

    @Test
    void addChallengeItemHelperInitialisesList() {
        RegistrationPending o = new RegistrationPending();
        o.setChallenges(null);
        o.addChallengesItem(new ChallengeInfo());
        assertThat(o.getChallenges()).hasSize(1);
    }

    @Test
    void addDnsRecordHelperInitialisesList() {
        RegistrationPending o = new RegistrationPending();
        o.setDnsRecords(null);
        o.addDnsRecordsItem(new DnsRecord());
        assertThat(o.getDnsRecords()).hasSize(1);
    }

    @Test
    void addLinkHelperInitialisesList() {
        RegistrationPending o = new RegistrationPending();
        o.setLinks(null);
        o.addLinksItem(new Link());
        assertThat(o.getLinks()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> RegistrationPending.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
