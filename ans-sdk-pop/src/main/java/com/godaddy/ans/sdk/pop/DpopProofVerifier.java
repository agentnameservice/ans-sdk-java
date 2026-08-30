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

    static final int MAX_PROOF_SIZE = 8 * 1024;
    static final int MAX_JTI_BYTES = 128;
    static final Duration DEFAULT_SKEW = Duration.ofSeconds(120);
    static final Duration REPLAY_GRACE = Duration.ofSeconds(5);

    private static final Logger LOG = LoggerFactory.getLogger(DpopProofVerifier.class);

    record Verified(ProofResult result, String replayKey, Duration replayTtl) {
    }

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

        String replayKey = Base64Url.encode(sha256(jti.getBytes(StandardCharsets.UTF_8)));
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

    void recordReplay(Verified verified, ReplayCache replay) throws PopException {
        Objects.requireNonNull(verified, "verified");
        if (replay == null) {
            throw new PopException(ErrorType.MISCONFIGURED, "replay cache must not be null");
        }
        if (replay.checkAndStore(verified.replayKey(), verified.replayTtl())) {
            throw new PopException(ErrorType.REPLAY, "jti has already been used");
        }
    }

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