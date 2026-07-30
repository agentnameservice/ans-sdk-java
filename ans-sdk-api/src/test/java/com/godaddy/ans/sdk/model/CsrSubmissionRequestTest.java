package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class CsrSubmissionRequestTest {

    private static CsrSubmissionRequest populated() {
        return new CsrSubmissionRequest()
                .csrPEM("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CsrSubmissionRequest a = populated();
        CsrSubmissionRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CsrSubmissionRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CsrSubmissionRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CsrSubmissionRequest");
        assertThat(new CsrSubmissionRequest().toString()).contains("class CsrSubmissionRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CsrSubmissionRequest a = populated();
        String json = mapper.writeValueAsString(a);
        CsrSubmissionRequest back = mapper.readValue(json, CsrSubmissionRequest.class);
        assertThat(back).isEqualTo(a);
    }
}
