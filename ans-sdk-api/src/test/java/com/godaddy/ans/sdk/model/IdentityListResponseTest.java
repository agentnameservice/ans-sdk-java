package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class IdentityListResponseTest {

    private static IdentityListResponse populated() {
        return new IdentityListResponse()
                .items(List.of(new IdentityDetails()))
                .returnedCount(1)
                .limit(1)
                .nextCursor("x")
                .hasMore(true);
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityListResponse a = populated();
        IdentityListResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityListResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityListResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityListResponse");
        assertThat(new IdentityListResponse().toString()).contains("class IdentityListResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityListResponse a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityListResponse back = mapper.readValue(json, IdentityListResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addItemsItemInitialiseList() {
        IdentityListResponse o = new IdentityListResponse();
        o.setItems(null);
        o.addItemsItem(new IdentityDetails());
        assertThat(o.getItems()).hasSize(1);
    }
}
