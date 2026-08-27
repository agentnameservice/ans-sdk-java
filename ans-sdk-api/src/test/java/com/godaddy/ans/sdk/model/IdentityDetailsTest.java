package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class IdentityDetailsTest {

    private static IdentityDetails populated() {
        return new IdentityDetails()
                .identityId("x")
                .kind(IdentityDetails.KindEnum.DID_WEB)
                .value("x")
                .status(IdentityLifecycleStatus.PENDING_CONTROL)
                .proofMethod(IdentityDetails.ProofMethodEnum.DID_WEB_SIG)
                .pendingValue("x")
                .verifiedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .createdAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .linkedAgents(List.of(new IdentityDetailsLinkedAgentsInner()));
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityDetails a = populated();
        IdentityDetails b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityDetails a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityDetails());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityDetails");
        assertThat(new IdentityDetails().toString()).contains("class IdentityDetails");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityDetails a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityDetails back = mapper.readValue(json, IdentityDetails.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addLinkedAgentsItemInitialiseList() {
        IdentityDetails o = new IdentityDetails();
        o.setLinkedAgents(null);
        o.addLinkedAgentsItem(new IdentityDetailsLinkedAgentsInner());
        assertThat(o.getLinkedAgents()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> IdentityDetails.KindEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> IdentityDetails.ProofMethodEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
