package com.godaddy.ans.sdk.crypto;

import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.AsymmetricJWK;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.util.Base64URL;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdentityProofSignerTest {

    private static final String KID = "did:web:identity.acme-corp.com#key-1";
    // Base64url payload with '-' and '_' so any re-encoding would change it.
    private static final String SIGNING_INPUT = "c2ln-bmlu_Zy1pbnB1dA";

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private final IdentityProofSigner signer = new IdentityProofSigner();

    // ==================== payload verbatim + roundtrip per alg ====================

    @Test
    void signVerifyRoundtripRs256() throws Exception {
        assertRoundTrip(genRsa(2048));
    }

    @Test
    void signVerifyRoundtripEs256() throws Exception {
        assertRoundTrip(genEc("secp256r1", null));
    }

    @Test
    void signVerifyRoundtripEddsa() throws Exception {
        assertRoundTrip(gen("Ed25519"));
    }

    @Test
    void payloadSegmentEqualsSigningInputVerbatim() {
        String jws = signer.sign(SIGNING_INPUT, genRsa(2048).getPrivate(), KID);
        String[] parts = jws.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[1]).isEqualTo(SIGNING_INPUT);
    }

    @Test
    void kidOnlyOverloadOmitsJwk() throws Exception {
        KeyPair kp = genRsa(2048);
        String jws = signer.sign(SIGNING_INPUT, kp.getPrivate(), KID);
        JWSObject parsed = JWSObject.parse(jws);
        assertThat(parsed.getHeader().getKeyID()).isEqualTo(KID);
        assertThat(parsed.getHeader().getJWK()).isNull();
        assertThat(parsed.verify(new RSASSAVerifier((RSAPublicKey) kp.getPublic()))).isTrue();
    }

    @Test
    void kidOnlyOverloadOmitsJwkEs256() throws Exception {
        KeyPair kp = genEc("secp256r1", null);
        String jws = signer.sign(SIGNING_INPUT, kp.getPrivate(), KID);
        JWSObject parsed = JWSObject.parse(jws);
        assertThat(parsed.getHeader().getKeyID()).isEqualTo(KID);
        assertThat(parsed.getHeader().getJWK()).isNull();
        assertThat(parsed.verify(new ECDSAVerifier((ECPublicKey) kp.getPublic()))).isTrue();
    }

    @Test
    void kidOnlyOverloadOmitsJwkEddsa() throws Exception {
        KeyPair kp = gen("Ed25519");
        String jws = signer.sign(SIGNING_INPUT, kp.getPrivate(), KID);
        String[] parts = jws.split("\\.");
        JWSObject parsed = JWSObject.parse(jws);
        assertThat(parsed.getHeader().getKeyID()).isEqualTo(KID);
        assertThat(parsed.getHeader().getJWK()).isNull();
        assertThat(verifies(parts, kp.getPublic())).isTrue();
    }

    // ==================== unsupported key/alg → throw before signing ====================

    @Test
    void rsaBelow2048Throws() {
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, genRsa(1024).getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void secp256k1Throws() throws Exception {
        KeyPair kp = genEc("secp256k1", BouncyCastleProvider.PROVIDER_NAME);
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, kp.getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ecP384Throws() throws Exception {
        KeyPair kp = genEc("secp384r1", null);
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, kp.getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ed448Throws() throws Exception {
        KeyPair kp = gen("Ed448");
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, kp.getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ed448PublicKeyWithEd25519PrivateThrows() throws Exception {
        KeyPair ed25519 = gen("Ed25519");
        PublicKey ed448Public = gen("Ed448").getPublic();
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, ed25519.getPrivate(), KID, ed448Public))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void x25519Throws() throws Exception {
        KeyPair kp = gen("X25519");
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, kp.getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== input validation ====================

    @Test
    void nullSigningInputThrows() {
        assertThatThrownBy(() -> signer.sign(null, genRsa(2048).getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void blankSigningInputThrows() {
        assertThatThrownBy(() -> signer.sign("  ", genRsa(2048).getPrivate(), KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nullPrivateKeyThrows() {
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, null, KID))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void blankKidThrows() {
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, genRsa(2048).getPrivate(), " "))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mismatchedPublicKeyThrows() {
        KeyPair rsa = genRsa(2048);
        PublicKey ecPublic = genEcQuietly().getPublic();
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, rsa.getPrivate(), KID, ecPublic))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mismatchedPublicKeyForEddsaThrows() throws Exception {
        KeyPair ed = gen("Ed25519");
        PublicKey rsaPublic = genRsa(2048).getPublic();
        assertThatThrownBy(() -> signer.sign(SIGNING_INPUT, ed.getPrivate(), KID, rsaPublic))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // ==================== helpers ====================

    private void assertRoundTrip(KeyPair kp) throws Exception {
        String jws = signer.sign(SIGNING_INPUT, kp.getPrivate(), KID, kp.getPublic());
        String[] parts = jws.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[1]).isEqualTo(SIGNING_INPUT);

        JWSObject parsed = JWSObject.parse(jws);
        assertThat(parsed.getHeader().getKeyID()).isEqualTo(KID);

        JWK embeddedJwk = parsed.getHeader().getJWK();
        assertThat(embeddedJwk).isNotNull();
        assertThat(embeddedJwk.isPrivate()).isFalse();

        // Verify against the key recovered from the embedded JWK, not the original key pair.
        // This exercises toPublicJwk(): if it encoded the wrong key material, recovery yields a
        // different key and verification fails.
        PublicKey recoveredPublic = recoverPublicKey(embeddedJwk);
        assertThat(verifies(parts, recoveredPublic)).isTrue();
    }

    /**
     * Rebuilds a JCA public key from the embedded JWK. RSA and EC use Nimbus directly. Ed25519 is
     * reconstructed from its raw x-coordinate wrapped in a SubjectPublicKeyInfo, because Nimbus's
     * OctetKeyPair.toPublicKey() pulls in an optional Tink dependency that is not on the classpath.
     */
    private PublicKey recoverPublicKey(JWK jwk) throws Exception {
        if (jwk instanceof OctetKeyPair okp) {
            byte[] raw = okp.getDecodedX();
            byte[] spki = new byte[SPKI_ED25519_PREFIX.length + raw.length];
            System.arraycopy(SPKI_ED25519_PREFIX, 0, spki, 0, SPKI_ED25519_PREFIX.length);
            System.arraycopy(raw, 0, spki, SPKI_ED25519_PREFIX.length, raw.length);
            return KeyFactory.getInstance("Ed25519").generatePublic(new X509EncodedKeySpec(spki));
        }
        return ((AsymmetricJWK) jwk).toPublicKey();
    }

    // DER prefix for an Ed25519 SubjectPublicKeyInfo: SEQUENCE / AlgorithmIdentifier(1.3.101.112)
    // / BIT STRING, followed by the 32-byte raw public key.
    private static final byte[] SPKI_ED25519_PREFIX = {
        0x30, 0x2a, 0x30, 0x05, 0x06, 0x03, 0x2b, 0x65, 0x70, 0x03, 0x21, 0x00
    };

    /**
     * Verifies the compact JWS signature against the public key. RSA and EC use Nimbus verifiers.
     * Ed25519 uses JCA directly, because Nimbus's Ed25519Verifier pulls in an optional Tink dependency.
     */
    private boolean verifies(String[] parts, PublicKey publicKey) throws Exception {
        if (publicKey instanceof RSAPublicKey rsa) {
            return JWSObject.parse(String.join(".", parts)).verify(new RSASSAVerifier(rsa));
        }
        if (publicKey instanceof ECPublicKey ec) {
            return JWSObject.parse(String.join(".", parts)).verify(new ECDSAVerifier(ec));
        }
        byte[] signingInputBytes = (parts[0] + "." + parts[1]).getBytes(StandardCharsets.US_ASCII);
        byte[] signatureBytes = new Base64URL(parts[2]).decode();
        Signature verifier = Signature.getInstance("Ed25519");
        verifier.initVerify(publicKey);
        verifier.update(signingInputBytes);
        return verifier.verify(signatureBytes);
    }

    private static KeyPair genRsa(int bits) {
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(bits);
            return gen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyPair genEc(String curve, String provider) throws Exception {
        KeyPairGenerator gen = provider == null
            ? KeyPairGenerator.getInstance("EC")
            : KeyPairGenerator.getInstance("EC", provider);
        gen.initialize(new ECGenParameterSpec(curve));
        return gen.generateKeyPair();
    }

    private static KeyPair genEcQuietly() {
        try {
            return genEc("secp256r1", null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyPair gen(String algorithm) throws Exception {
        return KeyPairGenerator.getInstance(algorithm).generateKeyPair();
    }
}
