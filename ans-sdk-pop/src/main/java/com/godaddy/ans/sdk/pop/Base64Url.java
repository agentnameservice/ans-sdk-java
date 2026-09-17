package com.godaddy.ans.sdk.pop;

import java.util.Base64;
import java.util.Objects;

/** base64url without padding (RFC 7515 §2 / RFC 4648 §5), used for JWS segments. */
public final class Base64Url {

    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    private Base64Url() {
    }

    public static String encode(byte[] data) {
        Objects.requireNonNull(data, "data");
        return ENCODER.encodeToString(data);
    }

    public static byte[] decode(String value) {
        Objects.requireNonNull(value, "value");
        return DECODER.decode(value);
    }
}
