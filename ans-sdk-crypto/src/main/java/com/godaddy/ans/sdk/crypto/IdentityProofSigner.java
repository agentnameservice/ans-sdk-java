package com.godaddy.ans.sdk.crypto;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.EdECPrivateKey;
import java.security.interfaces.EdECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

/**
 * Signs Verified-Identity control-proof challenges as compact JWS strings.
 *
 * <p>The Registration Authority (RA) serves a {@code signingInput} — the base64url of the
 * canonical proof bytes — in the {@code 202} challenge round. This signer produces one compact
 * JWS per proven key, suitable for the {@code signedProofs} array of a verify-control request.</p>
 *
 * <p>The served {@code signingInput} becomes the JWS payload segment <b>verbatim</b>: the RA checks
 * payload equality before it checks the signature, so the client never canonicalizes or re-encodes
 * it. The protected header always carries {@code kid} and may carry the public {@code jwk}.</p>
 *
 * <p>This signer supports only the algorithms the verifier implements: EdDSA (Ed25519), ES256
 * (ECDSA P-256), and RS256 (RSA &gt;= 2048). It infers the algorithm from the private key. It
 * rejects key-agreement keys (X25519) and curves with no verifier (secp256k1, P-384, P-521)
 * before it signs.</p>
 */
public final class IdentityProofSigner {

    private static final Logger LOG = LoggerFactory.getLogger(IdentityProofSigner.class);

    private static final int MIN_RSA_KEY_BITS = 2048;
    private static final int ED25519_RAW_KEY_LEN = 32;
    private static final String ED25519 = "Ed25519";

    /**
     * Creates a new IdentityProofSigner.
     */
    public IdentityProofSigner() {
        // Default constructor
    }

    /**
     * Signs the served {@code signingInput} and returns a compact JWS with {@code kid} in the
     * protected header.
     *
     * @param signingInput the base64url signing input served by the RA, used as the JWS payload verbatim
     * @param privateKey   the private key that proves control of the identifier
     * @param kid          the verification-method id claimed by this proof
     * @return the compact JWS ({@code header.payload.signature})
     * @throws IllegalArgumentException if an argument is missing or the key/algorithm is unsupported
     * @throws RuntimeException         if signing fails
     */
    public String sign(String signingInput, PrivateKey privateKey, String kid) {
        return sign(signingInput, privateKey, kid, null);
    }

    /**
     * Signs the served {@code signingInput} and returns a compact JWS with {@code kid} and the
     * public {@code jwk} in the protected header.
     *
     * <p>The embedded {@code jwk} is public-only. It is required by the quickstart noop resolver and
     * ignored by the web resolver, which always uses the resolved DID document.</p>
     *
     * @param signingInput the base64url signing input served by the RA, used as the JWS payload verbatim
     * @param privateKey   the private key that proves control of the identifier
     * @param kid          the verification-method id claimed by this proof
     * @param publicKey    the public key to embed as {@code jwk}, or {@code null} to omit it
     * @return the compact JWS ({@code header.payload.signature})
     * @throws IllegalArgumentException if an argument is missing or the key/algorithm is unsupported
     * @throws RuntimeException         if signing fails
     */
    public String sign(String signingInput, PrivateKey privateKey, String kid, PublicKey publicKey) {
        if (signingInput == null || signingInput.isBlank()) {
            throw new IllegalArgumentException("signingInput cannot be null or blank");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("privateKey cannot be null");
        }
        if (kid == null || kid.isBlank()) {
            throw new IllegalArgumentException("kid cannot be null or blank");
        }

        JWSAlgorithm algorithm = resolveAlgorithm(privateKey);
        LOG.debug("Signing identity proof with algorithm {} and kid {}", algorithm, kid);

        JWSHeader.Builder headerBuilder = new JWSHeader.Builder(algorithm).keyID(kid);
        if (publicKey != null) {
            headerBuilder.jwk(toPublicJwk(algorithm, publicKey));
        }
        JWSHeader header = headerBuilder.build();

        String headerSegment = header.toBase64URL().toString();
        byte[] signingInputBytes = (headerSegment + "." + signingInput).getBytes(StandardCharsets.US_ASCII);

        Base64URL signature = computeSignature(algorithm, privateKey, header, signingInputBytes);
        return headerSegment + "." + signingInput + "." + signature;
    }

    /**
     * Resolves the JWS algorithm from the private key, rejecting unsupported keys before signing.
     */
    private JWSAlgorithm resolveAlgorithm(PrivateKey privateKey) {
        if (privateKey instanceof RSAPrivateKey rsaKey) {
            int bits = rsaKey.getModulus().bitLength();
            if (bits < MIN_RSA_KEY_BITS) {
                throw new IllegalArgumentException(
                    "RSA key must be at least " + MIN_RSA_KEY_BITS + " bits, was " + bits);
            }
            return JWSAlgorithm.RS256;
        }
        if (privateKey instanceof ECPrivateKey ecKey) {
            Curve curve = Curve.forECParameterSpec(ecKey.getParams());
            if (!Curve.P_256.equals(curve)) {
                throw new IllegalArgumentException(
                    "Unsupported EC curve for ES256 (only P-256 is supported): " + curve);
            }
            return JWSAlgorithm.ES256;
        }
        if (privateKey instanceof EdECPrivateKey edKey) {
            String curveName = edKey.getParams().getName();
            if (!ED25519.equals(curveName)) {
                throw new IllegalArgumentException(
                    "Unsupported EdDSA curve (only Ed25519 is supported): " + curveName);
            }
            return JWSAlgorithm.EdDSA;
        }
        throw new IllegalArgumentException(
            "Unsupported key type for identity proof: " + privateKey.getAlgorithm());
    }

    /**
     * Computes the JWS signature over the signing input bytes for the resolved algorithm.
     */
    private Base64URL computeSignature(JWSAlgorithm algorithm, PrivateKey privateKey,
                                       JWSHeader header, byte[] signingInputBytes) {
        try {
            if (JWSAlgorithm.EdDSA.equals(algorithm)) {
                // Ed25519 JCA signatures are already the raw R||S form JOSE expects, no transcoding needed.
                Signature signature = Signature.getInstance(ED25519);
                signature.initSign(privateKey);
                signature.update(signingInputBytes);
                return Base64URL.encode(signature.sign());
            }
            JWSSigner signer = JWSAlgorithm.RS256.equals(algorithm)
                ? new RSASSASigner(privateKey)
                : new ECDSASigner(privateKey, Curve.P_256);
            return signer.sign(header, signingInputBytes);
        } catch (GeneralSecurityException | JOSEException e) {
            throw new IllegalStateException(
                "Failed to sign identity proof (alg=" + algorithm + ", kid=" + header.getKeyID() + ")", e);
        }
    }

    /**
     * Builds a public-only JWK for the given public key and resolved algorithm.
     */
    private JWK toPublicJwk(JWSAlgorithm algorithm, PublicKey publicKey) {
        try {
            if (JWSAlgorithm.RS256.equals(algorithm)) {
                return new RSAKey.Builder((RSAPublicKey) publicKey).build();
            }
            if (JWSAlgorithm.ES256.equals(algorithm)) {
                return new ECKey.Builder(Curve.P_256, (ECPublicKey) publicKey).build();
            }
            // EdDSA: the raw 32-byte public key is the tail of the X.509 SubjectPublicKeyInfo encoding.
            if (!(publicKey instanceof EdECPublicKey)) {
                throw new IllegalArgumentException("publicKey does not match the private key algorithm");
            }
            byte[] encoded = publicKey.getEncoded();
            byte[] raw = Arrays.copyOfRange(encoded, encoded.length - ED25519_RAW_KEY_LEN, encoded.length);
            return new OctetKeyPair.Builder(Curve.Ed25519, Base64URL.encode(raw)).build();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("publicKey does not match the private key algorithm", e);
        }
    }
}
