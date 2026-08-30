package com.godaddy.ans.sdk.pop;

import com.godaddy.ans.sdk.crypto.CertificateUtils;
import com.godaddy.ans.sdk.transparency.model.CertType;
import com.godaddy.ans.sdk.transparency.model.CertificateInfo;
import com.godaddy.ans.sdk.transparency.scitt.ScittExpectation;
import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;
import com.godaddy.ans.sdk.transparency.scitt.ScittReceipt;
import com.godaddy.ans.sdk.transparency.scitt.ScittVerifier;
import com.godaddy.ans.sdk.transparency.scitt.StatusToken;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
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
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECGenParameterSpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

class CallerVerifierTest {

    private static final String METHOD = "POST";
    private static final String URL = "https://rp.example.com/verify";
    private static final String AGENT_ID = "agent-123";
    private static final String ANS_NAME = "ans://agent.example.com";

    private static KeyPair keyPair;
    private static X509Certificate cert;
    private static String certFingerprint;
    private static String proofJws;

    private static KeyPair noSanKeyPair;
    private static X509Certificate noSanCert;
    private static String noSanFingerprint;
    private static String noSanProofJws;

    @BeforeAll
    static void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        keyPair = ec();
        cert = selfSigned(keyPair, ANS_NAME);
        certFingerprint = CertificateUtils.computeSha256Fingerprint(cert);
        proofJws = PopSigner.create((ECPrivateKey) keyPair.getPrivate(), cert.getEncoded()).sign(METHOD, URL);

        noSanKeyPair = ec();
        noSanCert = selfSigned(noSanKeyPair, null);
        noSanFingerprint = CertificateUtils.computeSha256Fingerprint(noSanCert);
        noSanProofJws = PopSigner.create((ECPrivateKey) noSanKeyPair.getPrivate(), noSanCert.getEncoded())
            .sign(METHOD, URL);
    }

    @Test
    void happyPathReturnsIdentity() throws Exception {
        CountingReplay replay = new CountingReplay(false);
        CallerIdentity identity = verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), replay, CallerOptions.none());

        assertThat(identity.ansName()).isEqualTo(ANS_NAME);
        assertThat(identity.agentId()).isEqualTo(AGENT_ID);
        assertThat(identity.jkt()).isNotBlank();
        assertThat(identity.fingerprintHex()).hasSize(64);
        assertThat(replay.calls).isEqualTo(1);
    }

    @Test
    void bindingRejectsFingerprintNotInStatusToken() {
        CountingReplay replay = new CountingReplay(false);
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, "SHA256:deadbeef"),
            METHOD, URL, Map.of(), replay, CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.BINDING_FAILED);
        assertThat(replay.calls).isZero();
    }

    @Test
    void bindingRejectsAnsHostMismatch() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, "ans://other.example.com"),
            token("ans://other.example.com", AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.BINDING_FAILED);
    }

    @Test
    void bindingAcceptsVersionLabelHost() throws Exception {
        String versioned = "ans://v1.2.3.agent.example.com";
        CallerIdentity identity = verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, versioned), token(versioned, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none());

        assertThat(identity.ansName()).isEqualTo(versioned);
    }

    @Test
    void bindingRejectsNoSan() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            noSanProofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, noSanFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.BINDING_FAILED);
    }

    @Test
    void bindingRejectsReceiptAgentMismatch() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt("other-agent", ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.BINDING_FAILED);
    }

    @Test
    void bindingRejectsMissingReceiptAgent() {
        ScittReceipt receipt = new ScittReceipt(null, null, null,
            "{\"ansName\":\"ans://agent.example.com\"}".getBytes(StandardCharsets.UTF_8), null);
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt, token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.BINDING_FAILED);
    }

    @Test
    void replayNotConsumedWhenLaterCheckFails() {
        CountingReplay replay = new CountingReplay(false);
        catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), replay,
            CallerOptions.none().withExpectedPeer("ans://other.example.com")), PopException.class);

        assertThat(replay.calls).isZero();
    }

    @Test
    void replayDetectedRejects() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(true), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.REPLAY);
    }

    @Test
    void expectedPeerMatchAccepts() throws Exception {
        CallerIdentity identity = verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false),
            CallerOptions.none().withExpectedPeer("ans://agent.example.com"));

        assertThat(identity.agentId()).isEqualTo(AGENT_ID);
    }

    @Test
    void expectedPeerMismatchRejects() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false),
            CallerOptions.none().withExpectedPeer("ans://other.example.com")), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.EXPECTED_PEER_MISMATCH);
    }

    @Test
    void nullReplayCacheRejected() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), null, CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MISCONFIGURED);
    }

    @Test
    void nullRootKeysRejected() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, null, new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MISCONFIGURED);
    }

    @Test
    void expiredStatusTokenMapsToStatusInvalid() {
        CallerVerifier verifier = new CallerVerifier(new FakeScitt(ScittExpectation.expired()), DEFAULT_SKEW);
        PopException ex = catchThrowableOfType(() -> verifier.verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.STATUS_INVALID);
    }

    @Test
    void invalidReceiptMapsToReceiptInvalid() {
        CallerVerifier verifier = new CallerVerifier(
            new FakeScitt(ScittExpectation.invalidReceipt("bad")), DEFAULT_SKEW);
        PopException ex = catchThrowableOfType(() -> verifier.verifyParsed(
            proofJws, receipt(AGENT_ID, ANS_NAME), token(ANS_NAME, AGENT_ID, certFingerprint),
            METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()), PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.RECEIPT_INVALID);
    }

    @Test
    void missingHeadersRejected() {
        PopException ex = catchThrowableOfType(() -> verifier().verifyCaller(
            proofJws, Map.of(), METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.MISSING_HEADERS);
    }

    @Test
    void duplicateScittHeaderRejected() {
        Map<String, List<String>> headers = Map.of(
            ScittHeaders.SCITT_RECEIPT_HEADER, List.of("a", "b"));
        PopException ex = catchThrowableOfType(() -> verifier().verifyCaller(
            proofJws, headers, METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.SCITT_HEADER_INVALID);
    }

    @Test
    void invalidBase64HeaderRejected() {
        Map<String, List<String>> headers = Map.of(
            ScittHeaders.SCITT_RECEIPT_HEADER, List.of("!!!not-base64!!!"));
        PopException ex = catchThrowableOfType(() -> verifier().verifyCaller(
            proofJws, headers, METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.SCITT_HEADER_INVALID);
    }

    @Test
    void unparseableReceiptRejected() {
        Map<String, List<String>> headers = Map.of(
            ScittHeaders.SCITT_RECEIPT_HEADER,
            List.of(Base64.getEncoder().encodeToString("garbage".getBytes(StandardCharsets.UTF_8))));
        PopException ex = catchThrowableOfType(() -> verifier().verifyCaller(
            proofJws, headers, METHOD, URL, Map.of(), new CountingReplay(false), CallerOptions.none()),
            PopException.class);

        assertThat(ex.category()).isEqualTo(ErrorType.RECEIPT_INVALID);
    }

    private static final Duration DEFAULT_SKEW = Duration.ofSeconds(120);

    private static CallerVerifier verifier() {
        return new CallerVerifier(new FakeScitt(ScittExpectation.verified(
            List.of(), List.of(), ANS_NAME, Map.of(), null)), DEFAULT_SKEW);
    }

    private static StatusToken token(String ansName, String agentId, String identityFingerprint) {
        Instant now = Instant.now();
        return new StatusToken(agentId, StatusToken.Status.ACTIVE, now, now.plusSeconds(3600), ansName,
            List.of(new CertificateInfo(identityFingerprint, CertType.X509_EV_CLIENT)),
            List.of(), Map.of(), null);
    }

    private static ScittReceipt receipt(String agentId, String ansName) {
        String json = "{\"agentId\":\"" + agentId + "\",\"ansName\":\"" + ansName + "\"}";
        return new ScittReceipt(null, null, null, json.getBytes(StandardCharsets.UTF_8), null);
    }

    private static KeyPair ec() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        generator.initialize(new ECGenParameterSpec("secp256r1"));
        return generator.generateKeyPair();
    }

    private static X509Certificate selfSigned(KeyPair keyPair, String ansUri) throws Exception {
        X500Name dn = new X500Name("CN=test");
        Instant now = Instant.now();
        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
            dn, BigInteger.ONE, Date.from(now.minusSeconds(60)), Date.from(now.plusSeconds(3600)),
            dn, keyPair.getPublic());
        if (ansUri != null) {
            GeneralNames san = new GeneralNames(new GeneralName(GeneralName.uniformResourceIdentifier, ansUri));
            builder.addExtension(Extension.subjectAlternativeName, false, san);
        }
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA").build(keyPair.getPrivate());
        return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME)
            .getCertificate(builder.build(signer));
    }

    private static final class FakeScitt implements ScittVerifier {
        private final ScittExpectation expectation;

        private FakeScitt(ScittExpectation expectation) {
            this.expectation = expectation;
        }

        @Override
        public ScittExpectation verify(ScittReceipt receipt, StatusToken token, Map<String, PublicKey> rootKeys) {
            return expectation;
        }

        @Override
        public ScittVerificationResult postVerify(String hostname, X509Certificate serverCert,
                                                  ScittExpectation expectation) {
            return null;
        }
    }

    private static final class CountingReplay implements ReplayCache {
        private final boolean seen;
        private int calls;

        private CountingReplay(boolean seen) {
            this.seen = seen;
        }

        @Override
        public boolean checkAndStore(String key, Duration ttl) {
            calls++;
            return seen;
        }
    }
}