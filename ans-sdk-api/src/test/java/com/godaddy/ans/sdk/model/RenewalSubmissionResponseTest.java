package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class RenewalSubmissionResponseTest {

    private static RenewalSubmissionResponse populated() {
        return new RenewalSubmissionResponse()
                .renewalType(RenewalSubmissionResponse.RenewalTypeEnum.SERVER_CSR)
                .status(RenewalSubmissionResponse.StatusEnum.PENDING_VALIDATION)
                .csrId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .challenges(new RenewalSubmissionResponseChallenges())
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .nextStep(new NextStep())
                .links(List.of(new Link()));
    }

    @Test
    void gettersReflectFluentSetters() {
        RenewalSubmissionResponse a = populated();
        RenewalSubmissionResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        RenewalSubmissionResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new RenewalSubmissionResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class RenewalSubmissionResponse");
        assertThat(new RenewalSubmissionResponse().toString()).contains("class RenewalSubmissionResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        RenewalSubmissionResponse a = populated();
        String json = mapper.writeValueAsString(a);
        RenewalSubmissionResponse back = mapper.readValue(json, RenewalSubmissionResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addLinksItemInitialiseList() {
        RenewalSubmissionResponse o = new RenewalSubmissionResponse();
        o.setLinks(null);
        o.addLinksItem(new Link());
        assertThat(o.getLinks()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> RenewalSubmissionResponse.RenewalTypeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> RenewalSubmissionResponse.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
