package com.godaddy.ans.sdk.pop;

import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class CallerPolicyTest {

    private static CallerIdentity identity(String ansName) {
        return new CallerIdentity(ansName, "agent-1", new byte[] {1, 2, 3}, "jkt");
    }

    // ==================== normalizeAuthority ====================

    @Test
    void normalizeLowercasesAndDropsDefaultPorts() {
        assertThat(CallerPolicy.normalizeAuthority("  RP.Example.com  ")).isEqualTo("rp.example.com");
        assertThat(CallerPolicy.normalizeAuthority("rp.example.com:443")).isEqualTo("rp.example.com");
        assertThat(CallerPolicy.normalizeAuthority("rp.example.com:80")).isEqualTo("rp.example.com");
        assertThat(CallerPolicy.normalizeAuthority("rp.example.com:8443")).isEqualTo("rp.example.com:8443");
    }

    // ==================== authority trust ====================

    @Test
    void trustsAnyAuthorityWhenNoTrustedHostsConfigured() {
        CallerPolicy policy = CallerPolicy.builder().build();

        assertThat(policy.trustsAnyAuthority()).isTrue();
        assertThat(policy.authorityTrusted("anything.example.com")).isTrue();
        assertThat(policy.authorityTrusted(null)).isTrue();
    }

    @Test
    void authorityTrustedMatchesNormalizedTrustedHost() {
        CallerPolicy policy = CallerPolicy.builder().trustedHosts("rp.example.com").build();

        assertThat(policy.trustsAnyAuthority()).isFalse();
        assertThat(policy.authorityTrusted("RP.example.com:443")).isTrue();
        assertThat(policy.authorityTrusted("evil.example.com")).isFalse();
        assertThat(policy.authorityTrusted(null)).isFalse();
    }

    // ==================== caller allowed ====================

    @Test
    void allowsAnyCallerWhenNoAllowedNamesConfigured() {
        CallerPolicy policy = CallerPolicy.builder().build();

        assertThat(policy.callerAllowed(identity("ans://agent.example.com"))).isTrue();
    }

    @Test
    void callerAllowedMatchesResolvedAnsHost() {
        CallerPolicy policy = CallerPolicy.builder().allowedAnsNames("ans://agent.example.com").build();

        assertThat(policy.callerAllowed(identity("ans://agent.example.com"))).isTrue();
        assertThat(policy.callerAllowed(identity("ans://other.example.com"))).isFalse();
    }

    @Test
    void callerAllowedRejectsUnparseableAnsName() {
        CallerPolicy policy = CallerPolicy.builder().allowedAnsNames("ans://agent.example.com").build();

        assertThat(policy.callerAllowed(identity("   "))).isFalse();
    }

    // ==================== duplicate security header ====================

    @Test
    void duplicateSecurityHeaderReturnsEmptyWhenAllSingleValued() {
        CallerPolicy policy = CallerPolicy.builder().build();
        Map<String, List<String>> headers = Map.of(
            PopHttp.DPOP_HEADER, List.of("proof"),
            "content-type", List.of("a", "b"));

        assertThat(policy.duplicateSecurityHeader(headers)).isEmpty();
    }

    @Test
    void duplicateSecurityHeaderDetectsDuplicateDpop() {
        CallerPolicy policy = CallerPolicy.builder().build();
        Map<String, List<String>> headers = Map.of(PopHttp.DPOP_HEADER, List.of("a", "b"));

        assertThat(policy.duplicateSecurityHeader(headers)).contains(PopHttp.DPOP_HEADER);
    }

    @Test
    void duplicateSecurityHeaderMatchesNameCaseInsensitively() {
        CallerPolicy policy = CallerPolicy.builder().build();
        Map<String, List<String>> headers = Map.of(
            ScittHeaders.SCITT_RECEIPT_HEADER.toUpperCase(Locale.ROOT), List.of("a", "b"));

        assertThat(policy.duplicateSecurityHeader(headers)).contains(ScittHeaders.SCITT_RECEIPT_HEADER);
    }

    // ==================== builder validation ====================

    @Test
    void trustedHostsSkipsNullAndBlank() {
        CallerPolicy policy = CallerPolicy.builder().trustedHosts("rp.example.com", null, "  ").build();

        assertThat(policy.authorityTrusted("rp.example.com")).isTrue();
    }

    @Test
    void trustedHostsRejectsWhenEverySuppliedHostEmpty() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> CallerPolicy.builder().trustedHosts("  ", ""))
            .withMessageContaining("every supplied host was empty");
    }

    @Test
    void allowedAnsNamesRejectsNull() {
        assertThatNullPointerException()
            .isThrownBy(() -> CallerPolicy.builder().allowedAnsNames((String) null));
    }

    @Test
    void buildRejectsInvalidAllowedAnsName() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> CallerPolicy.builder().allowedAnsNames("ans://").build())
            .withMessageContaining("invalid allowed ans name");
    }
}
