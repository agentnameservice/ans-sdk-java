package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class EventPageResponseTest {

    private static EventPageResponse populated() {
        return new EventPageResponse()
                .items(List.of(new EventItem()))
                .lastLogId("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        EventPageResponse a = populated();
        EventPageResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        EventPageResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new EventPageResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class EventPageResponse");
        assertThat(new EventPageResponse().toString()).contains("class EventPageResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        EventPageResponse a = populated();
        String json = mapper.writeValueAsString(a);
        EventPageResponse back = mapper.readValue(json, EventPageResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addItemsItemInitialiseList() {
        EventPageResponse o = new EventPageResponse();
        o.setItems(null);
        o.addItemsItem(new EventItem());
        assertThat(o.getItems()).hasSize(1);
    }
}
