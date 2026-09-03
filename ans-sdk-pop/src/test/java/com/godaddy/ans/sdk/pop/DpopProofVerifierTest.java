package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.util.Base64;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class DpopProofVerifierTest {

    private static final String METHOD = "POST";
    private static final String URL = "https://api.example.com/agents";

    private static KeyPair keyA;
    private static KeyPair keyB;
    private static X509Certificate certA;
    private static byte[] certAder;

    private final DpopProofVerifier verifier = new DpopProofVerifier();

    @BeforeAll
    static void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        keyA = ec("secp256r1");
        keyB = ec("secp256r1");
        certA = selfSigned(keyA);
        certAder = certA.getEncoded();
    }

    private ReplayCache cache() {
        return CaffeineReplayCache.create(1024);
    }

    private PopSigner signerA() throws Exception {
        return PopSigner.create((ECPrivateKey) keyA.getPrivate(), certAder);
    }

    @Test
    void roundTripVerifies() throws Exception {
        String proof = signerA().sign(METHOD, URL);

        ProofResult result = verifier.verify(proof, METHOD, URL, Instant.now(),
            DpopProofVerifier.DEFAULT_SKEW, cache(), null);

        assertThat(result).isNotNull();
        assertThat(result.htu()).isEqualTo("https://api.example.com/agents");
    }

    @Test
    void proofResultFieldsPopulated() throws Exception {
        String proof = signerA().sign(METHOD, URL);

        ProofResult result = verifier.verify(proof, METHOD, URL, Instant.now(),
            null, cache(), VerifyOptions.none());

        assertThat(result.cert()).isEqualTo(certA);
        assertThat(Proof.coordinates(result.key()))
            .isEqualTo(Proof.coordinates((ECPublicKey) keyA.getPublic()));
        assertThat(result.fingerprint()).hasSize(32);
        assertThat(result.fingerprint()).isEqualTo(sha256(certAder));
        assertThat(result.jkt()).isEqualTo(signerA().jkt());
        assertThat(result.jti()).isNotBlank();
        assertThat(result.issuedAt()).isNotNull();
    }

    @Test
    void rejectsTamperedSignature() throws Exception {
        Map<String, Object> claims = baseClaims(Instant.now());
        String proof = craft(claims, (ECPrivateKey) keyB.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.SIGNATURE_INVALID);
    }

    @Test
    void rejectsHtmMismatch() throws Exception {
        String proof = signerA().sign("GET", URL);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.HTTP_BINDING_MISMATCH);
    }

    @Test
    void rejectsHtuMismatch() throws Exception {
        String proof = signerA().sign(METHOD, "https://evil.example.com/agents");

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.HTTP_BINDING_MISMATCH);
    }

    @Test
    void rejectsStaleIat() throws Exception {
        Instant now = Instant.now();
        Map<String, Object> claims = baseClaims(now.minusSeconds(3600));
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, now, null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.PROOF_STALE);
    }

    @Test
    void rejectsFutureIat() throws Exception {
        Instant now = Instant.now();
        Map<String, Object> claims = baseClaims(now.plusSeconds(3600));
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, now, null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.PROOF_STALE);
    }

    @Test
    void rejectsMissingIat() throws Exception {
        Map<String, Object> claims = baseClaims(Instant.now());
        claims.remove("iat");
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void rejectsMissingJti() throws Exception {
        Map<String, Object> claims = baseClaims(Instant.now());
        claims.remove("jti");
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void rejectsEmptyJti() throws Exception {
        Map<String, Object> claims = baseClaims(Instant.now());
        claims.put("jti", "");
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void acceptsIatAtWindowEdges() throws Exception {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");

        String earliest = craft(baseClaims(now.minus(DpopProofVerifier.DEFAULT_SKEW)),
            (ECPrivateKey) keyA.getPrivate());
        assertThat(verifier.verify(earliest, METHOD, URL, now,
            DpopProofVerifier.DEFAULT_SKEW, cache(), null)).isNotNull();

        String latest = craft(baseClaims(now.plus(DpopProofVerifier.DEFAULT_SKEW)),
            (ECPrivateKey) keyA.getPrivate());
        assertThat(verifier.verify(latest, METHOD, URL, now,
            DpopProofVerifier.DEFAULT_SKEW, cache(), null)).isNotNull();
    }

    @Test
    void rejectsOversizeJti() throws Exception {
        Map<String, Object> claims = baseClaims(Instant.now());
        claims.put("jti", "x".repeat(DpopProofVerifier.MAX_JTI_BYTES + 1));
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void rejectsReplay() throws Exception {
        ReplayCache replay = cache();
        String proof = signerA().sign(METHOD, URL);

        verifier.verify(proof, METHOD, URL, Instant.now(), null, replay, null);
        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, replay, null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.REPLAY);
    }

    @Test
    void nullReplayCacheIsMisconfigured() throws Exception {
        String proof = signerA().sign(METHOD, URL);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, null, null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MISCONFIGURED);
    }

    @Test
    void rejectsOversizeProof() {
        String proof = "a".repeat(DpopProofVerifier.MAX_PROOF_SIZE + 1);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void rejectsTokenWithoutAth() throws Exception {
        String proof = signerA().sign(METHOD, URL);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
                VerifyOptions.withAccessToken("some-token")),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.TOKEN_BINDING_MISMATCH);
    }

    @Test
    void rejectsAthWithoutToken() throws Exception {
        String proof = signerA().sign(METHOD, URL, "some-token");

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), VerifyOptions.none()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.TOKEN_BINDING_MISMATCH);
    }

    @Test
    void rejectsAthMismatch() throws Exception {
        String proof = signerA().sign(METHOD, URL, "the-real-token");

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
                VerifyOptions.withAccessToken("a-different-token")),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.TOKEN_BINDING_MISMATCH);
    }

    @Test
    void acceptsMatchingAth() throws Exception {
        String token = "Kz~8mXK1EalYznwH-LC-1fBAo.4Ljp~zsPE_NeO.gxU";
        String proof = signerA().sign(METHOD, URL, token);

        ProofResult result = verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
            VerifyOptions.withAccessToken(token));

        assertThat(result).isNotNull();
    }

    @Test
    void replayKeyIsHashedJti() throws Exception {
        CapturingCache replay = new CapturingCache();
        Map<String, Object> claims = baseClaims(Instant.now());
        claims.put("jti", "fixed-jti-value");
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        verifier.verify(proof, METHOD, URL, Instant.now(), null, replay, null);

        String expected = Base64Url.encode(sha256("fixed-jti-value".getBytes(StandardCharsets.UTF_8)));
        assertThat(replay.lastKey).isEqualTo(expected);
    }

    @Test
    void replayTtlComputedFromInjectedNow() throws Exception {
        CapturingCache replay = new CapturingCache();
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        Instant iat = now.minusSeconds(30);
        Map<String, Object> claims = baseClaims(iat);
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        verifier.verify(proof, METHOD, URL, now, DpopProofVerifier.DEFAULT_SKEW, replay, null);

        Duration expected = Duration.between(now,
            iat.plus(DpopProofVerifier.DEFAULT_SKEW).plus(DpopProofVerifier.REPLAY_GRACE));
        assertThat(replay.lastTtl).isEqualTo(expected);
    }

    @Test
    void toleratesExtraPayloadClaim() throws Exception {
        Map<String, Object> claims = baseClaims(Instant.now());
        claims.put("extra", "ignored");
        String proof = craft(claims, (ECPrivateKey) keyA.getPrivate());

        ProofResult result = verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), null);

        assertThat(result).isNotNull();
    }

    @Test
    void rejectsExpiredCertificate() throws Exception {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        X509Certificate expired = selfSigned(keyA,
            Date.from(now.minusSeconds(7200)), Date.from(now.minusSeconds(3600)));
        String proof = PopSigner.create((ECPrivateKey) keyA.getPrivate(), expired.getEncoded())
            .sign(METHOD, URL);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, now, DpopProofVerifier.DEFAULT_SKEW, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void rejectsNotYetValidCertificate() throws Exception {
        Instant now = Instant.parse("2026-08-28T12:00:00Z");
        X509Certificate notYetValid = selfSigned(keyA,
            Date.from(now.plusSeconds(3600)), Date.from(now.plusSeconds(7200)));
        String proof = PopSigner.create((ECPrivateKey) keyA.getPrivate(), notYetValid.getEncoded())
            .sign(METHOD, URL);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, now, DpopProofVerifier.DEFAULT_SKEW, cache(), null),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void rejectsContentDigestWithoutOption() throws Exception {
        String proof = signerA().sign(METHOD, URL, "body".getBytes(StandardCharsets.UTF_8));

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(), VerifyOptions.none()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CONTENT_BINDING_MISMATCH);
    }

    @Test
    void acceptsMissingContentWhenNotRequired() throws Exception {
        String proof = signerA().sign(METHOD, URL);
        byte[] bodyHash = sha256("body".getBytes(StandardCharsets.UTF_8));

        ProofResult result = verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
            VerifyOptions.none().withContentSha256(bodyHash));

        assertThat(result).isNotNull();
    }

    @Test
    void rejectsMissingContentWhenRequired() throws Exception {
        String proof = signerA().sign(METHOD, URL);
        byte[] bodyHash = sha256("body".getBytes(StandardCharsets.UTF_8));

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
                VerifyOptions.none().withContentSha256(bodyHash).withRequiredContentBinding()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CONTENT_BINDING_MISMATCH);
    }

    @Test
    void acceptsMatchingContent() throws Exception {
        byte[] body = "the-request-body".getBytes(StandardCharsets.UTF_8);
        String proof = signerA().sign(METHOD, URL, body);

        ProofResult result = verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
            VerifyOptions.none().withContentSha256(sha256(body)).withRequiredContentBinding());

        assertThat(result).isNotNull();
    }

    @Test
    void rejectsContentMismatch() throws Exception {
        String proof = signerA().sign(METHOD, URL, "real-body".getBytes(StandardCharsets.UTF_8));
        byte[] otherHash = sha256("tampered-body".getBytes(StandardCharsets.UTF_8));

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
                VerifyOptions.none().withContentSha256(otherHash)),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CONTENT_BINDING_MISMATCH);
    }

    @Test
    void rejectsBadLengthContentSha256() throws Exception {
        String proof = signerA().sign(METHOD, URL);

        PopException ex = catchThrowableOfType(
            () -> verifier.verify(proof, METHOD, URL, Instant.now(), null, cache(),
                VerifyOptions.none().withContentSha256(new byte[16])),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MISCONFIGURED);
    }

    private static Map<String, Object> baseClaims(Instant iat) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("htm", METHOD);
        claims.put("htu", "https://api.example.com/agents");
        claims.put("iat", iat.getEpochSecond());
        claims.put("jti", "test-jti-" + iat.getEpochSecond());
        return claims;
    }

    private static String craft(Map<String, Object> claims, ECPrivateKey signingKey) throws Exception {
        ECKey jwk = new ECKey.Builder(Curve.P_256, (ECPublicKey) keyA.getPublic()).build().toPublicJWK();
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(Jws.DPOP_TYP)
            .jwk(jwk)
            .x509CertChain(List.of(Base64.encode(certAder)))
            .build();
        JWSObject jws = new JWSObject(header, new Payload(claims));
        jws.sign(new ECDSASigner(signingKey));
        return jws.serialize();
    }

    private static byte[] sha256(byte[] input) throws Exception {
        return MessageDigest.getInstance("SHA-256").digest(input);
    }

    private static KeyPair ec(String curve) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec(curve));
        return kpg.generateKeyPair();
    }

    private static X509Certificate selfSigned(KeyPair pair) throws Exception {
        return selfSigned(pair, new Date(1_600_000_000_000L), new Date(4_100_000_000_000L));
    }

    private static X509Certificate selfSigned(KeyPair pair, Date notBefore, Date notAfter) throws Exception {
        X500Name subject = new X500Name("CN=test");
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
            subject, BigInteger.valueOf(1), notBefore, notAfter, subject, pair.getPublic());
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA")
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .build(pair.getPrivate());
        return new JcaX509CertificateConverter()
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .getCertificate(builder.build(signer));
    }

    private static final class CapturingCache implements ReplayCache {
        private String lastKey;
        private Duration lastTtl;

        @Override
        public boolean checkAndStore(String key, Duration ttl) {
            this.lastKey = key;
            this.lastTtl = ttl;
            return false;
        }
    }
}