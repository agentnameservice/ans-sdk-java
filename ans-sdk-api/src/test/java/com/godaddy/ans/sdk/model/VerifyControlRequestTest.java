package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

import org.junit.jupiter.api.Test;

class VerifyControlRequestTest {

    private static VerifyControlRequest populated() {
        return new VerifyControlRequest()
                .signedProofs(List.of("x"))
                .cesrSignature("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        VerifyControlRequest a = populated();
        VerifyControlRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        VerifyControlRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new VerifyControlRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class VerifyControlRequest");
        assertThat(new VerifyControlRequest().toString()).contains("class VerifyControlRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        VerifyControlRequest a = populated();
        String json = mapper.writeValueAsString(a);
        VerifyControlRequest back = mapper.readValue(json, VerifyControlRequest.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void addSignedProofsItemInitialiseList() {
        VerifyControlRequest o = new VerifyControlRequest();
        o.setSignedProofs(null);
        o.addSignedProofsItem("x");
        assertThat(o.getSignedProofs()).hasSize(1);
    }
}
