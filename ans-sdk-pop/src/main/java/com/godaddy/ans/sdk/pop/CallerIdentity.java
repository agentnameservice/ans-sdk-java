package com.godaddy.ans.sdk.pop;

import java.util.HexFormat;

/**
 * The authenticated identity of an A2A caller.
 *
 * <p>This is the result of AUTHENTICATION, not authorization. A returned
 * {@code CallerIdentity} means the request provably came from this agent. The
 * callee must still decide whether this agent may perform the requested action.
 *
 * @param ansName     the caller's ans:// name, from the verified status token
 * @param agentId     the caller's agent id, from the verified status token
 * @param fingerprint SHA-256 of the identity certificate that signed the proof
 * @param jkt         the RFC 7638 thumbprint of the key that signed the proof. A
 *                    callee that also accepts a DPoP-bound OAuth2 access token
 *                    must compare this to the token's cnf.jkt claim to complete
 *                    RFC 9449 §4.3 token binding. The ath check alone proves only
 *                    that proof and token were presented together, not that the
 *                    token was issued to this key.
 */
public record CallerIdentity(
    String ansName,
    String agentId,
    byte[] fingerprint,
    String jkt) {

    /** Returns the identity-certificate fingerprint as lowercase hex. */
    public String fingerprintHex() {
        return HexFormat.of().formatHex(fingerprint);
    }
}