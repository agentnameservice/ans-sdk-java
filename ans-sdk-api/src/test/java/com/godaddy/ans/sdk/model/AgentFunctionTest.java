package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class AgentFunctionTest {

    private static AgentFunction populated() {
        return new AgentFunction()
                .id("x")
                .name("x")
                .tags(List.of("x"));
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentFunction a = populated();
        AgentFunction b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentFunction a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentFunction());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentFunction");
        assertThat(new AgentFunction().toString()).contains("class AgentFunction");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentFunction a = populated();
        String json = mapper.writeValueAsString(a);
        AgentFunction back = mapper.readValue(json, AgentFunction.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addTagsItemInitialiseList() {
            AgentFunction o = new AgentFunction();
            o.setTags(null);
            o.addTagsItem("x");
            assertThat(o.getTags()).hasSize(1);
    }
}
