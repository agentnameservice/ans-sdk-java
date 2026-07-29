package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class ProblemTest {

    private static Problem populated() {
        return new Problem()
                .type("x")
                .title("x")
                .status(1)
                .detail("x")
                .code("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        Problem a = populated();
        Problem b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        Problem a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new Problem());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class Problem");
        assertThat(new Problem().toString()).contains("class Problem");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        Problem a = populated();
        String json = mapper.writeValueAsString(a);
        Problem back = mapper.readValue(json, Problem.class);
        assertThat(back).isEqualTo(a);
    }
}
