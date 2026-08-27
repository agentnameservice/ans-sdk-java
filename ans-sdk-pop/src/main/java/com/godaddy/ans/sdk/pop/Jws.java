package com.godaddy.ans.sdk.pop;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.text.ParseException;
import java.util.Set;

final class Jws {

    static final JOSEObjectType DPOP_TYP = new JOSEObjectType("dpop+jwt");

    static final Set<String> ALLOWED_HEADER_PARAMS = Set.of("typ", "alg", "jwk", "x5c");

    private Jws() {
    }

    static JWSObject strictParse(String compactJws) throws PopException {
        JWSObject jws;
        try {
            jws = JWSObject.parse(compactJws);
        } catch (ParseException e) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "proof is not a valid compact JWS", e);
        }

        JWSHeader header = jws.getHeader();

        if (!ALLOWED_HEADER_PARAMS.equals(header.getIncludedParams())) {
            throw new PopException(ErrorType.MALFORMED_PROOF,
                "proof header params must be exactly {typ, alg, jwk, x5c}");
        }

        if (!DPOP_TYP.equals(header.getType())) {
            throw new PopException(ErrorType.MALFORMED_PROOF, "proof typ must be dpop+jwt");
        }

        if (!JWSAlgorithm.ES256.equals(header.getAlgorithm())) {
            throw new PopException(ErrorType.UNSUPPORTED_ALG, "proof alg must be ES256");
        }

        return jws;
    }

    static String sign(JWSHeader header, Payload payload, ECPrivateKey key) throws PopException {
        try {
            JWSObject jws = new JWSObject(header, payload);
            jws.sign(new ECDSASigner(key));
            return jws.serialize();
        } catch (JOSEException e) {
            throw new PopException(ErrorType.MISCONFIGURED, "failed to sign DPoP proof", e);
        }
    }

    static boolean verify(JWSObject jws, ECPublicKey key) throws PopException {
        try {
            return jws.verify(new ECDSAVerifier(key));
        } catch (JOSEException e) {
            throw new PopException(ErrorType.SIGNATURE_INVALID, "failed to verify DPoP proof signature", e);
        }
    }
}
