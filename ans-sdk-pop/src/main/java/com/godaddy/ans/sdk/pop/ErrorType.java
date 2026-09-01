package com.godaddy.ans.sdk.pop;

/**
 * Classifies a proof-of-possession verification failure, so callers (and the
 * HTTP layer) can branch on a stable category rather than a message string.
 * Every failure on the verify path carries one of these.
 */
public enum ErrorType {
    /**
     * A structurally invalid DPoP proof (bad compact JWS, base64, JSON, or a
     * missing required header or claim).
     */
    MALFORMED_PROOF,
    /**
     * A proof whose alg or typ is not the pinned ES256 / {@code dpop+jwt} pair
     * (this covers the alg:"none" downgrade), or a jwk that is not EC/P-256.
     */
    UNSUPPORTED_ALG,
    /** An htm or htu that does not match the request. */
    HTTP_BINDING_MISMATCH,
    /**
     * An iat outside the accepted freshness window (too old or too far in the
     * future).
     */
    PROOF_STALE,
    /** A jti already seen within the freshness window. */
    REPLAY,
    /**
     * Reserved: the replay cache is at capacity and cannot record the proof id.
     * The in-process cache does not raise this today — see the replay-cache
     * capacity finding.
     */
    REPLAY_CACHE_FULL,
    /** A proof whose signature does not verify under the x5c leaf key. */
    SIGNATURE_INVALID,
    /**
     * A missing or unparseable x5c, or a leaf key that is not ECDSA P-256.
     */
    CERT_INVALID,
    /**
     * The header's jwk and x5c leaf do not present the same public key — the
     * dual-header consistency invariant failed.
     */
    KEY_MISMATCH,
    /**
     * The proof's ath claim and the presented OAuth2 access token disagree: ath
     * present with no token presented, absent when one was, or a hash mismatch
     * (RFC 9449 §4.3 / §7.1).
     */
    TOKEN_BINDING_MISMATCH,
    /**
     * A verified proof and a verified status token do not describe the same
     * agent (fingerprint, {@code ans://} SAN, or receipt agent mismatch).
     */
    BINDING_FAILED,
    /**
     * The SCITT status token failed verification (bad signature, expired,
     * terminal status, or malformed).
     */
    STATUS_INVALID,
    /**
     * The SCITT receipt failed verification, or its leaf event could not be
     * decoded.
     */
    RECEIPT_INVALID,
    /**
     * The request carried no SCITT receipt or status token, or no DPoP proof.
     */
    MISSING_HEADERS,
    /**
     * The X-SCITT-Receipt or X-ANS-Status-Token header could not be extracted
     * (missing, duplicated, or not valid base64).
     */
    SCITT_HEADER_INVALID,
    /**
     * A required dependency or argument was not supplied (a null replay cache,
     * root keys, or signer). This is a wiring error, not attacker-influenced
     * input. Verification fails closed.
     */
    MISCONFIGURED,
    /**
     * The proven caller is not the peer the callee was configured to accept
     * (see {@link CallerOptions#withExpectedPeer(String)}).
     */
    EXPECTED_PEER_MISMATCH
}