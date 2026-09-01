package com.godaddy.ans.sdk.pop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public final class DpopProofVerifier {

    /**
     * Bounds the compact DPoP proof length to limit parser work on untrusted
     * input.
     */
    static final int MAX_PROOF_SIZE = 8 * 1024;
    /**
     * Bounds the jti claim. RFC 9449 §11.1 calls for rejecting "unnecessarily
     * large jti values" precisely because a verifier stores them: without this,
     * a cache bounded by entry COUNT is unbounded in BYTES. 128 bytes is ample
     * for any collision-resistant identifier.
     */
    static final int MAX_JTI_BYTES = 128;
    /**
     * The freshness window for a proof's iat. A possession proof is single-use
     * and short-lived, so this window is deliberately tight.
     */
    static final Duration DEFAULT_SKEW = Duration.ofSeconds(120);
    /**
     * Keeps a jti in the replay cache slightly past the freshness window, so a
     * replay the freshness check would still accept is always caught by the
     * cache (no boundary gap). Cache retention = iat + skew + grace.
     */
    static final Duration REPLAY_GRACE = Duration.ofSeconds(5);

    private static final Logger LOG = LoggerFactory.getLogger(DpopProofVerifier.class);

    record Verified(ProofResult result, String replayKey, Duration replayTtl) {
    }

    /**
     * Verifies a compact DPoP proof against an HTTP method and URL at time
     * {@code now}, with freshness window {@code skew} and replay protection via
     * {@code replay}.
     *
     * <p>Order: size cap, pinned typ/alg plus required jwk/x5c, x5c P-256 leaf,
     * jwk↔x5c key equality, signature under that single key, htm, normalized
     * htu, ath vs presented token, iat window, then jti single-use. Replay is
     * recorded LAST, so only proofs that pass every other check consume a cache
     * slot.
     *
     * <p>A proof verified here is cryptographically well-formed but NOT yet
     * trusted: nothing has established that its certificate belongs to a live
     * ANS agent (there is no chain validation). Use
     * {@link CallerVerifier#verifyCaller} for the full three-proof check — it
     * records the jti only after the status-token binding succeeds, so an
     * untrusted flood cannot consume replay-cache capacity.
     */
    public ProofResult verify(String proofJWS, String method, String url, Instant now,
                              Duration skew, ReplayCache replay, VerifyOptions options) throws PopException {
        if (replay == null) {
            LOG.error("DPoP proof rejected: replay cache is not configured");
            throw new PopException(ErrorType.MISCONFIGURED, "replay cache must not be null");
        }
        try {
            Verified verified = verifyUnrecorded(proofJWS, method, url, now, skew, options);
            recordReplay(verified, replay);
            LOG.debug("DPoP proof accepted: jti={} htu={}", verified.result().jti(), verified.result().htu());
            return verified.result();
        } catch (PopException e) {
            if (e.category() == ErrorType.MISCONFIGURED) {
                LOG.error("DPoP proof rejected: {} - {}", e.category(), e.getMessage());
            } else {
                LOG.info("DPoP proof rejected: {} - {}", e.category(), e.getMessage());
            }
            throw e;
        }
    }

    /**
     * Runs every proof check except the replay commit, so a caller that has more
     * trust checks to perform can defer consuming a cache slot until the proof is
     * known to belong to a vouched agent.
     */
    Verified verifyUnrecorded(String proofJWS, String method, String url, Instant now,
                              Duration skew, VerifyOptions options) throws PopException {
        Objects.requireNonNull(proofJWS, "proofJWS");
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(url, "url");
        Objects.requireNonNull(now, "now");

        Duration effectiveSkew = skew != null ? skew : DEFAULT_SKEW;
        VerifyOptions effectiveOptions = options != null ? options : VerifyOptions.none();

        if (proofJWS.length() > MAX_PROOF_SIZE) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "proof exceeds maximum size");
        }

        Proof.Header header = Proof.acceptES256DPoP(proofJWS);

        if (!Jws.verify(header.jws(), header.publicKey())) {
            throw new PopException(ErrorType.SIGNATURE_INVALID, "proof signature is invalid");
        }

        Proof.Claims claims = Proof.parseClaims(header.jws().getPayload());

        if (!method.equals(claims.htm())) {
            throw new PopException(ErrorType.HTTP_BINDING_MISMATCH, "htm does not match request method");
        }

        String normalizedHtu = Proof.normalizeHTU(url);
        if (!normalizedHtu.equals(claims.htu())) {
            throw new PopException(ErrorType.HTTP_BINDING_MISMATCH, "htu does not match request url");
        }

        verifyAth(claims.ath(), effectiveOptions.accessToken());

        Instant iat = claims.iat();
        if (iat == null) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "iat claim is missing");
        }
        if (iat.isBefore(now.minus(effectiveSkew)) || iat.isAfter(now.plus(effectiveSkew))) {
            throw new PopException(ErrorType.PROOF_STALE, "iat is outside the acceptable window");
        }

        String jti = claims.jti();
        if (jti == null || jti.isEmpty()) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "jti claim is missing");
        }
        if (jti.getBytes(StandardCharsets.UTF_8).length > MAX_JTI_BYTES) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "jti exceeds maximum size");
        }

        // Store a fixed-width digest of the jti rather than the jti itself, so a
        // cache bounded by entry count is also bounded in bytes (RFC 9449 §11.1
        // sanctions storing "only a hash thereof"). SHA-256 collision resistance
        // preserves single-use semantics.
        String replayKey = Base64Url.encode(sha256(jti.getBytes(StandardCharsets.UTF_8)));
        // Retain the jti until iat + skew + grace, so any replay still inside the
        // freshness window is caught by the cache.
        Duration replayTtl = Duration.between(now, iat.plus(effectiveSkew).plus(REPLAY_GRACE));

        ProofResult result = new ProofResult(
            header.cert(),
            header.publicKey(),
            certFingerprint(header.cert()),
            Proof.jkt(header.jwk()),
            jti,
            normalizedHtu,
            iat);

        return new Verified(result, replayKey, replayTtl);
    }

    /**
     * Records the jti single-use, retaining it until the proof's replay
     * expiry. Call this only once a proof is trusted: the cache is a bounded,
     * shared resource, so recording an unvouched proof lets anyone who can reach
     * the port exhaust capacity and fail authentication for every legitimate
     * caller.
     */
    void recordReplay(Verified verified, ReplayCache replay) throws PopException {
        Objects.requireNonNull(verified, "verified");
        if (replay == null) {
            throw new PopException(ErrorType.MISCONFIGURED, "replay cache must not be null");
        }
        if (replay.checkAndStore(verified.replayKey(), verified.replayTtl())) {
            throw new PopException(ErrorType.REPLAY, "jti has already been used");
        }
    }

    /**
     * Enforces ath vs presented access token, strictly in both directions: a
     * proof minted for a token-bound context is not accepted without its token,
     * and a presented token demands a matching ath (RFC 9449 §4.3).
     */
    private static void verifyAth(String proofAth, String accessToken) throws PopException {
        boolean tokenPresented = accessToken != null;
        boolean athPresent = proofAth != null;
        if (tokenPresented != athPresent) {
            throw new PopException(ErrorType.TOKEN_BINDING_MISMATCH,
                "ath presence does not match presented access token");
        }
        if (!tokenPresented) {
            return;
        }
        String expected = Proof.accessTokenHash(accessToken);
        if (!MessageDigest.isEqual(
                expected.getBytes(StandardCharsets.UTF_8),
                proofAth.getBytes(StandardCharsets.UTF_8))) {
            throw new PopException(ErrorType.TOKEN_BINDING_MISMATCH, "ath does not match presented access token");
        }
    }

    private static byte[] certFingerprint(X509Certificate cert) throws PopException {
        try {
            return sha256(cert.getEncoded());
        } catch (CertificateEncodingException e) {
            throw new PopException(ErrorType.CERT_INVALID, "failed to encode certificate", e);
        }
    }

    private static byte[] sha256(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}