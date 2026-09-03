package com.godaddy.ans.sdk.pop;

import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;

/**
 * A verified DPoP proof: the caller's identity certificate and key, the SHA-256
 * fingerprint the status-token binding matches on, the key's RFC 7638 thumbprint
 * for OAuth2 cnf.jkt confirmation, and the proof's jti/htu/iat for the caller
 * binding and structured logging.
 *
 * @param cert        the caller's identity certificate (x5c leaf)
 * @param key         the certificate's P-256 public key
 * @param fingerprint SHA-256 of the certificate DER
 * @param jkt         the RFC 7638 thumbprint of {@code key}. A resource server
 *                    holding a DPoP-bound access token must compare it to the
 *                    token's cnf.jkt claim to complete RFC 9449 §4.3 token
 *                    binding. The ath check alone does not establish
 *                    sender-constraint.
 * @param jti         the proof's unique id, for replay detection
 * @param htu         the normalized target URI the proof is bound to
 * @param issuedAt    the proof's iat
 */
public record ProofResult(
    X509Certificate cert,
    ECPublicKey key,
    byte[] fingerprint,
    String jkt,
    String jti,
    String htu,
    Instant issuedAt) {
}