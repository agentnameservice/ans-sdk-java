package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class IdentityChallengeResponseTest {

    private static IdentityChallengeResponse populated() {
        return new IdentityChallengeResponse()
                .identityId("x")
                .kind(IdentityChallengeResponse.KindEnum.DID_WEB)
                .value("x")
                .status(IdentityLifecycleStatus.PENDING_CONTROL)
                .nonce("x")
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .challenges(List.of(new IdentityProofChallenge()))
                .presentationStatus(IdentityChallengeResponse.PresentationStatusEnum.AUTHORIZED);
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityChallengeResponse a = populated();
        IdentityChallengeResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityChallengeResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityChallengeResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityChallengeResponse");
        assertThat(new IdentityChallengeResponse().toString()).contains("class IdentityChallengeResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityChallengeResponse a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityChallengeResponse back = mapper.readValue(json, IdentityChallengeResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addChallengesItemInitialiseList() {
        IdentityChallengeResponse o = new IdentityChallengeResponse();
        o.setChallenges(null);
        o.addChallengesItem(new IdentityProofChallenge());
        assertThat(o.getChallenges()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> IdentityChallengeResponse.KindEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> IdentityChallengeResponse.PresentationStatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
