package com.godaddy.ans.sdk.pop;

import java.util.Objects;

public final class VerifyOptions {

    private final String accessToken;

    private VerifyOptions(String accessToken) {
        this.accessToken = accessToken;
    }

    public static VerifyOptions none() {
        return new VerifyOptions(null);
    }

    public static VerifyOptions withAccessToken(String accessToken) {
        return new VerifyOptions(Objects.requireNonNull(accessToken, "accessToken"));
    }

    String accessToken() {
        return accessToken;
    }
}