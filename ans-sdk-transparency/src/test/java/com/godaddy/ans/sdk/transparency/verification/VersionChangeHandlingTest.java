package com.godaddy.ans.sdk.transparency.verification;

import com.godaddy.ans.sdk.crypto.CertificateUtils;
import com.godaddy.ans.sdk.transparency.TransparencyClient;
import com.godaddy.ans.sdk.transparency.dns.RaBadgeLookupService;
import com.godaddy.ans.sdk.transparency.dns.RaBadgeRecord;
import com.godaddy.ans.sdk.transparency.model.AgentV1;
import com.godaddy.ans.sdk.transparency.model.AttestationsV1;
import com.godaddy.ans.sdk.transparency.model.CertificateInfo;
import com.godaddy.ans.sdk.transparency.model.CertType;
import com.godaddy.ans.sdk.transparency.model.EventV1;
import com.godaddy.ans.sdk.transparency.model.ProducerV1;
import com.godaddy.ans.sdk.transparency.model.TransparencyLog;
import com.godaddy.ans.sdk.transparency.model.TransparencyLogV1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;

/**
 * Unit tests for version change handling scenarios.
 * Tests the badge selection logic when multiple versions exist.
 */
class VersionChangeHandlingTest {

    private static final String TEST_HOSTNAME = "agent.example.com";
    private static final String TEST_AGENT_ID_V1 = "6bf2b7a9-1383-4e33-a945-845f34af7526";
    private static final String TEST_AGENT_ID_V2 = "7cf3c8b0-2494-5f44-b056-956f45bf8637";
    private static final String TEST_ANS_NAME_V1 = "ans://v1.0.0.agent.example.com";
    private static final String TEST_ANS_NAME_V2 = "ans://v1.0.1.agent.example.com";
    private static final String TEST_FINGERPRINT_V1 =
            "SHA256:a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2";
    private static final String TEST_FINGERPRINT_V2 =
            "SHA256:b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3";

    // v1.0.9 constants for cross-form matching tests
    private static final String TEST_AGENT_ID_V9 = "9ef5e0d2-4616-7a66-d278-178a67cf0859";
    private static final String TEST_ANS_NAME_V9 = "ans://v1.0.9.agent.example.com";
    private static final String TEST_ANS_NAME_V9_UPPER = "ans://V1.0.9.agent.example.com";
    private static final String TEST_FINGERPRINT_V9 =
            "SHA256:c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4";

    // versionless constants for two-pass fallback tests
    private static final String TEST_AGENT_ID_VERSIONLESS = "0af6f1e3-5727-8b77-e389-289b78df1970";
    private static final String TEST_FINGERPRINT_VERSIONLESS =
            "SHA256:d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5f6a1b2c3d4e5";

    @Mock
    private TransparencyClient transparencyClient;

    @Mock
    private RaBadgeLookupService raBadgeLookupService;

    @Mock
    private X509Certificate mockCertificate;

    private BadgeVerificationService verificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        verificationService = BadgeVerificationService.builder()
            .transparencyClient(transparencyClient)
            .raBadgeLookupService(raBadgeLookupService)
            .build();
    }

    // ==================== Two ACTIVE versions ====================

    @Test
    @DisplayName("Should select correct badge when two ACTIVE versions exist")
    void shouldSelectCorrectBadgeWithTwoActiveVersions() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents v1.0.0 identity cert
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V1, TEST_FINGERPRINT_V1))
                .thenReturn(true);

            // Two badges - v1.0.0 and v1.0.1 both ACTIVE
            RaBadgeRecord badgeV1 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V1);
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV1, badgeV2));

            // Both registrations are ACTIVE
            TransparencyLog registrationV1 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V1, TEST_ANS_NAME_V1);
            TransparencyLog registrationV2 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V2, TEST_ANS_NAME_V2);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(registrationV1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V2)).thenReturn(registrationV2);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - should select v1.0.0 badge and pass
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            assertThat(result.getExpectedAnsName()).isEqualTo(TEST_ANS_NAME_V1);
            assertThat(result.getExpectedIdentityCertFingerprint()).isEqualTo(TEST_FINGERPRINT_V1);

            // Should only fetch the v1.0.0 registration (version filtering optimization)
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_V1);
        }
    }

    // ==================== DV-CLIENT cert type ====================

    @Test
    @DisplayName("Should verify client with X509-DV-CLIENT identity cert type")
    void shouldVerifyClientWithDvClientCertType() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            certUtils.when(
                    () -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V1, TEST_FINGERPRINT_V1))
                .thenReturn(true);

            RaBadgeRecord badge = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/"
                    + TEST_AGENT_ID_V1);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badge));

            TransparencyLog registration = createMockRegistrationWithDvClientCert(
                "ACTIVE", TEST_FINGERPRINT_V1, TEST_ANS_NAME_V1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(registration);

            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
        }
    }

    // ==================== Old version DEPRECATED ====================

    @Test
    @DisplayName("Should pass with warning when old version is DEPRECATED")
    void shouldPassWithWarningWhenOldVersionDeprecated() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents v1.0.0 identity cert (old version)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V1, TEST_FINGERPRINT_V1))
                .thenReturn(true);

            // Two badges - v1.0.0 DEPRECATED, v1.0.1 ACTIVE
            RaBadgeRecord badgeV1 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/"
                        + TEST_AGENT_ID_V1);
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/"
                        + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV1, badgeV2));

            // v1.0.0 is DEPRECATED, v1.0.1 is ACTIVE
            TransparencyLog registrationV1 = createMockRegistration("DEPRECATED", TEST_FINGERPRINT_V1,
                    TEST_ANS_NAME_V1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(registrationV1);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - should pass with DEPRECATED_OK status
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.DEPRECATED_OK);
            assertThat(result.getWarningMessage()).contains("deprecated");
        }
    }

    // ==================== No matching version in DNS ====================

    @Test
    @DisplayName("Should reject when no matching version in DNS (bare semver badge)")
    void shouldRejectWhenNoMatchingVersionInDns() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents v1.0.0 cert, but DNS only has v1.0.1 (bare form)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1)); // v1.0.0
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);

            // Only v1.0.1 badge exists in DNS (bare semver form)
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV2));

            // Stub TL: previously reachable via the all-badges fallback and returned FINGERPRINT_MISMATCH
            TransparencyLog registrationV2 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V2, TEST_ANS_NAME_V2);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V2)).thenReturn(registrationV2);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - no badge published for v1.0.0 → NOT_ANS_AGENT, TL never called
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.NOT_ANS_AGENT);
            assertThat(result.getWarningMessage()).contains("1.0.0");
            verify(transparencyClient, never()).getAgentTransparencyLog(any());
        }
    }

    @Test
    @DisplayName("Should reject when no matching version in DNS (v-prefixed badge)")
    void shouldRejectWhenNoMatchingVersionInDnsVPrefixedBadge() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents v1.0.0 cert, but DNS only has v1.0.1 (production v-prefixed form)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1)); // v1.0.0
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            // No fingerprintMatches stub needed: code returns NOT_ANS_AGENT before any fingerprint check.

            // Only v1.0.1 badge exists in DNS (production v-prefixed form: version=v1.0.1)
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV2));

            // Stub TL: previously reachable via the all-badges fallback and returned FINGERPRINT_MISMATCH
            TransparencyLog registrationV2 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V2, TEST_ANS_NAME_V2);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V2)).thenReturn(registrationV2);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - no badge published for v1.0.0 → NOT_ANS_AGENT, TL never called
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.NOT_ANS_AGENT);
            assertThat(result.getWarningMessage()).contains("1.0.0");
            verify(transparencyClient, never()).getAgentTransparencyLog(any());
        }
    }

    // ==================== Server verification without version ====================

    @Test
    @DisplayName("Should prefer ACTIVE badge when server cert has no version")
    void shouldPreferActiveBadgeWhenServerCertHasNoVersion() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents cert without version (no URI SAN)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.empty()); // No version in cert
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V2);
            // Fingerprint matches v1.0.1's fingerprint
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V2, TEST_FINGERPRINT_V2))
                .thenReturn(true);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V2, TEST_FINGERPRINT_V1))
                .thenReturn(false);

            // Two badges - v1.0.0 DEPRECATED, v1.0.1 ACTIVE
            RaBadgeRecord badgeV1 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V1);
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV1, badgeV2));

            // v1.0.0 is DEPRECATED, v1.0.1 is ACTIVE
            TransparencyLog registrationV1 = createMockRegistrationNoAnsName("DEPRECATED", TEST_FINGERPRINT_V1);
            TransparencyLog registrationV2 = createMockRegistrationNoAnsName("ACTIVE", TEST_FINGERPRINT_V2);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(registrationV1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V2)).thenReturn(registrationV2);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - should prefer ACTIVE badge (v1.0.1)
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
        }
    }

    // ==================== Partial fetch failure ====================

    @Test
    @DisplayName("Should pass if successful badge matches despite partial failure")
    void shouldPassIfSuccessfulBadgeMatchesDespitePartialFailure() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents v1.0.0 identity cert
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V1, TEST_FINGERPRINT_V1))
                .thenReturn(true);

            // Two badges - v1.0.0 and v1.0.1
            RaBadgeRecord badgeV1 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V1);
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV2, badgeV1));

            // v1.0.1 fetch fails (5xx), but v1.0.0 succeeds
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V2))
                .thenThrow(new RuntimeException("Service unavailable"));
            TransparencyLog registrationV1 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V1, TEST_ANS_NAME_V1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(registrationV1);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - should pass because the matching badge (v1.0.0) succeeded
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            assertThat(result.getExpectedAnsName()).isEqualTo(TEST_ANS_NAME_V1);
        }
    }

    @Test
    @DisplayName("Should apply failure policy when all badge fetches fail")
    void shouldApplyFailurePolicyWhenAllBadgeFetchesFail() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - client presents cert
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);

            // Two badges exist
            RaBadgeRecord badgeV1 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V1);
            RaBadgeRecord badgeV2 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV1, badgeV2));

            // Both fetch attempts fail
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1))
                .thenThrow(new RuntimeException("Service unavailable"));
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V2))
                .thenThrow(new RuntimeException("Service unavailable"));

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - should fail with lookup failure
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.LOOKUP_FAILED);
        }
    }

    // ==================== Cross-form version matching ====================

    @Test
    @DisplayName("Should match v-prefixed badge with versioned SAN (ANS-6 §6.2 leniency)")
    void shouldMatchVPrefixedBadgeWithVersionedSan() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - cert SAN ans://v1.0.9.agent.example.com → certVersion "1.0.9"
            //         badge version=v1.0.9 (production form) → agentVersion "v1.0.9"
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V9));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V9);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V9, TEST_FINGERPRINT_V9))
                .thenReturn(true);

            // Badge uses production v-prefixed form: version=v1.0.9
            RaBadgeRecord badgeV9 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v1.0.9; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V9);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV9));

            TransparencyLog registrationV9 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V9, TEST_ANS_NAME_V9);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V9)).thenReturn(registrationV9);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - v-prefixed badge selected; verification reaches fingerprint check → VERIFIED
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            assertThat(result.getExpectedAnsName()).isEqualTo(TEST_ANS_NAME_V9);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_V9);
        }
    }

    @Test
    @DisplayName("Should match bare-semver badge with versioned SAN (regression guard)")
    void shouldMatchBareSemverBadgeWithVersionedSan() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - cert SAN ans://v1.0.9.agent.example.com → certVersion "1.0.9"
            //         badge version=1.0.9 (bare form) → agentVersion "1.0.9"
            //         (regression guard: this form matched before this change)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V9));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V9);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V9, TEST_FINGERPRINT_V9))
                .thenReturn(true);

            // Badge uses bare semver form: version=1.0.9
            RaBadgeRecord badgeV9 = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.9; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V9);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV9));

            TransparencyLog registrationV9 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V9, TEST_ANS_NAME_V9);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V9)).thenReturn(registrationV9);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - bare-semver badge selected and verified
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            assertThat(result.getExpectedAnsName()).isEqualTo(TEST_ANS_NAME_V9);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_V9);
        }
    }

    // ==================== Two-pass badge selection ====================

    @Test
    @DisplayName("Should prefer exact-version badge over versionless badge (pass 1 wins)")
    void shouldPreferExactVersionBadgeOverVersionlessBadge() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - cert v1.0.0; two badges: exact-version + versionless
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V1, TEST_FINGERPRINT_V1))
                .thenReturn(true);

            RaBadgeRecord exactBadge = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V1);
            RaBadgeRecord versionlessBadge = RaBadgeRecord.parse(
                "v=ra-badge1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_VERSIONLESS);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME))
                .thenReturn(List.of(exactBadge, versionlessBadge));

            TransparencyLog exactRegistration = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V1, TEST_ANS_NAME_V1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(exactRegistration);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - pass 1 selected exact-version badge only; versionless badge's TL never fetched
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_V1);
            verify(transparencyClient, never()).getAgentTransparencyLog(TEST_AGENT_ID_VERSIONLESS);
        }
    }

    @Test
    @DisplayName("Should fall back to versionless badge when only versionless badge exists (pass 2)")
    void shouldFallbackToVersionlessBadgeWhenOnlyVersionlessBadgeExists() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // Given - cert v1.0.0; only a versionless badge exists → pass 2 fallback
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_VERSIONLESS);
            String fp = TEST_FINGERPRINT_VERSIONLESS;
            certUtils.when(() -> CertificateUtils.fingerprintMatches(fp, fp))
                .thenReturn(true);

            // Only a versionless badge (no version= field)
            RaBadgeRecord versionlessBadge = RaBadgeRecord.parse(
                "v=ra-badge1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_VERSIONLESS);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(versionlessBadge));

            TransparencyLog versionlessRegistration =
                createMockRegistrationNoAnsName("ACTIVE", TEST_FINGERPRINT_VERSIONLESS);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_VERSIONLESS))
                .thenReturn(versionlessRegistration);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - pass 2 used the versionless badge; verification succeeded
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_VERSIONLESS);
        }
    }

    // ==================== Uppercase V normalization ====================

    @Test
    @DisplayName("Should normalize uppercase-V badge version against lowercase-SAN version (badge V1.0.9 + SAN v1.0.9)")
    void shouldMatchUppercaseVBadgeVersion() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // badge version=V1.0.9 (uppercase V); SAN ans://v1.0.9.agent.example.com (lowercase)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V9)); // lowercase SAN
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V9);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V9, TEST_FINGERPRINT_V9))
                .thenReturn(true);

            // Badge uses uppercase-V form: version=V1.0.9
            RaBadgeRecord badgeV9 = RaBadgeRecord.parse(
                "v=ra-badge1; version=V1.0.9; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V9);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV9));

            TransparencyLog registrationV9 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V9, TEST_ANS_NAME_V9);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V9)).thenReturn(registrationV9);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - normalizeVersion strips uppercase V on both sides; badge is selected → VERIFIED
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_V9);
        }
    }

    @Test
    @DisplayName("Uppercase-V SAN yields ANS_NAME_MISMATCH — case-sensitive ansName comparison is a known limitation")
    void shouldReportAnsNameMismatchForUppercaseSan() {
        // Note: ANS_VERSION_PATTERN is CASE_INSENSITIVE so "ans://V1.0.9.host" → certVersion "1.0.9"
        // and filterBadgesByVersion selects the badge correctly. However,
        // findMatchingClientRegistration compares certAnsName.equals(expectedAnsName) case-sensitively:
        // "ans://V1.0.9.agent.example.com" ≠ "ans://v1.0.9.agent.example.com" → ANS_NAME_MISMATCH.
        // Spec examples and RA issuance use lowercase ans:// SANs and ANS-6 §6.2 requires equality;
        // this asymmetry is pre-existing and not changed here.
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            // SAN uses uppercase V
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V9_UPPER));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V9);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V9, TEST_FINGERPRINT_V9))
                .thenReturn(true);

            // Badge uses lowercase v-prefixed form (production form)
            RaBadgeRecord badgeV9 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v1.0.9; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V9);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badgeV9));

            // TL stores the ansName as issued (lowercase)
            TransparencyLog registrationV9 = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V9, TEST_ANS_NAME_V9);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V9)).thenReturn(registrationV9);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - badge selected (version filter is case-insensitive) but certAnsName case mismatch
            // with stored ansName → ANS_NAME_MISMATCH. Assert the actual observable outcome.
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.ANS_NAME_MISMATCH);
        }
    }

    // ==================== Mixed badge sets ====================

    @Test
    @DisplayName("Should select exact-version badge from set [other-version, versionless, exact] — pass 1 preferred")
    void shouldSelectExactFromThreeBadgeMixedSet() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // cert SAN v1.0.0; three badges: version=1.0.1 (other), no-version, version=1.0.0 (exact)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);
            certUtils.when(() -> CertificateUtils.fingerprintMatches(TEST_FINGERPRINT_V1, TEST_FINGERPRINT_V1))
                .thenReturn(true);

            RaBadgeRecord otherBadge = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            RaBadgeRecord versionlessBadge = RaBadgeRecord.parse(
                "v=ra-badge1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_VERSIONLESS);
            RaBadgeRecord exactBadge = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V1);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME))
                .thenReturn(List.of(otherBadge, versionlessBadge, exactBadge));

            TransparencyLog exactRegistration = createMockRegistration("ACTIVE", TEST_FINGERPRINT_V1, TEST_ANS_NAME_V1);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_V1)).thenReturn(exactRegistration);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - pass 1 selected only the exact-version badge; other and versionless TLs never fetched
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_V1);
            verify(transparencyClient, never()).getAgentTransparencyLog(TEST_AGENT_ID_V2);
            verify(transparencyClient, never()).getAgentTransparencyLog(TEST_AGENT_ID_VERSIONLESS);
        }
    }

    @Test
    @DisplayName("Should fall back to versionless badge from set [other-version, versionless]")
    void shouldFallbackToVersionlessWhenOtherVersionAndVersionlessExist() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // cert SAN v1.0.0; two badges: version=1.0.1 (other), no-version (versionless fallback)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_VERSIONLESS);
            String fp = TEST_FINGERPRINT_VERSIONLESS;
            certUtils.when(() -> CertificateUtils.fingerprintMatches(fp, fp))
                .thenReturn(true);

            RaBadgeRecord otherBadge = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            RaBadgeRecord versionlessBadge = RaBadgeRecord.parse(
                "v=ra-badge1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_VERSIONLESS);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME))
                .thenReturn(List.of(otherBadge, versionlessBadge));

            TransparencyLog versionlessRegistration =
                createMockRegistrationNoAnsName("ACTIVE", TEST_FINGERPRINT_VERSIONLESS);
            when(transparencyClient.getAgentTransparencyLog(TEST_AGENT_ID_VERSIONLESS))
                .thenReturn(versionlessRegistration);

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - pass 1 empty (no exact match), pass 2 selects versionless badge
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.VERIFIED);
            verify(transparencyClient).getAgentTransparencyLog(TEST_AGENT_ID_VERSIONLESS);
            verify(transparencyClient, never()).getAgentTransparencyLog(TEST_AGENT_ID_V2);
        }
    }

    @Test
    @DisplayName("Should return NOT_ANS_AGENT when only other-version badges exist — neither pass matches")
    void shouldReturnNotAnsAgentWhenOnlyOtherVersionBadgesExist() {
        String otherAgentIdA = "aaaaaaaa-1111-2222-3333-444444444444";
        String otherAgentIdB = "bbbbbbbb-5555-6666-7777-888888888888";
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // cert SAN v1.0.0; two badges both at other versions — neither pass yields a match
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1));
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);

            RaBadgeRecord otherA = RaBadgeRecord.parse(
                "v=ra-badge1; version=1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + otherAgentIdA);
            RaBadgeRecord otherB = RaBadgeRecord.parse(
                "v=ra-badge1; version=2.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + otherAgentIdB);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(otherA, otherB));

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - both passes empty → NOT_ANS_AGENT; transparency client never called
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.NOT_ANS_AGENT);
            verify(transparencyClient, never()).getAgentTransparencyLog(any());
        }
    }

    // ==================== Warning message exact text ====================

    @Test
    @DisplayName("Warning message names the presented version and the count of other-version records exactly")
    void shouldIncludeExactWarningMessageForVersionMismatch() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // cert SAN v1.0.0; badges at v1.0.1 and v2.0.0 (2 other-version valid badges)
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1)); // v1.0.0
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);

            RaBadgeRecord badge101 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            RaBadgeRecord badge200 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v2.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V9);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME)).thenReturn(List.of(badge101, badge200));

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - exact warning message format: version is bare (1.0.0), count is total valid badges
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.NOT_ANS_AGENT);
            assertThat(result.getWarningMessage()).isEqualTo(
                "No badge record published for version 1.0.0; 2 badge record(s) found for other versions");
        }
    }

    @Test
    @DisplayName("Warning message counts only badges whose URL passed validation")
    void shouldCountOnlyUrlValidBadgesInVersionMismatchWarning() {
        try (MockedStatic<CertificateUtils> certUtils = mockStatic(CertificateUtils.class)) {
            // cert SAN v1.0.0; two other-version badges with trusted URLs plus one with an untrusted URL
            certUtils.when(() -> CertificateUtils.extractFqdn(mockCertificate))
                .thenReturn(Optional.of(TEST_HOSTNAME));
            certUtils.when(() -> CertificateUtils.getCommonName(mockCertificate))
                .thenReturn(TEST_HOSTNAME);
            certUtils.when(() -> CertificateUtils.extractAnsName(mockCertificate))
                .thenReturn(Optional.of(TEST_ANS_NAME_V1)); // v1.0.0
            certUtils.when(() -> CertificateUtils.computeSha256Fingerprint(mockCertificate))
                .thenReturn(TEST_FINGERPRINT_V1);

            RaBadgeRecord badge101 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v1.0.1; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V2);
            RaBadgeRecord badge200 = RaBadgeRecord.parse(
                "v=ra-badge1; version=v2.0.0; url=https://transparency.ans.godaddy.com/v1/agents/" + TEST_AGENT_ID_V9);
            RaBadgeRecord badgeUntrusted = RaBadgeRecord.parse(
                "v=ra-badge1; version=v3.0.0; url=https://untrusted.example.com/v1/agents/"
                    + TEST_AGENT_ID_VERSIONLESS);
            when(raBadgeLookupService.lookupBadges(TEST_HOSTNAME))
                .thenReturn(List.of(badge101, badge200, badgeUntrusted));

            // When
            ClientVerificationResult result = verificationService.verifyClient(mockCertificate);

            // Then - the untrusted-URL badge is excluded before the version filter, so the count is 2 not 3
            assertThat(result.getStatus()).isEqualTo(VerificationStatus.NOT_ANS_AGENT);
            assertThat(result.getWarningMessage()).isEqualTo(
                "No badge record published for version 1.0.0; 2 badge record(s) found for other versions");
            verify(transparencyClient, never()).getAgentTransparencyLog(any());
        }
    }

    // ==================== Helper Methods ====================

    private TransparencyLog createMockRegistration(String status, String fingerprint, String ansName) {
        CertificateInfo serverCert = new CertificateInfo();
        serverCert.setFingerprint(fingerprint);
        serverCert.setType(CertType.X509_DV_SERVER);

        CertificateInfo identityCert = new CertificateInfo();
        identityCert.setFingerprint(fingerprint);
        identityCert.setType(CertType.X509_OV_CLIENT);

        AttestationsV1 attestations = new AttestationsV1();
        attestations.setServerCert(serverCert);
        attestations.setIdentityCert(identityCert);

        AgentV1 agent = new AgentV1();
        agent.setHost(TEST_HOSTNAME);
        agent.setName("Test Agent");
        agent.setVersion(extractAgentVersion(ansName));

        EventV1 event = new EventV1();
        event.setAnsName(ansName);
        event.setAgent(agent);
        event.setAttestations(attestations);

        ProducerV1 producer = new ProducerV1();
        producer.setEvent(event);

        TransparencyLogV1 v1Payload = new TransparencyLogV1();
        v1Payload.setLogId("log-123");
        v1Payload.setProducer(producer);

        TransparencyLog log = new TransparencyLog();
        log.setStatus(status);
        log.setSchemaVersion("V1");
        log.setParsedPayload(v1Payload);

        return log;
    }

    private static String extractAgentVersion(String ansName) {
        // Extract the version segment from an ANS name like ans://v1.0.9.agent.example.com
        java.util.regex.Matcher m = java.util.regex.Pattern
            .compile("ans://v?(\\d+\\.\\d+\\.\\d+)\\.", java.util.regex.Pattern.CASE_INSENSITIVE)
            .matcher(ansName);
        return m.find() ? "v" + m.group(1) : "v1.0.0";
    }

    private TransparencyLog createMockRegistrationWithDvClientCert(String status, String fingerprint, String ansName) {
        CertificateInfo serverCert = new CertificateInfo();
        serverCert.setFingerprint(fingerprint);
        serverCert.setType(CertType.X509_DV_SERVER);

        CertificateInfo identityCert = new CertificateInfo();
        identityCert.setFingerprint(fingerprint);
        identityCert.setType(CertType.X509_DV_CLIENT);

        AttestationsV1 attestations = new AttestationsV1();
        attestations.setServerCert(serverCert);
        attestations.setIdentityCert(identityCert);

        AgentV1 agent = new AgentV1();
        agent.setHost(TEST_HOSTNAME);
        agent.setName("Test Agent");
        agent.setVersion("v1.0.0");

        EventV1 event = new EventV1();
        event.setAnsName(ansName);
        event.setAgent(agent);
        event.setAttestations(attestations);

        ProducerV1 producer = new ProducerV1();
        producer.setEvent(event);

        TransparencyLogV1 v1Payload = new TransparencyLogV1();
        v1Payload.setLogId("log-dv-client");
        v1Payload.setProducer(producer);

        TransparencyLog log = new TransparencyLog();
        log.setStatus(status);
        log.setSchemaVersion("V1");
        log.setParsedPayload(v1Payload);

        return log;
    }

    private TransparencyLog createMockRegistrationNoAnsName(String status, String fingerprint) {
        CertificateInfo serverCert = new CertificateInfo();
        serverCert.setFingerprint(fingerprint);
        serverCert.setType(CertType.X509_DV_SERVER);

        CertificateInfo identityCert = new CertificateInfo();
        identityCert.setFingerprint(fingerprint);
        identityCert.setType(CertType.X509_OV_CLIENT);

        AttestationsV1 attestations = new AttestationsV1();
        attestations.setServerCert(serverCert);
        attestations.setIdentityCert(identityCert);

        AgentV1 agent = new AgentV1();
        agent.setHost(TEST_HOSTNAME);
        agent.setName("Test Agent");
        agent.setVersion("v1.0.0");

        EventV1 event = new EventV1();
        event.setAnsName(null); // No ANS name
        event.setAgent(agent);
        event.setAttestations(attestations);

        ProducerV1 producer = new ProducerV1();
        producer.setEvent(event);

        TransparencyLogV1 v1Payload = new TransparencyLogV1();
        v1Payload.setLogId("log-123");
        v1Payload.setProducer(producer);

        TransparencyLog log = new TransparencyLog();
        log.setStatus(status);
        log.setSchemaVersion("V1");
        log.setParsedPayload(v1Payload);

        return log;
    }
}
