package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class Proof {

    static final int P256_FIELD_BYTES = 32;

    private Proof() {
    }

    record Header(JWSObject jws, ECKey jwk, X509Certificate cert, ECPublicKey publicKey) {
    }

    record Claims(String htm, String htu, Instant iat, String jti, String ath) {
    }

    static Header acceptES256DPoP(String compactJws) throws PopException {
        JWSObject jws = Jws.strictParse(compactJws);
        JWSHeader header = jws.getHeader();

        ECKey jwk = extractPublicEcKey(header);
        X509Certificate cert = extractLeafCertificate(header);
        ECPublicKey matched = matchJWKToCert(jwk, cert);

        return new Header(jws, jwk, cert, matched);
    }

    static ECPublicKey matchJWKToCert(ECKey jwk, X509Certificate cert) throws PopException {
        ECPublicKey jwkKey;
        try {
            jwkKey = jwk.toECPublicKey();
        } catch (JOSEException e) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "jwk is not a usable EC public key", e);
        }

        if (!(cert.getPublicKey() instanceof ECPublicKey certKey)) {
            throw new PopException(ErrorType.CERT_INVALID, "x5c leaf key is not EC");
        }

        if (!Arrays.equals(coordinates(jwkKey), coordinates(certKey))) {
            throw new PopException(ErrorType.KEY_MISMATCH, "jwk coordinates do not match x5c leaf key");
        }

        return jwkKey;
    }

    static byte[] coordinates(ECPublicKey key) {
        ECPoint point = key.getW();
        byte[] x = fieldElement(point.getAffineX());
        byte[] y = fieldElement(point.getAffineY());
        byte[] out = new byte[P256_FIELD_BYTES * 2];
        System.arraycopy(x, 0, out, 0, P256_FIELD_BYTES);
        System.arraycopy(y, 0, out, P256_FIELD_BYTES, P256_FIELD_BYTES);
        return out;
    }

    static String jkt(ECKey jwk) throws PopException {
        try {
            return jwk.computeThumbprint().toString();
        } catch (JOSEException e) {
            throw new PopException(ErrorType.MISCONFIGURED, "failed to compute jwk thumbprint", e);
        }
    }

    static String accessTokenHash(String accessToken) {
        return Base64Url.encode(sha256(accessToken.getBytes(StandardCharsets.UTF_8)));
    }

    static String normalizeHTU(String rawUrl) throws PopException {
        URI uri;
        try {
            uri = new URI(rawUrl);
        } catch (URISyntaxException e) {
            throw new PopException(ErrorType.HTTP_BINDING_MISMATCH, "htu is not a valid URI", e);
        }

        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (scheme == null || host == null) {
            throw new PopException(ErrorType.HTTP_BINDING_MISMATCH, "htu must have scheme and host");
        }
        scheme = scheme.toLowerCase(Locale.ROOT);
        host = host.toLowerCase(Locale.ROOT);

        int port = uri.getPort();
        boolean defaultPort = port == -1
            || (scheme.equals("http") && port == 80)
            || (scheme.equals("https") && port == 443);

        String path = uri.getRawPath();
        if (path == null || path.isEmpty()) {
            path = "/";
        }

        StringBuilder sb = new StringBuilder(scheme).append("://").append(host);
        if (!defaultPort) {
            sb.append(':').append(port);
        }
        return sb.append(path).toString();
    }

    static Claims parseClaims(Payload payload) throws PopException {
        Map<String, Object> map = payload.toJSONObject();
        if (map == null) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "proof payload is not a JSON object");
        }
        return new Claims(
            stringClaim(map, "htm"),
            stringClaim(map, "htu"),
            instantClaim(map, "iat"),
            stringClaim(map, "jti"),
            stringClaim(map, "ath"));
    }

    private static ECKey extractPublicEcKey(JWSHeader header) throws PopException {
        JWK jwk = header.getJWK();
        if (!(jwk instanceof ECKey ecKey)) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "jwk must be an EC key");
        }
        if (!Curve.P_256.equals(ecKey.getCurve())) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "jwk curve must be P-256");
        }
        if (ecKey.isPrivate()) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "jwk must not contain a private key");
        }
        return ecKey;
    }

    private static X509Certificate extractLeafCertificate(JWSHeader header) throws PopException {
        List<Base64> chain = header.getX509CertChain();
        if (chain == null || chain.size() != 1) {
            throw new PopException(ErrorType.CERT_INVALID, "x5c must contain exactly one certificate");
        }

        X509Certificate cert;
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) factory.generateCertificate(
                new ByteArrayInputStream(chain.get(0).decode()));
        } catch (CertificateException e) {
            throw new PopException(ErrorType.CERT_INVALID, "x5c leaf is not a valid X.509 certificate", e);
        }

        if (!(cert.getPublicKey() instanceof ECPublicKey ecPublicKey)) {
            throw new PopException(ErrorType.CERT_INVALID, "x5c leaf key is not EC");
        }
        if (!Curve.P_256.equals(Curve.forECParameterSpec(ecPublicKey.getParams()))) {
            throw new PopException(ErrorType.CERT_INVALID, "x5c leaf key must be P-256");
        }
        return cert;
    }

    private static byte[] fieldElement(BigInteger value) {
        byte[] raw = value.toByteArray();
        if (raw.length == P256_FIELD_BYTES) {
            return raw;
        }
        byte[] out = new byte[P256_FIELD_BYTES];
        if (raw.length > P256_FIELD_BYTES) {
            System.arraycopy(raw, raw.length - P256_FIELD_BYTES, out, 0, P256_FIELD_BYTES);
        } else {
            System.arraycopy(raw, 0, out, P256_FIELD_BYTES - raw.length, raw.length);
        }
        return out;
    }

    private static String stringClaim(Map<String, Object> map, String name) throws PopException {
        Object value = map.get(name);
        if (value == null) {
            return null;
        }
        if (!(value instanceof String s)) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "claim " + name + " must be a string");
        }
        return s;
    }

    private static Instant instantClaim(Map<String, Object> map, String name) throws PopException {
        Object value = map.get(name);
        if (value == null) {
            return null;
        }
        if (!(value instanceof Number n)) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "claim " + name + " must be a number");
        }
        return Instant.ofEpochSecond(n.longValue());
    }

    private static byte[] sha256(byte[] input) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
