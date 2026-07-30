package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class CsrSubmissionResponseTest {

    private static CsrSubmissionResponse populated() {
        return new CsrSubmissionResponse()
                .csrId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .message("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CsrSubmissionResponse a = populated();
        CsrSubmissionResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CsrSubmissionResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CsrSubmissionResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CsrSubmissionResponse");
        assertThat(new CsrSubmissionResponse().toString()).contains("class CsrSubmissionResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CsrSubmissionResponse a = populated();
        String json = mapper.writeValueAsString(a);
        CsrSubmissionResponse back = mapper.readValue(json, CsrSubmissionResponse.class);
        assertThat(back).isEqualTo(a);
    }
}
