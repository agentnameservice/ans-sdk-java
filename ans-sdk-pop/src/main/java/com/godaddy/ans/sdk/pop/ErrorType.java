package com.godaddy.ans.sdk.pop;

/**
 * Classifies a proof-of-possession verification failure, so callers (and the
 * HTTP layer) can branch on a stable category rather than a message string.
 * Every failure on the verify path carries one of these.
 */
public enum ErrorType {
    /**
     * A structurally invalid DPoP proof (bad compact JWS, base64, JSON, a missing
     * required header or claim, or an {@code ans_content_digest} that is not the
     * base64url of a SHA-256 digest).
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
     * The replay cache is at capacity and cannot record the proof id. The proof
     * is rejected (fail closed) rather than admitted, since an unrecorded id
     * reopens the replay window it exists to close.
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
    EXPECTED_PEER_MISMATCH,
    /**
     * The SHA-256 of the received request content does not equal the proof's
     * {@code ans_content_digest} (ANS-6 §7.13): content was added to, removed
     * from, or rewritten in the request after it was signed.
     */
    CONTENT_BINDING_MISMATCH,
    /**
     * The request content could not be read while checking
     * {@code ans_content_digest}: an I/O failure, or content larger than the
     * callee's bound. The proof is rejected without recording its jti.
     */
    CONTENT_UNREADABLE,
    /**
     * The receipt or status token is signed by a key the verifier does not hold
     * and no refresh produced it (ANS-6 §4.5, §9.5). Usually a stale root-key
     * cache after the Transparency Log added a key, not a bad caller.
     */
    UNKNOWN_SIGNING_KEY,
    /**
     * The proof's ans_profile claim selects a rule-set revision this verifier
     * does not implement (ANS-6 §7.12). Absent means revision 1; only revision 1
     * is accepted. Any other value fails closed before the HTTP binding checks.
     */
    UNSUPPORTED_PROFILE
}