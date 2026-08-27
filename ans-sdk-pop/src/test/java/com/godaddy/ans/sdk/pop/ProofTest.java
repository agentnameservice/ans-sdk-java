package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;

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
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class ProofTest {

    private static KeyPair p256A;
    private static KeyPair p256B;
    private static ECKey jwkA;
    private static X509Certificate certA;
    private static X509Certificate certB;
    private static Base64 x5cA;

    private static KeyPair p384;
    private static ECKey jwk384;
    private static X509Certificate cert384;

    private static X509Certificate rsaCert;

    @BeforeAll
    static void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        p256A = ec("secp256r1");
        p256B = ec("secp256r1");
        jwkA = publicEcJwk(p256A, Curve.P_256);
        certA = selfSigned(p256A, "SHA256withECDSA");
        certB = selfSigned(p256B, "SHA256withECDSA");
        x5cA = Base64.encode(certA.getEncoded());

        p384 = ec("secp384r1");
        jwk384 = publicEcJwk(p384, Curve.P_384);
        cert384 = selfSigned(p384, "SHA384withECDSA");

        KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
        rsaGen.initialize(2048);
        rsaCert = selfSigned(rsaGen.generateKeyPair(), "SHA256withRSA");
    }

    @Test
    void acceptReturnsMatchedKeyOnHappyPath() throws Exception {
        String compact = signedProof(header(JWSAlgorithm.ES256, jwkA, List.of(x5cA)), p256A);

        Proof.Header result = Proof.acceptES256DPoP(compact);

        assertThat(result.cert()).isEqualTo(certA);
        assertThat(Proof.coordinates(result.publicKey()))
            .isEqualTo(Proof.coordinates((ECPublicKey) p256A.getPublic()));
    }

    @Test
    void acceptRejectsWrongAlg() throws Exception {
        String compact = signedProof(header(JWSAlgorithm.ES384, jwk384, List.of(x5cA)), p384);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.UNSUPPORTED_ALG);
    }

    @Test
    void acceptRejectsWrongTyp() throws Exception {
        JWSHeader wrongTyp = new JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(new com.nimbusds.jose.JOSEObjectType("jwt"))
            .jwk(jwkA)
            .x509CertChain(List.of(x5cA))
            .build();
        String compact = signedProof(wrongTyp, p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void acceptRejectsPrivateJwk() {
        ECKey privateJwk = new ECKey.Builder(Curve.P_256, (ECPublicKey) p256A.getPublic())
            .privateKey((ECPrivateKey) p256A.getPrivate())
            .build();
        String headerJson = "{\"typ\":\"dpop+jwt\",\"alg\":\"ES256\",\"jwk\":"
            + privateJwk.toJSONString() + ",\"x5c\":[\"" + x5cA.toString() + "\"]}";
        String compact = Base64URL.encode(headerJson) + "." + Base64URL.encode("{}")
            + "." + Base64URL.encode(new byte[]{1, 2, 3});

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void acceptRejectsNonEcJwk() throws Exception {
        KeyPairGenerator rsaGen = KeyPairGenerator.getInstance("RSA");
        rsaGen.initialize(2048);
        RSAKey rsaJwk = new RSAKey.Builder((RSAPublicKey) rsaGen.generateKeyPair().getPublic()).build();
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(Jws.DPOP_TYP)
            .jwk(rsaJwk)
            .x509CertChain(List.of(x5cA))
            .build();
        String compact = signedProof(header, p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void acceptRejectsNonP256Jwk() throws Exception {
        String compact = signedProof(header(JWSAlgorithm.ES256, jwk384, List.of(x5cA)), p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void acceptRejectsMultipleCerts() throws Exception {
        Base64 x5cB = Base64.encode(certB.getEncoded());
        String compact = signedProof(header(JWSAlgorithm.ES256, jwkA, List.of(x5cA, x5cB)), p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void acceptRejectsNonP256LeafCert() throws Exception {
        Base64 x5c384 = Base64.encode(cert384.getEncoded());
        String compact = signedProof(header(JWSAlgorithm.ES256, jwkA, List.of(x5c384)), p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void acceptRejectsNonEcLeafCert() throws Exception {
        Base64 x5cRsa = Base64.encode(rsaCert.getEncoded());
        String compact = signedProof(header(JWSAlgorithm.ES256, jwkA, List.of(x5cRsa)), p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void acceptRejectsCoordMismatch() throws Exception {
        Base64 x5cB = Base64.encode(certB.getEncoded());
        String compact = signedProof(header(JWSAlgorithm.ES256, jwkA, List.of(x5cB)), p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.KEY_MISMATCH);
    }

    @Test
    void matchReturnsKeyForMatchingPair() throws Exception {
        ECPublicKey matched = Proof.matchJWKToCert(jwkA, certA);

        assertThat(Proof.coordinates(matched))
            .isEqualTo(Proof.coordinates((ECPublicKey) p256A.getPublic()));
    }

    @Test
    void matchRejectsNonEcCert() {
        PopException ex = catchThrowableOfType(() -> Proof.matchJWKToCert(jwkA, rsaCert), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void acceptRejectsUnparseableLeafCert() throws Exception {
        Base64 badCert = Base64.encode(new byte[]{1, 2, 3});
        String compact = signedProof(header(JWSAlgorithm.ES256, jwkA, List.of(badCert)), p256A);

        PopException ex = catchThrowableOfType(() -> Proof.acceptES256DPoP(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.CERT_INVALID);
    }

    @Test
    void coordinatesDifferForDifferentKeys() {
        byte[] a = Proof.coordinates((ECPublicKey) p256A.getPublic());
        byte[] b = Proof.coordinates((ECPublicKey) p256B.getPublic());

        assertThat(a).hasSize(64);
        assertThat(a).isNotEqualTo(b);
    }

    @Test
    void coordinatesNormalizeOversizedAndUndersizedFieldElements() {
        // X = 2^256 - 1 -> toByteArray() is 33 bytes (leading sign byte) -> trim path.
        // Y = 1 -> toByteArray() is 1 byte -> left-pad path.
        BigInteger oversizedX = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);
        ECPublicKey key = fixedCoordinateKey(oversizedX, BigInteger.ONE);

        byte[] coords = Proof.coordinates(key);

        assertThat(coords).hasSize(64);
        byte[] expectedX = new byte[32];
        Arrays.fill(expectedX, (byte) 0xFF);
        assertThat(Arrays.copyOfRange(coords, 0, 32)).isEqualTo(expectedX);
        byte[] expectedY = new byte[32];
        expectedY[31] = 1;
        assertThat(Arrays.copyOfRange(coords, 32, 64)).isEqualTo(expectedY);
    }

    @Test
    void normalizeHtuLowercasesSchemeAndHost() throws Exception {
        assertThat(Proof.normalizeHTU("HTTPS://API.Example.COM/Agents"))
            .isEqualTo("https://api.example.com/Agents");
    }

    @Test
    void normalizeHtuDropsDefaultHttpsPort() throws Exception {
        assertThat(Proof.normalizeHTU("https://api.example.com:443/x"))
            .isEqualTo("https://api.example.com/x");
    }

    @Test
    void normalizeHtuDropsDefaultHttpPort() throws Exception {
        assertThat(Proof.normalizeHTU("http://api.example.com:80/x"))
            .isEqualTo("http://api.example.com/x");
    }

    @Test
    void normalizeHtuKeepsNonDefaultPort() throws Exception {
        assertThat(Proof.normalizeHTU("https://api.example.com:8443/x"))
            .isEqualTo("https://api.example.com:8443/x");
    }

    @Test
    void normalizeHtuDropsQueryAndFragment() throws Exception {
        assertThat(Proof.normalizeHTU("https://api.example.com/x?a=1&b=2#frag"))
            .isEqualTo("https://api.example.com/x");
    }

    @Test
    void normalizeHtuEmptyPathBecomesSlash() throws Exception {
        assertThat(Proof.normalizeHTU("https://api.example.com")).isEqualTo("https://api.example.com/");
    }

    @Test
    void normalizeHtuPreservesPathCase() throws Exception {
        assertThat(Proof.normalizeHTU("https://api.example.com/Mixed/Case/Path"))
            .isEqualTo("https://api.example.com/Mixed/Case/Path");
    }

    @Test
    void normalizeHtuDoesNotCanonicalizeDotSegments() throws Exception {
        assertThat(Proof.normalizeHTU("https://api.example.com/a/../b"))
            .isEqualTo("https://api.example.com/a/../b");
    }

    @Test
    void normalizeHtuRejectsMissingScheme() {
        PopException ex = catchThrowableOfType(() -> Proof.normalizeHTU("//api.example.com/x"),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.HTTP_BINDING_MISMATCH);
    }

    @Test
    void normalizeHtuRejectsInvalidUri() {
        PopException ex = catchThrowableOfType(() -> Proof.normalizeHTU("http://exa mple.com/x"),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.HTTP_BINDING_MISMATCH);
    }

    @Test
    void jktMatchesNimbusThumbprint() throws Exception {
        String jkt = Proof.jkt(jwkA);

        assertThat(jkt).isEqualTo(jwkA.computeThumbprint().toString());
        assertThat(jkt).doesNotContain("=");
    }

    @Test
    void accessTokenHashKnownVector() {
        String token = "Kz~8mXK1EalYznwH-LC-1fBAo.4Ljp~zsPE_NeO.gxU";

        assertThat(Proof.accessTokenHash(token)).isEqualTo("fUHyO2r2Z3DZ53EsNrWBb0xWXoaNy59IiKCAqksmQEo");
    }

    @Test
    void parseClaimsRoundTrips() throws Exception {
        Payload payload = new Payload(Map.of(
            "htm", "POST",
            "htu", "https://api.example.com/x",
            "iat", 1700000000L,
            "jti", "unique-id",
            "ath", "abc"));

        Proof.Claims claims = Proof.parseClaims(payload);

        assertThat(claims.htm()).isEqualTo("POST");
        assertThat(claims.htu()).isEqualTo("https://api.example.com/x");
        assertThat(claims.iat()).isEqualTo(Instant.ofEpochSecond(1700000000L));
        assertThat(claims.jti()).isEqualTo("unique-id");
        assertThat(claims.ath()).isEqualTo("abc");
    }

    @Test
    void parseClaimsToleratesExtraClaimsAndMissingAth() throws Exception {
        Payload payload = new Payload(Map.of(
            "htm", "GET",
            "htu", "https://api.example.com/",
            "iat", 1700000000L,
            "jti", "id",
            "extra", "ignored"));

        Proof.Claims claims = Proof.parseClaims(payload);

        assertThat(claims.ath()).isNull();
        assertThat(claims.htm()).isEqualTo("GET");
    }

    @Test
    void parseClaimsRejectsNonStringJti() {
        Payload payload = new Payload(Map.of("jti", 123));

        PopException ex = catchThrowableOfType(() -> Proof.parseClaims(payload), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void parseClaimsRejectsNonNumberIat() {
        Payload payload = new Payload(Map.of("iat", "not-a-number"));

        PopException ex = catchThrowableOfType(() -> Proof.parseClaims(payload), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void parseClaimsRejectsNonObjectPayload() {
        Payload payload = new Payload("not a json object");

        PopException ex = catchThrowableOfType(() -> Proof.parseClaims(payload), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    private static KeyPair ec(String curve) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec(curve));
        return kpg.generateKeyPair();
    }

    private static ECKey publicEcJwk(KeyPair pair, Curve curve) {
        return new ECKey.Builder(curve, (ECPublicKey) pair.getPublic()).build().toPublicJWK();
    }

    private static ECPublicKey fixedCoordinateKey(BigInteger x, BigInteger y) {
        ECPoint point = new ECPoint(x, y);
        return new ECPublicKey() {
            @Override
            public ECPoint getW() {
                return point;
            }

            @Override
            public ECParameterSpec getParams() {
                return null;
            }

            @Override
            public String getAlgorithm() {
                return "EC";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return null;
            }
        };
    }

    private static JWSHeader header(JWSAlgorithm alg, com.nimbusds.jose.jwk.JWK jwk, List<Base64> x5c) {
        return new JWSHeader.Builder(alg)
            .type(Jws.DPOP_TYP)
            .jwk(jwk)
            .x509CertChain(x5c)
            .build();
    }

    private static String signedProof(JWSHeader header, KeyPair signingPair) throws Exception {
        return Jws.sign(header, new Payload("{}"), (ECPrivateKey) signingPair.getPrivate());
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