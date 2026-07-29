package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class AgentListResponseTest {

    private static AgentListResponse populated() {
        return new AgentListResponse()
                .items(List.of(new AgentListResponseItemsInner()))
                .returnedCount(1)
                .limit(1)
                .nextCursor("x")
                .hasMore(true);
    }

    @Test
    void gettersReflectFluentSetters() {
        AgentListResponse a = populated();
        AgentListResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        AgentListResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new AgentListResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class AgentListResponse");
        assertThat(new AgentListResponse().toString()).contains("class AgentListResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        AgentListResponse a = populated();
        String json = mapper.writeValueAsString(a);
        AgentListResponse back = mapper.readValue(json, AgentListResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addItemsItemInitialiseList() {
        AgentListResponse o = new AgentListResponse();
        o.setItems(null);
        o.addItemsItem(new AgentListResponseItemsInner());
        assertThat(o.getItems()).hasSize(1);
    }
}
