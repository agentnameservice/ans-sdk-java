package com.godaddy.ans.sdk.pop;

import java.util.HexFormat;

public record CallerIdentity(
    String ansName,
    String agentId,
    byte[] fingerprint,
    String jkt) {

    public String fingerprintHex() {
        return HexFormat.of().formatHex(fingerprint);
    }
}