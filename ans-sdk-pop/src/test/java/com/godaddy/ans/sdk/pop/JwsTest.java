package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.util.Base64;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class JwsTest {

    private static ECPrivateKey p256Private;
    private static ECPublicKey p256Public;
    private static ECPublicKey otherPublic;
    private static ECKey p256Jwk;
    private static Base64 x5c;

    private static ECPrivateKey p384Private;
    private static ECKey p384Jwk;

    @BeforeAll
    static void keys() throws Exception {
        KeyPair a = generate("secp256r1");
        p256Private = (ECPrivateKey) a.getPrivate();
        p256Public = (ECPublicKey) a.getPublic();
        p256Jwk = new ECKey.Builder(Curve.P_256, p256Public).build().toPublicJWK();
        x5c = Base64.encode(p256Public.getEncoded());

        otherPublic = (ECPublicKey) generate("secp256r1").getPublic();

        KeyPair b = generate("secp384r1");
        p384Private = (ECPrivateKey) b.getPrivate();
        p384Jwk = new ECKey.Builder(Curve.P_384, (ECPublicKey) b.getPublic()).build().toPublicJWK();
    }

    private static KeyPair generate(String curve) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec(curve));
        return kpg.generateKeyPair();
    }

    private static JWSHeader.Builder dpopHeader(JWSAlgorithm alg, ECKey jwk) {
        return new JWSHeader.Builder(alg)
            .type(Jws.DPOP_TYP)
            .jwk(jwk)
            .x509CertChain(List.of(x5c));
    }

    private static String signed(JWSHeader header, ECPrivateKey key) throws Exception {
        return Jws.sign(header, new Payload("{}"), key);
    }

    @Test
    void strictParseAcceptsValidDpopHeader() throws Exception {
        String compact = signed(dpopHeader(JWSAlgorithm.ES256, p256Jwk).build(), p256Private);

        JWSObject jws = Jws.strictParse(compact);

        assertThat(jws.getHeader().getIncludedParams())
            .isEqualTo(Jws.ALLOWED_HEADER_PARAMS);
    }

    @Test
    void strictParseRejectsExtraHeaderParam() throws Exception {
        String compact = signed(
            dpopHeader(JWSAlgorithm.ES256, p256Jwk).customParam("nonce", "abc").build(),
            p256Private);

        PopException ex = catchThrowableOfType(() -> Jws.strictParse(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void strictParseRejectsMissingHeaderParam() throws Exception {
        JWSHeader noX5c = new JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(Jws.DPOP_TYP)
            .jwk(p256Jwk)
            .build();
        String compact = signed(noX5c, p256Private);

        PopException ex = catchThrowableOfType(() -> Jws.strictParse(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void strictParseRejectsWrongTyp() throws Exception {
        JWSHeader wrongTyp = new JWSHeader.Builder(JWSAlgorithm.ES256)
            .type(new JOSEObjectType("jwt"))
            .jwk(p256Jwk)
            .x509CertChain(List.of(x5c))
            .build();
        String compact = signed(wrongTyp, p256Private);

        PopException ex = catchThrowableOfType(() -> Jws.strictParse(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void strictParseRejectsWrongAlg() throws Exception {
        String compact = signed(dpopHeader(JWSAlgorithm.ES384, p384Jwk).build(), p384Private);

        PopException ex = catchThrowableOfType(() -> Jws.strictParse(compact), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.UNSUPPORTED_ALG);
    }

    @Test
    void strictParseRejectsNonJws() {
        assertThatThrownBy(() -> Jws.strictParse("this-is-not-a-jws"))
            .isInstanceOf(PopException.class)
            .extracting(e -> ((PopException) e).category())
            .isEqualTo(ErrorType.MALFORMED_PROOF);
    }

    @Test
    void verifyRoundTripsUnderMatchedKey() throws Exception {
        String compact = signed(dpopHeader(JWSAlgorithm.ES256, p256Jwk).build(), p256Private);
        JWSObject jws = Jws.strictParse(compact);

        assertThat(Jws.verify(jws, p256Public)).isTrue();
    }

    @Test
    void verifyFailsUnderWrongKey() throws Exception {
        String compact = signed(dpopHeader(JWSAlgorithm.ES256, p256Jwk).build(), p256Private);
        JWSObject jws = Jws.strictParse(compact);

        assertThat(Jws.verify(jws, otherPublic)).isFalse();
    }
}
