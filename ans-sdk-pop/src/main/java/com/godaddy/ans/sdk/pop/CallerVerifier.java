package com.godaddy.ans.sdk.pop;

import com.godaddy.ans.sdk.crypto.CertificateUtils;
import com.godaddy.ans.sdk.transparency.scitt.DefaultScittVerifier;
import com.godaddy.ans.sdk.transparency.scitt.ScittExpectation;
import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;
import com.godaddy.ans.sdk.transparency.scitt.ScittParseException;
import com.godaddy.ans.sdk.transparency.scitt.ScittReceipt;
import com.godaddy.ans.sdk.transparency.scitt.ScittVerifier;
import com.godaddy.ans.sdk.transparency.scitt.StatusToken;
import com.nimbusds.jose.util.JSONObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class CallerVerifier {

    private static final Logger LOG = LoggerFactory.getLogger(CallerVerifier.class);

    private final DpopProofVerifier proofVerifier = new DpopProofVerifier();
    private final ScittVerifier scittVerifier;
    private final Duration popSkew;

    private CallerVerifier(String expectedIssuer, Duration scittClockSkew, Duration popSkew) {
        Objects.requireNonNull(expectedIssuer, "expectedIssuer");
        this.scittVerifier = new DefaultScittVerifier(
            Objects.requireNonNull(scittClockSkew, "scittClockSkew"), expectedIssuer);
        this.popSkew = Objects.requireNonNull(popSkew, "popSkew");
    }

    CallerVerifier(ScittVerifier scittVerifier, Duration popSkew) {
        this.scittVerifier = Objects.requireNonNull(scittVerifier, "scittVerifier");
        this.popSkew = Objects.requireNonNull(popSkew, "popSkew");
    }

    public static CallerVerifier create(String expectedIssuer) {
        return new CallerVerifier(expectedIssuer, StatusToken.DEFAULT_CLOCK_SKEW, DpopProofVerifier.DEFAULT_SKEW);
    }

    public static CallerVerifier create(String expectedIssuer, Duration scittClockSkew, Duration popSkew) {
        return new CallerVerifier(expectedIssuer, scittClockSkew, popSkew);
    }

    public CallerIdentity verifyCaller(String proofJWS, Map<String, List<String>> headers, String method,
                                       String url, Map<String, PublicKey> rootKeys, ReplayCache replay,
                                       CallerOptions options) throws PopException {
        Objects.requireNonNull(headers, "headers");
        ScittReceipt receipt;
        StatusToken token;
        try {
            receipt = parseReceipt(headers);
            token = parseStatusToken(headers);
        } catch (PopException e) {
            logRejection(e);
            throw e;
        }
        return verifyParsed(proofJWS, receipt, token, method, url, rootKeys, replay, options);
    }

    CallerIdentity verifyParsed(String proofJWS, ScittReceipt receipt, StatusToken token, String method,
                                String url, Map<String, PublicKey> rootKeys, ReplayCache replay,
                                CallerOptions options) throws PopException {
        try {
            if (replay == null) {
                throw new PopException(ErrorType.MISCONFIGURED, "replay cache must not be null");
            }
            if (rootKeys == null) {
                throw new PopException(ErrorType.MISCONFIGURED, "root keys must not be null");
            }

            CallerOptions effectiveOptions = options != null ? options : CallerOptions.none();

            DpopProofVerifier.Verified verified = verifyPossession(proofJWS, method, url, effectiveOptions);
            ProofResult proof = verified.result();

            ScittExpectation expectation = scittVerifier.verify(receipt, token, rootKeys);
            if (!expectation.isVerified()) {
                throw mapExpectation(expectation);
            }

            verifyBinding(proof, receipt, token);

            if (effectiveOptions.expectedPeer() != null
                    && !ansHost(effectiveOptions.expectedPeer()).equals(ansHost(token.ansName()))) {
                throw new PopException(ErrorType.EXPECTED_PEER_MISMATCH,
                    "status token peer does not match expected peer");
            }

            proofVerifier.recordReplay(verified, replay);

            CallerIdentity identity = new CallerIdentity(
                token.ansName(), token.agentId(), proof.fingerprint(), proof.jkt());
            LOG.debug("caller authenticated: ansName={} agentId={}", identity.ansName(), identity.agentId());
            return identity;
        } catch (PopException e) {
            logRejection(e);
            throw e;
        }
    }

    private static void logRejection(PopException e) {
        if (e.category() == ErrorType.MISCONFIGURED) {
            LOG.error("caller rejected: {} - {}", e.category(), e.getMessage());
        } else {
            LOG.info("caller rejected: {} - {}", e.category(), e.getMessage());
        }
    }

    private DpopProofVerifier.Verified verifyPossession(String proofJWS, String method, String url,
                                                        CallerOptions options) throws PopException {
        VerifyOptions verifyOptions = options.accessToken() != null
            ? VerifyOptions.withAccessToken(options.accessToken())
            : VerifyOptions.none();
        Instant now = options.clock() != null ? options.clock() : Instant.now();
        return proofVerifier.verifyUnrecorded(proofJWS, method, url, now, popSkew, verifyOptions);
    }

    private void verifyBinding(ProofResult proof, ScittReceipt receipt, StatusToken token) throws PopException {
        String proofFingerprint = CertificateUtils.computeSha256Fingerprint(proof.cert());
        boolean fingerprintMatched = false;
        for (String expected : token.identityCertFingerprints()) {
            if (CertificateUtils.fingerprintMatches(proofFingerprint, expected)) {
                fingerprintMatched = true;
                break;
            }
        }
        if (!fingerprintMatched) {
            throw new PopException(ErrorType.BINDING_FAILED,
                "proof certificate is not in status token identity fingerprints");
        }

        Optional<String> certAnsName = CertificateUtils.extractAnsName(proof.cert());
        if (certAnsName.isEmpty()) {
            throw new PopException(ErrorType.BINDING_FAILED, "proof certificate has no ans name SAN");
        }
        if (!ansHost(certAnsName.get()).equals(ansHost(token.ansName()))) {
            throw new PopException(ErrorType.BINDING_FAILED,
                "proof ans host does not match status token ans host");
        }

        verifyReceiptAgent(receipt, token);
    }

    private static void verifyReceiptAgent(ScittReceipt receipt, StatusToken token) throws PopException {
        byte[] payload = receipt.eventPayload();
        if (payload == null) {
            throw new PopException(ErrorType.BINDING_FAILED, "receipt has no event payload");
        }
        Map<String, Object> event;
        try {
            event = JSONObjectUtils.parse(new String(payload, StandardCharsets.UTF_8));
        } catch (ParseException e) {
            throw new PopException(ErrorType.BINDING_FAILED, "receipt event payload is not valid JSON", e);
        }
        Object agentId = event.get("agentId");
        if (!(agentId instanceof String eventAgentId) || eventAgentId.isBlank()) {
            throw new PopException(ErrorType.BINDING_FAILED, "receipt event payload has no agent id");
        }
        if (!eventAgentId.equals(token.agentId())) {
            throw new PopException(ErrorType.BINDING_FAILED,
                "receipt agent does not match status token agent");
        }
    }

    private ScittReceipt parseReceipt(Map<String, List<String>> headers) throws PopException {
        String encoded = requireSingleHeader(headers, ScittHeaders.SCITT_RECEIPT_HEADER);
        byte[] decoded = decodeHeader(encoded, "receipt");
        try {
            return ScittReceipt.parse(decoded);
        } catch (ScittParseException e) {
            throw new PopException(ErrorType.RECEIPT_INVALID, "receipt could not be parsed", e);
        }
    }

    private StatusToken parseStatusToken(Map<String, List<String>> headers) throws PopException {
        String encoded = requireSingleHeader(headers, ScittHeaders.STATUS_TOKEN_HEADER);
        byte[] decoded = decodeHeader(encoded, "status token");
        try {
            return StatusToken.parse(decoded);
        } catch (ScittParseException e) {
            throw new PopException(ErrorType.STATUS_INVALID, "status token could not be parsed", e);
        }
    }

    private static byte[] decodeHeader(String encoded, String label) throws PopException {
        try {
            return Base64.getDecoder().decode(encoded.trim());
        } catch (IllegalArgumentException e) {
            throw new PopException(ErrorType.SCITT_HEADER_INVALID, label + " header is not valid base64", e);
        }
    }

    private static String requireSingleHeader(Map<String, List<String>> headers, String name) throws PopException {
        List<String> values = null;
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (entry.getKey() != null && entry.getKey().equalsIgnoreCase(name)) {
                values = entry.getValue();
                break;
            }
        }
        if (values == null || values.isEmpty()) {
            throw new PopException(ErrorType.MISSING_HEADERS, "missing header " + name);
        }
        if (values.size() > 1) {
            throw new PopException(ErrorType.SCITT_HEADER_INVALID, "duplicate header " + name);
        }
        String value = values.get(0);
        if (value == null || value.isBlank()) {
            throw new PopException(ErrorType.MISSING_HEADERS, "empty header " + name);
        }
        return value;
    }

    static String ansHost(String ansName) throws PopException {
        if (ansName == null || ansName.isBlank()) {
            throw new PopException(ErrorType.BINDING_FAILED, "ans name is missing");
        }
        String authority;
        if (ansName.toLowerCase(Locale.ROOT).startsWith("ans://")) {
            try {
                URI uri = new URI(ansName);
                authority = uri.getHost() != null ? uri.getHost() : uri.getAuthority();
            } catch (URISyntaxException e) {
                throw new PopException(ErrorType.BINDING_FAILED, "ans name is not a valid URI", e);
            }
        } else {
            authority = ansName;
        }
        if (authority == null || authority.isBlank()) {
            throw new PopException(ErrorType.BINDING_FAILED, "ans name has no authority");
        }
        return authority.toLowerCase(Locale.ROOT).replaceFirst("^v\\d+\\.\\d+\\.\\d+\\.", "");
    }

    private static PopException mapExpectation(ScittExpectation expectation) {
        ErrorType type = switch (expectation.status()) {
            case INVALID_RECEIPT -> ErrorType.RECEIPT_INVALID;
            case INVALID_TOKEN, TOKEN_EXPIRED, AGENT_REVOKED, AGENT_INACTIVE, KEY_NOT_FOUND -> ErrorType.STATUS_INVALID;
            case PARSE_ERROR, NOT_PRESENT, VERIFIED -> ErrorType.SCITT_HEADER_INVALID;
        };
        return new PopException(type, expectation.failureReason());
    }
}