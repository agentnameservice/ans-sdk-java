package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class EventItemTest {

    private static EventItem populated() {
        return new EventItem()
                .logId("x")
                .eventType(EventItem.EventTypeEnum.AGENT_DEPRECATED)
                .createdAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .expiresAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .agentId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .ansName("x")
                .agentHost("x")
                .agentDisplayName("x")
                .agentDescription("x")
                .version("x")
                .providerId("x")
                .endpoints(List.of(new AgentEndpoint()));
    }

    @Test
    void gettersReflectFluentSetters() {
        EventItem a = populated();
        EventItem b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        EventItem a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new EventItem());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class EventItem");
        assertThat(new EventItem().toString()).contains("class EventItem");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        EventItem a = populated();
        String json = mapper.writeValueAsString(a);
        EventItem back = mapper.readValue(json, EventItem.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addEndpointsItemInitialiseList() {
        EventItem o = new EventItem();
        o.setEndpoints(null);
        o.addEndpointsItem(new AgentEndpoint());
        assertThat(o.getEndpoints()).hasSize(1);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> EventItem.EventTypeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
