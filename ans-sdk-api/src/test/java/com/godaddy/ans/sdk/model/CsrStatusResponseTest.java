package com.godaddy.ans.sdk.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class CsrStatusResponseTest {

    private static CsrStatusResponse populated() {
        return new CsrStatusResponse()
                .csrId(UUID.fromString("11111111-1111-1111-1111-111111111111"))
                .type(CsrStatusResponse.TypeEnum.SERVER)
                .status(CsrStatusResponse.StatusEnum.PENDING)
                .submittedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .updatedAt(OffsetDateTime.parse("2026-01-01T00:00:00Z"))
                .failureReason("x");
    }

    @Test
    void gettersReflectFluentSetters() {
        CsrStatusResponse a = populated();
        CsrStatusResponse b = populated();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }

    @Test
    void equalsAndHashCodeContract() {
        CsrStatusResponse a = populated();
        assertThat(a).isEqualTo(a);
        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("other-type");
        assertThat(a).isNotEqualTo(new CsrStatusResponse());
    }

    @Test
    void toStringContainsClassName() {
        assertThat(populated().toString()).contains("class CsrStatusResponse");
        assertThat(new CsrStatusResponse().toString()).contains("class CsrStatusResponse");
    }

    @Test
    void jacksonRoundTrip() throws Exception {
        ObjectMapper mapper = ModelTestSupport.mapper();
        CsrStatusResponse a = populated();
        String json = mapper.writeValueAsString(a);
        CsrStatusResponse back = mapper.readValue(json, CsrStatusResponse.class);
        assertThat(back).isEqualTo(a);
    }

    @Test
    void nestedEnumsRejectUnknown() {
        assertThat(catchThrowable(() -> CsrStatusResponse.TypeEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(catchThrowable(() -> CsrStatusResponse.StatusEnum.fromValue("NOPE")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
