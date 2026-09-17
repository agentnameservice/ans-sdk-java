package com.godaddy.ans.sdk.pop;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class Base64UrlTest {

    @Test
    void roundTripsArbitraryBytes() {
        byte[] data = new byte[256];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }

        String encoded = Base64Url.encode(data);

        assertThat(Base64Url.decode(encoded)).isEqualTo(data);
    }

    @Test
    void encodesUrlSafeWithoutPadding() {
        byte[] data = "some data.".getBytes(StandardCharsets.UTF_8);

        String encoded = Base64Url.encode(data);

        assertThat(encoded).doesNotContain("=").doesNotContain("+").doesNotContain("/");
        assertThat(Base64Url.decode(encoded)).isEqualTo(data);
    }
}
