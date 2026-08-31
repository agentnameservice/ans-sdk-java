package com.godaddy.ans.sdk.pop;

import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;

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
import java.net.URI;
import java.net.http.HttpRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PopHttpTest {

    private static PopSigner signer;

    @BeforeAll
    static void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        KeyPair pair = ec("secp256r1");
        X509Certificate cert = selfSigned(pair, "SHA256withECDSA");
        signer = PopSigner.create((ECPrivateKey) pair.getPrivate(), cert.getEncoded());
    }

    @Test
    void attachIdentitySetsDpopAndScittHeaders() throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create("https://api.example.com/agents"))
            .POST(HttpRequest.BodyPublishers.noBody());
        Map<String, List<String>> scitt = Map.of(
            ScittHeaders.SCITT_RECEIPT_HEADER, List.of("receipt-bytes"),
            ScittHeaders.STATUS_TOKEN_HEADER, List.of("token-bytes"));

        PopHttp.attachIdentity(builder, signer, scitt, null);

        HttpRequest request = builder.build();
        assertThat(request.headers().firstValue(PopHttp.DPOP_HEADER)).isPresent();
        assertThat(request.headers().firstValue(ScittHeaders.SCITT_RECEIPT_HEADER))
            .contains("receipt-bytes");
        assertThat(request.headers().firstValue(ScittHeaders.STATUS_TOKEN_HEADER))
            .contains("token-bytes");
    }

    @Test
    void attachIdentityProofBindsMethodAndUrl() throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create("HTTPS://API.Example.COM:443/X?q=1#frag"))
            .DELETE();

        PopHttp.attachIdentity(builder, signer, Map.of(), null);

        Proof.Claims claims = decodeProof(builder);
        assertThat(claims.htm()).isEqualTo("DELETE");
        assertThat(claims.htu()).isEqualTo("https://api.example.com/X");
    }

    @Test
    void attachIdentityWithTokenBindsAth() throws Exception {
        String token = "Kz~8mXK1EalYznwH-LC-1fBAo.4Ljp~zsPE_NeO.gxU";
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create("https://api.example.com/x"))
            .GET();

        PopHttp.attachIdentity(builder, signer, Map.of(), token);

        assertThat(decodeProof(builder).ath()).isEqualTo(Proof.accessTokenHash(token));
    }

    @Test
    void attachIdentityWithoutTokenHasNoAth() throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create("https://api.example.com/x"))
            .GET();

        PopHttp.attachIdentity(builder, signer, Map.of(), null);

        assertThat(decodeProof(builder).ath()).isNull();
    }

    @Test
    void attachIdentityDoesNotSniffAuthorizationHeader() throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create("https://api.example.com/x"))
            .header("Authorization", "DPoP some-token")
            .GET();

        PopHttp.attachIdentity(builder, signer, Map.of(), null);

        assertThat(decodeProof(builder).ath()).isNull();
    }

    @Test
    void accessTokenFromAuthorizationParsesDpopScheme() {
        assertThat(PopHttp.accessTokenFromAuthorization("DPoP abc123")).contains("abc123");
    }

    @Test
    void accessTokenFromAuthorizationCaseInsensitiveScheme() {
        assertThat(PopHttp.accessTokenFromAuthorization("dpop abc123")).contains("abc123");
    }

    @Test
    void accessTokenFromAuthorizationTabSeparatorAndTrim() {
        assertThat(PopHttp.accessTokenFromAuthorization("DPoP\t  abc123 \t")).contains("abc123");
    }

    @Test
    void accessTokenFromAuthorizationRejectsBearer() {
        assertThat(PopHttp.accessTokenFromAuthorization("Bearer abc123")).isEmpty();
    }

    @Test
    void accessTokenFromAuthorizationRejectsSchemeOnly() {
        assertThat(PopHttp.accessTokenFromAuthorization("DPoP")).isEmpty();
    }

    @Test
    void accessTokenFromAuthorizationRejectsMissingSeparator() {
        assertThat(PopHttp.accessTokenFromAuthorization("DPoPabc")).isEmpty();
    }

    @Test
    void accessTokenFromAuthorizationRejectsBlankToken() {
        assertThat(PopHttp.accessTokenFromAuthorization("DPoP   ")).isEmpty();
    }

    @Test
    void accessTokenFromAuthorizationRejectsNull() {
        assertThat(PopHttp.accessTokenFromAuthorization(null)).isEmpty();
    }

    private static Proof.Claims decodeProof(HttpRequest.Builder builder) throws Exception {
        Optional<String> proof = builder.build().headers().firstValue(PopHttp.DPOP_HEADER);
        assertThat(proof).isPresent();
        return Proof.parseClaims(Proof.acceptES256DPoP(proof.get()).jws().getPayload());
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

        ContentSigner contentSigner = new JcaContentSignerBuilder(sigAlg)
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .build(pair.getPrivate());

        return new JcaX509CertificateConverter()
            .setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .getCertificate(builder.build(contentSigner));
    }
}