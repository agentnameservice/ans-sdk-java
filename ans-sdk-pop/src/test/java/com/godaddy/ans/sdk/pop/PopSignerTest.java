package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;

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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class PopSignerTest {

    private static KeyPair p256A;
    private static KeyPair p256B;
    private static KeyPair p384;
    private static X509Certificate certA;
    private static X509Certificate certB;
    private static X509Certificate cert384;
    private static X509Certificate rsaCert;

    @BeforeAll
    static void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        p256A = ec("secp256r1");
        p256B = ec("secp256r1");
        p384 = ec("secp384r1");

        certA = selfSigned(p256A, "SHA256withECDSA");
        certB = selfSigned(p256B, "SHA256withECDSA");
        cert384 = selfSigned(p384, "SHA384withECDSA");

        KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
        rsaGen.initialize(2048);
        rsaCert = selfSigned(rsaGen.generateKeyPair(), "SHA256withRSA");
    }

    @Test
    void createSucceedsForMatchingPair() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        assertThat(signer).isNotNull();
    }

    @Test
    void createRejectsKeyCertMismatch() {
        PopException ex = catchThrowableOfType(
            () -> PopSigner.create((ECPrivateKey) p256A.getPrivate(), certB.getEncoded()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.KEY_MISMATCH);
    }

    @Test
    void createRejectsWrongCurvePrivateKey() {
        PopException ex = catchThrowableOfType(
            () -> PopSigner.create((ECPrivateKey) p384.getPrivate(), certA.getEncoded()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.KEY_MISMATCH);
    }

    @Test
    void createRejectsNonEcCert() {
        PopException ex = catchThrowableOfType(
            () -> PopSigner.create((ECPrivateKey) p256A.getPrivate(), rsaCert.getEncoded()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void createRejectsNonP256Cert() {
        PopException ex = catchThrowableOfType(
            () -> PopSigner.create((ECPrivateKey) p384.getPrivate(), cert384.getEncoded()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void createRejectsUnparseableCert() {
        PopException ex = catchThrowableOfType(
            () -> PopSigner.create((ECPrivateKey) p256A.getPrivate(), new byte[]{1, 2, 3}),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void signRoundTripsThroughAccept() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        String compact = signer.sign("POST", "https://api.example.com/agents");

        Proof.Header header = Proof.acceptES256DPoP(compact);
        assertThat(Proof.coordinates(header.publicKey()))
            .isEqualTo(Proof.coordinates((ECPublicKey) p256A.getPublic()));
        assertThat(header.jws().verify(
            new com.nimbusds.jose.crypto.ECDSAVerifier((ECPublicKey) p256A.getPublic()))).isTrue();
    }

    @Test
    void signHtuMatchesNormalizeHtu() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());
        String url = "HTTPS://API.Example.COM:443/X?q=1#frag";

        String compact = signer.sign("GET", url);

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.htu()).isEqualTo(Proof.normalizeHTU(url));
        assertThat(claims.htu()).isEqualTo("https://api.example.com/X");
    }

    @Test
    void signSetsHtmAndFreshIat() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());
        Instant before = Instant.now().minusSeconds(2);

        String compact = signer.sign("DELETE", "https://api.example.com/x");

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.htm()).isEqualTo("DELETE");
        assertThat(claims.jti()).isNotBlank();
        assertThat(claims.iat()).isAfterOrEqualTo(before);
        assertThat(claims.iat()).isBeforeOrEqualTo(Instant.now().plusSeconds(2));
    }

    @Test
    void signGeneratesUniqueJti() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        Proof.Claims first = Proof.parseClaims(
            Proof.acceptES256DPoP(signer.sign("GET", "https://api.example.com/x")).jws().getPayload());
        Proof.Claims second = Proof.parseClaims(
            Proof.acceptES256DPoP(signer.sign("GET", "https://api.example.com/x")).jws().getPayload());

        assertThat(first.jti()).isNotEqualTo(second.jti());
    }

    @Test
    void signWithAccessTokenAddsAth() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());
        String token = "Kz~8mXK1EalYznwH-LC-1fBAo.4Ljp~zsPE_NeO.gxU";

        String compact = signer.sign("POST", "https://api.example.com/x", token);

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.ath()).isEqualTo(Proof.accessTokenHash(token));
    }

    @Test
    void signWithoutTokenHasNoAth() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        String compact = signer.sign("POST", "https://api.example.com/x");

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.ath()).isNull();
    }

    @Test
    void signWithContentAddsDigest() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());
        byte[] body = "request-body".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        String compact = signer.sign("POST", "https://api.example.com/x", body);

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.ansContentDigest()).isEqualTo(Proof.contentDigest(body));
        assertThat(claims.ath()).isNull();
    }

    @Test
    void signWithEmptyContentHasNoDigest() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        String compact = signer.sign("POST", "https://api.example.com/x", new byte[0]);

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.ansContentDigest()).isNull();
    }

    @Test
    void signWithTokenAndContentAddsBoth() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());
        String token = "Kz~8mXK1EalYznwH-LC-1fBAo.4Ljp~zsPE_NeO.gxU";
        byte[] body = "request-body".getBytes(java.nio.charset.StandardCharsets.UTF_8);

        String compact = signer.sign("POST", "https://api.example.com/x", token, body);

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.ath()).isEqualTo(Proof.accessTokenHash(token));
        assertThat(claims.ansContentDigest()).isEqualTo(Proof.contentDigest(body));
    }

    @Test
    void signEmitsProfileOne() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        String compact = signer.sign("POST", "https://api.example.com/x");

        Proof.Claims claims = Proof.parseClaims(Proof.acceptES256DPoP(compact).jws().getPayload());
        assertThat(claims.ansProfile()).isEqualTo(1L);
    }

    @Test
    void signRejectsInvalidUrl() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());

        PopException ex = catchThrowableOfType(
            () -> signer.sign("GET", "//api.example.com/x"),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.HTTP_BINDING_MISMATCH);
    }

    @Test
    void jktMatchesCertKeyThumbprint() throws Exception {
        PopSigner signer = PopSigner.create((ECPrivateKey) p256A.getPrivate(), certA.getEncoded());
        ECKey certJwk = new ECKey.Builder(Curve.P_256, (ECPublicKey) p256A.getPublic()).build().toPublicJWK();

        assertThat(signer.jkt()).isEqualTo(certJwk.computeThumbprint().toString());
        assertThat(signer.jkt()).doesNotContain("=");
    }

    private static KeyPair ec(String curve) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec(curve));
        return kpg.generateKeyPair();
    }

    private static X509Certificate selfSigned(KeyPair pair, String sigAlg) throws Exception {
        X500Name subject = new X500Name("CN=test");
        BigInteger serial = BigInteger.valueOf(1);
        Date notBefore = new Date(1_600_000_000_000L);
        Date notAfter = new Date(4_100_000_000_000L);

        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
            subject, serial, notBefore, notAfter, subject, pair.getPublic());
        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));

        ContentSigner signer = new JcaContentSignerBuilder(sigAlg)
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .build(pair.getPrivate());

        return new JcaX509CertificateConverter()
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .getCertificate(builder.build(signer));
    }
}