package com.godaddy.ans.sdk.pop;

import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;

public record ProofResult(
    X509Certificate cert,
    ECPublicKey key,
    byte[] fingerprint,
    String jkt,
    String jti,
    String htu,
    Instant issuedAt) {
}