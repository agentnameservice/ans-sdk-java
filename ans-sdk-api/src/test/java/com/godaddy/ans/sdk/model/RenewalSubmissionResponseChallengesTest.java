package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class RenewalSubmissionResponseChallengesTest {

    private static RenewalSubmissionResponseChallenges populated() {
        return new RenewalSubmissionResponseChallenges()
                .dns01(new ChallengeInfo())
                .http01(new ChallengeInfo());
    }

    @Test
    void gettersReflectFluentSetters() {
        RenewalSubmissionResponseChallenges a = populated();
        RenewalSubmissionResponseChallenges b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        RenewalSubmissionResponseChallenges a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new RenewalSubmissionResponseChallenges());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class RenewalSubmissionResponseChallenges");
        assertThat(new RenewalSubmissionResponseChallenges().toString())
                .contains("class RenewalSubmissionResponseChallenges");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        RenewalSubmissionResponseChallenges a = populated();
        String json = mapper.writeValueAsString(a);
        RenewalSubmissionResponseChallenges back = mapper.readValue(json, RenewalSubmissionResponseChallenges.class);
        assertThat(back).isEqualTo(a);
    }
}
