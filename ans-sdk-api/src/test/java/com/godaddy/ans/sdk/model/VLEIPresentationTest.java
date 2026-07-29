package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class VLEIPresentationTest {

    private static VLEIPresentation populated() {
        return new VLEIPresentation()
                .cesr("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        VLEIPresentation a = populated();
        VLEIPresentation b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        VLEIPresentation a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new VLEIPresentation());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class VLEIPresentation");
        assertThat(new VLEIPresentation().toString()).contains("class VLEIPresentation");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        VLEIPresentation a = populated();
        String json = mapper.writeValueAsString(a);
        VLEIPresentation back = mapper.readValue(json, VLEIPresentation.class);
        assertThat(back).isEqualTo(a);
    }
}
