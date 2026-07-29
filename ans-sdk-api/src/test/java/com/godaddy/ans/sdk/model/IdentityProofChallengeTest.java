package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class IdentityProofChallengeTest {

    private static IdentityProofChallenge populated() {
        return new IdentityProofChallenge()
                .kid("x")
                .signingInput("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityProofChallenge a = populated();
        IdentityProofChallenge b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityProofChallenge a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityProofChallenge());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityProofChallenge");
        assertThat(new IdentityProofChallenge().toString()).contains("class IdentityProofChallenge");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityProofChallenge a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityProofChallenge back = mapper.readValue(json, IdentityProofChallenge.class);
        assertThat(back).isEqualTo(a);
    }
}
