package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Shared JSON mapper for model round-trip tests. Registers the JSR-310 module so
 * {@link java.time.OffsetDateTime} fields serialize and deserialize, and writes
 * dates as ISO-8601 strings so a round-trip preserves value equality.
 */
final class ModelTestSupport {

    private ModelTestSupport() {
    }

    static ObjectMapper mapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
