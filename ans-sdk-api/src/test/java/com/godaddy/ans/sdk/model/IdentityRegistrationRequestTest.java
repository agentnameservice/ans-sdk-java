package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

class IdentityRegistrationRequestTest {

    private static IdentityRegistrationRequest populated() {
        return new IdentityRegistrationRequest()
                .value("x")
                .vleiPresentation(new VLEIPresentation());
    }

    @Test
    void gettersReflectFluentSetters() {
        IdentityRegistrationRequest a = populated();
        IdentityRegistrationRequest b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        IdentityRegistrationRequest a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new IdentityRegistrationRequest());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class IdentityRegistrationRequest");
        assertThat(new IdentityRegistrationRequest().toString()).contains("class IdentityRegistrationRequest");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        IdentityRegistrationRequest a = populated();
        String json = mapper.writeValueAsString(a);
        IdentityRegistrationRequest back = mapper.readValue(json, IdentityRegistrationRequest.class);
        assertThat(back).isEqualTo(a);
    }
}
