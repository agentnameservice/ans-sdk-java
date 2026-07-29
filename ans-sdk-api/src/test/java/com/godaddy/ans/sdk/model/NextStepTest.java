package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;

import org.junit.jupiter.api.Test;

class NextStepTest {

    private static NextStep populated() {
        return new NextStep()
                .action(NextStep.ActionEnum.CONFIGURE_DNS)
                .description("x")
                .endpoint(URI.create("https://example.com/x"))
                .estimatedTimeMinutes(1);
    }

    @Test
    void gettersReflectFluentSetters() {
        NextStep a = populated();
        NextStep b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        NextStep a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new NextStep());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class NextStep");
        assertThat(new NextStep().toString()).contains("class NextStep");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        NextStep a = populated();
        String json = mapper.writeValueAsString(a);
        NextStep back = mapper.readValue(json, NextStep.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> NextStep.ActionEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
