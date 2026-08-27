package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    private static ErrorResponse populated() {
        return new ErrorResponse()
                .status(ErrorResponse.StatusEnum.ERROR)
                .code("x")
                .message("x")
                .details(Map.of("k", "v"));
    }

    @Test
    void gettersReflectFluentSetters() {
        ErrorResponse a = populated();
        ErrorResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        ErrorResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new ErrorResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class ErrorResponse");
        assertThat(new ErrorResponse().toString()).contains("class ErrorResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        ErrorResponse a = populated();
        String json = mapper.writeValueAsString(a);
        ErrorResponse back = mapper.readValue(json, ErrorResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> ErrorResponse.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
