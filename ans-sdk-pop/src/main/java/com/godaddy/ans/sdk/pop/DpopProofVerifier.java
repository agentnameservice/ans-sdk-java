package com.godaddy.ans.sdk.pop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    private static final int SHA256_BYTES = 32;
    // The only ans_profile revision this verifier implements (ANS-6 §7.12).
    private static final long ANS_PROFILE_REVISION = 1;

    private static final Logger LOG = LoggerFactory.getLogger(DpopProofVerifier.class);

    record Verified(ProofResult result, byte[] contentDigest, String replayKey, Duration replayTtl) {
    }

    /**
     * Verifies a compact DPoP proof against an HTTP method and URL at time
     * {@code now}, with freshness window {@code skew} and replay protection via
     * {@code replay}.
     *
     * <p>Order: size cap, pinned typ/alg plus required jwk/x5c, x5c P-256 leaf,
     * jwk↔x5c key equality, signature under that single key, required
     * ans_content_digest shape, htm, normalized htu, ath vs presented token, iat
     * window, jti bounds, then the received content against ans_content_digest
     * and finally the jti single-use commit. Content is read and replay is
     * recorded LAST, so only proofs that pass every other check consume work or
     * a cache slot.
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
        VerifyOptions effectiveOptions = options != null ? options : VerifyOptions.none();
        try {
            Verified verified = verifyUnrecorded(proofJWS, method, url, now, skew, effectiveOptions);
            verifyContent(verified, effectiveOptions.receivedContent());
            recordReplay(verified, replay);
            LOG.debug("DPoP proof accepted: jti={} htu={}", verified.result().jti(), verified.result().htu());
            return verified.result();
        } catch (PopException e) {
            switch (e.category()) {
                case MISCONFIGURED, REPLAY_CACHE_FULL -> LOG.error("DPoP proof rejected: {} - {}", e.category(),
                    e.getMessage());
                default -> LOG.info("DPoP proof rejected: {} - {}", e.category(), e.getMessage());
            }
            throw e;
        }
    }

    /**
     * Runs every proof check except the content comparison and the replay
     * commit, so a caller that has more trust checks to perform can defer reading
     * content and consuming a cache slot until the proof is known to belong to a
     * vouched agent.
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

        // §7.4 step 3 / §7.5: the x5c leaf's validity period MUST contain the
        // current time. The status token cannot supply this bound — identity-cert
        // rotation is additive and a sealed event's validIdentityCerts array is
        // immutable, so nothing ever prunes a rotated-away or expired certificate.
        // The certificate's own notAfter is the only expiry the system carries for
        // it. Allow the §7.4 step 9 skew tolerance at both edges.
        verifyCertValidity(header.cert(), now, effectiveSkew);

        if (!Jws.verify(header.jws(), header.publicKey())) {
            throw new PopException(ErrorType.SIGNATURE_INVALID, "proof signature is invalid");
        }

        Proof.Claims claims = Proof.parseClaims(header.jws().getPayload());

        verifyProfileRevision(claims.ansProfile());

        byte[] contentDigest = decodeContentDigest(claims.ansContentDigest());

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

        return new Verified(result, contentDigest, replayKey, replayTtl);
    }

    /**
     * Compares the received request content with the proof's ans_content_digest
     * (ANS-6 §7.4 step 12). An absent source means the request carried no
     * content, which binds the empty octet string. Call this only once the proof
     * is otherwise trusted: reading content is the one step whose cost the
     * caller controls, and the spec defers it until after the identity binding.
     */
    void verifyContent(Verified verified, ContentSource content) throws PopException {
        Objects.requireNonNull(verified, "verified");
        byte[] received;
        try {
            received = content != null ? content.read() : Proof.EMPTY_CONTENT;
        } catch (IOException e) {
            throw new PopException(ErrorType.CONTENT_UNREADABLE, "request content could not be read", e);
        }
        if (received == null) {
            throw new PopException(ErrorType.MISCONFIGURED, "content source returned null");
        }
        if (!MessageDigest.isEqual(sha256(received), verified.contentDigest())) {
            throw new PopException(ErrorType.CONTENT_BINDING_MISMATCH,
                "ans_content_digest does not match request content");
        }
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
     * Enforces the ANS-6 §7.12 profile revision before any HTTP binding check.
     * An absent claim means revision 1, and only revision 1 is implemented, so
     * any other revision fails closed here rather than being interpreted under
     * rules this verifier does not have. A non-integral value is already rejected
     * during claim parsing.
     */
    private static void verifyProfileRevision(Long ansProfile) throws PopException {
        if (ansProfile == null || ansProfile == ANS_PROFILE_REVISION) {
            return;
        }
        throw new PopException(ErrorType.UNSUPPORTED_PROFILE,
            "ans_profile revision " + ansProfile + " is not supported");
    }

    /**
     * Decodes the required ans_content_digest claim (ANS-6 §7.2, §7.4 step 2): the
     * unpadded base64url of a SHA-256 digest. Its value is compared against the
     * received content only at step 12, in {@link #verifyContent}.
     */
    private static byte[] decodeContentDigest(String claim) throws PopException {
        if (claim == null) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "ans_content_digest claim is missing");
        }
        byte[] digest;
        try {
            digest = Base64Url.decode(claim);
        } catch (IllegalArgumentException e) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "ans_content_digest is not base64url", e);
        }
        if (digest.length != SHA256_BYTES || claim.indexOf('=') >= 0) {
            throw new PopException(ErrorType.MALFORMED_PROOF,
                "ans_content_digest must be an unpadded base64url SHA-256 digest");
        }
        return digest;
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

    private static void verifyCertValidity(X509Certificate cert, Instant now, Duration skew) throws PopException {
        Instant notBefore = cert.getNotBefore().toInstant();
        Instant notAfter = cert.getNotAfter().toInstant();
        if (now.plus(skew).isBefore(notBefore) || now.minus(skew).isAfter(notAfter)) {
            throw new PopException(ErrorType.CERT_INVALID,
                "x5c leaf certificate validity period does not contain the current time");
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
