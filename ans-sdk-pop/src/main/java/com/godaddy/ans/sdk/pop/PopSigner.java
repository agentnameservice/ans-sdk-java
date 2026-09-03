package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.util.Base64;

import java.io.ByteArrayInputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Mints DPoP proofs for an agent's outbound A2A requests. It holds the agent's
 * identity private key and the DER of the matching identity certificate — the
 * certificate whose fingerprint the agent's status token vouches for. Build one
 * with {@link #create(java.security.interfaces.ECPrivateKey, byte[])}.
 */
public final class PopSigner {

    // jti entropy size (128 bits).
    private static final int JTI_BYTES = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ECPrivateKey privateKey;
    private final byte[] certDer;
    private final ECKey jwk;

    private PopSigner(ECPrivateKey privateKey, byte[] certDer, ECKey jwk) {
        this.privateKey = privateKey;
        this.certDer = certDer;
        this.jwk = jwk;
    }

    /**
     * Builds a signer from a P-256 private key and the DER of the identity
     * certificate that binds the matching public key. It verifies the
     * certificate's public key equals the private key's public key, so a signer
     * can never emit a proof whose jwk or x5c disagrees with its signing key.
     */
    public static PopSigner create(ECPrivateKey key, byte[] certDER) throws PopException {
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(certDER, "certDER");

        X509Certificate cert = parseCertificate(certDER);
        ECPublicKey certKey = ecP256PublicKey(cert);
        assertKeyPairMatches(key, certKey);

        ECKey publicJwk = new ECKey.Builder(Curve.P_256, certKey).build().toPublicJWK();
        return new PopSigner(key, certDER.clone(), publicJwk);
    }

    public String sign(String method, String url) throws PopException {
        return signInternal(method, url, null, null);
    }

    /**
     * Signs a proof and binds an OAuth2 access token via ath =
     * base64url(SHA-256(token)) per RFC 9449 §4.2. Use this when the request
     * presents the token as {@code Authorization: DPoP <token>} (RFC 9449 §7.1).
     * A verifier enforces ath vs presented token in both directions.
     */
    public String sign(String method, String url, String accessToken) throws PopException {
        Objects.requireNonNull(accessToken, "accessToken");
        return signInternal(method, url, accessToken, null);
    }

    /**
     * Signs a proof and binds the request body via ans_content_digest =
     * base64url(SHA-256(content)) per ANS-6 §7.13. An empty body carries no
     * digest claim, so a verifier that does not require content binding still
     * accepts it. A verifier enforces the digest vs the body in both directions.
     */
    public String sign(String method, String url, byte[] content) throws PopException {
        Objects.requireNonNull(content, "content");
        return signInternal(method, url, null, content);
    }

    /**
     * Signs a proof binding both an OAuth2 access token (ath, RFC 9449 §4.2) and
     * the request body (ans_content_digest, ANS-6 §7.13). An empty body carries
     * no digest claim.
     */
    public String sign(String method, String url, String accessToken, byte[] content) throws PopException {
        Objects.requireNonNull(accessToken, "accessToken");
        Objects.requireNonNull(content, "content");
        return signInternal(method, url, accessToken, content);
    }

    /**
     * Returns the RFC 7638 thumbprint of the signer's public key — the value an
     * authorization server records as an access token's cnf.jkt confirmation
     * claim (RFC 9449 §6), and the value a callee compares against
     * {@link CallerIdentity#jkt()}.
     */
    public String jkt() throws PopException {
        return Proof.jkt(jwk);
    }

    private String signInternal(String method, String url, String accessToken, byte[] content) throws PopException {
        Objects.requireNonNull(method, "method");
        Objects.requireNonNull(url, "url");

        String htu = Proof.normalizeHTU(url);

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(Jws.DPOP_TYP)
            .jwk(jwk)
            .x509CertChain(List.of(Base64.encode(certDer)))
            .build();

        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("htm", method);
        claims.put("htu", htu);
        claims.put("iat", Instant.now().getEpochSecond());
        claims.put("jti", newJti());
        if (accessToken != null) {
            claims.put("ath", Proof.accessTokenHash(accessToken));
        }
        if (content != null && content.length > 0) {
            claims.put("ans_content_digest", Proof.contentDigest(content));
        }

        return Jws.sign(header, new Payload(claims), privateKey);
    }

    private static String newJti() {
        byte[] raw = new byte[JTI_BYTES];
        RANDOM.nextBytes(raw);
        return Base64Url.encode(raw);
    }

    private static X509Certificate parseCertificate(byte[] certDER) throws PopException {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certDER));
        } catch (CertificateException e) {
            throw new PopException(ErrorType.CERT_INVALID, "certDER is not a valid X.509 certificate", e);
        }
    }

    private static ECPublicKey ecP256PublicKey(X509Certificate cert) throws PopException {
        if (!(cert.getPublicKey() instanceof ECPublicKey ecPublicKey)) {
            throw new PopException(ErrorType.CERT_INVALID, "certificate key is not EC");
        }
        if (!Curve.P_256.equals(Curve.forECParameterSpec(ecPublicKey.getParams()))) {
            throw new PopException(ErrorType.CERT_INVALID, "certificate key must be P-256");
        }
        return ecPublicKey;
    }

    private static void assertKeyPairMatches(ECPrivateKey key, ECPublicKey certKey) throws PopException {
        try {
            JWSObject probe = new JWSObject(new JWSHeader(JWSAlgorithm.ES256), new Payload("pop-key-check"));
            probe.sign(new ECDSASigner(key));
            if (!probe.verify(new ECDSAVerifier(certKey))) {
                throw new PopException(ErrorType.KEY_MISMATCH, "private key does not match certificate public key");
            }
        } catch (JOSEException e) {
            throw new PopException(ErrorType.KEY_MISMATCH, "private key does not match certificate public key", e);
        }
    }
}