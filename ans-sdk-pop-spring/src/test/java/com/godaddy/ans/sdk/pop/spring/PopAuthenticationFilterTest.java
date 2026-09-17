package com.godaddy.ans.sdk.pop.spring;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import com.godaddy.ans.sdk.pop.CallerOptions;
import com.godaddy.ans.sdk.pop.CallerPolicy;
import com.godaddy.ans.sdk.pop.CallerVerifier;
import com.godaddy.ans.sdk.pop.ErrorType;
import com.godaddy.ans.sdk.pop.PopException;
import com.godaddy.ans.sdk.pop.PopHttp;
import com.godaddy.ans.sdk.pop.ReplayCache;
import com.godaddy.ans.sdk.pop.RootKeyRefresher;
import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link PopAuthenticationFilter}.
 */
class PopAuthenticationFilterTest {

    private static final String ANS_NAME = "ans://agent.example.com";
    private static final Supplier<Map<String, PublicKey>> ROOT_KEYS = Map::of;
    private static final ReplayCache REPLAY = (key, ttl) -> false;

    private static CallerIdentity identity(String ansName) {
        return new CallerIdentity(ansName, "agent-1", new byte[] {1, 2, 3}, "jkt");
    }

    private static CallerVerifier verifierReturning(CallerIdentity identity) throws PopException {
        CallerVerifier verifier = mock(CallerVerifier.class);
        when(verifier.verifyCaller(any(), any(), any(), any(), any(), any(), any())).thenReturn(identity);
        return verifier;
    }

    private static CallerVerifier verifierRejecting() throws PopException {
        CallerVerifier verifier = mock(CallerVerifier.class);
        when(verifier.verifyCaller(any(), any(), any(), any(), any(), any(), any()))
            .thenThrow(new PopException(ErrorType.MALFORMED_PROOF, "bad proof"));
        return verifier;
    }

    // Mimics the real verifier's step 12: reads the supplied content source, and reports a read
    // failure as CONTENT_UNREADABLE with the I/O cause attached.
    private static CallerVerifier verifierReadingContent(CallerIdentity identity) throws PopException {
        CallerVerifier verifier = mock(CallerVerifier.class);
        when(verifier.verifyCaller(any(), any(), any(), any(), any(), any(), any())).thenAnswer(invocation -> {
            CallerOptions options = invocation.getArgument(6);
            try {
                options.receivedContent().read();
            } catch (IOException e) {
                throw new PopException(ErrorType.CONTENT_UNREADABLE, "request content could not be read", e);
            }
            return identity;
        });
        return verifier;
    }

    private static PopAuthenticationFilter filter(CallerVerifier verifier, Set<String> trustedHosts,
                                                  Set<String> allowedHosts) {
        return filter(verifier, ROOT_KEYS, null, trustedHosts, allowedHosts);
    }

    private static PopAuthenticationFilter filter(CallerVerifier verifier,
                                                  Supplier<Map<String, PublicKey>> rootKeys,
                                                  Function<HttpServletRequest, String> externalUrl,
                                                  Set<String> trustedHosts, Set<String> allowedHosts) {
        return filter(verifier, rootKeys, externalUrl, trustedHosts, allowedHosts,
            PopAuthenticationFilter.DEFAULT_MAX_CONTENT_BYTES);
    }

    private static PopAuthenticationFilter filter(CallerVerifier verifier,
                                                  Supplier<Map<String, PublicKey>> rootKeys,
                                                  Function<HttpServletRequest, String> externalUrl,
                                                  Set<String> trustedHosts, Set<String> allowedHosts,
                                                  long maxContentBytes) {
        CallerPolicy policy = CallerPolicy.builder()
            .trustedHosts(trustedHosts.toArray(new String[0]))
            .allowedAnsNames(allowedHosts.toArray(new String[0]))
            .build();
        return new PopAuthenticationFilter(verifier, rootKeys, REPLAY, externalUrl, policy, maxContentBytes);
    }

    private static HttpServletRequest withRequestUrl(HttpServletRequest request, String url) {
        return new HttpServletRequestWrapper(request) {
            @Override
            public StringBuffer getRequestURL() {
                return new StringBuffer(url);
            }
        };
    }

    private static MockHttpServletRequest request() {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/verify");
        request.addHeader(PopHttp.DPOP_HEADER, "proof-token");
        return request;
    }

    private static void assertRejected(MockHttpServletResponse response, MockFilterChain chain) {
        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(response.getErrorMessage()).isEqualTo("unauthorized");
        assertThat(response.getHeader("WWW-Authenticate")).isEqualTo(PopHttp.DPOP_HEADER);
        assertThat(chain.getRequest()).as("filter chain should not be invoked").isNull();
    }

    // ==================== doFilterInternal - success ====================

    @Test
    void authenticatesCallerAndPopulatesAttribute() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
        assertThat(PopAuthentication.fromRequest(request)).map(CallerIdentity::ansName).contains(ANS_NAME);
    }

    @Test
    void authenticatesWhenAnsHostIsInAllowedSet() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), Set.of(), Set.of("agent.example.com"));
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    @Test
    void passesAccessTokenFromAuthorizationHeader() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        request.addHeader("Authorization", "DPoP access-token-value");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    // ==================== doFilterInternal - request content ====================

    @Test
    void readsContentOnVerifierDemandAndReplaysItDownstream() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReadingContent(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        request.setContent("hello".getBytes(StandardCharsets.UTF_8));
        request.setCharacterEncoding("UTF-8");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        HttpServletRequest downstream = (HttpServletRequest) chain.getRequest();
        assertThat(downstream).isNotSameAs(request);
        assertThat(downstream.getInputStream().readAllBytes()).isEqualTo("hello".getBytes(StandardCharsets.UTF_8));
        assertThat(downstream.getReader().readLine()).isEqualTo("hello");
        assertThat(downstream.getContentLength()).isEqualTo(5);
        assertThat(downstream.getContentLengthLong()).isEqualTo(5L);
        assertThat(PopAuthentication.fromRequest(downstream)).map(CallerIdentity::ansName).contains(ANS_NAME);
    }

    @Test
    void replayedReaderDefaultsToLatin1WithoutARequestEncoding() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReadingContent(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        request.setContent("héllo".getBytes(StandardCharsets.ISO_8859_1));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        HttpServletRequest downstream = (HttpServletRequest) chain.getRequest();
        assertThat(downstream.getReader().readLine()).isEqualTo("héllo");
        assertThat(downstream.getInputStream().isFinished()).isFalse();
        assertThat(downstream.getInputStream().isReady()).isTrue();
    }

    @Test
    void replayedRequestWithoutContentReportsFinishedStream() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReadingContent(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        HttpServletRequest downstream = (HttpServletRequest) chain.getRequest();
        assertThat(downstream.getInputStream().isFinished()).isTrue();
        assertThat(downstream.getInputStream().read()).isEqualTo(-1);
        assertThat(downstream.getContentLength()).isZero();
        assertThatThrownBy(() -> downstream.getInputStream().setReadListener(null))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void contentIsReadFromTheContainerOnlyOnce() throws Exception {
        CallerVerifier verifier = mock(CallerVerifier.class);
        when(verifier.verifyCaller(any(), any(), any(), any(), any(), any(), any())).thenAnswer(invocation -> {
            CallerOptions options = invocation.getArgument(6);
            byte[] first = options.receivedContent().read();
            byte[] second = options.receivedContent().read();
            assertThat(second).isSameAs(first);
            return identity(ANS_NAME);
        });
        PopAuthenticationFilter filter = filter(verifier, Set.of(), Set.of());
        MockHttpServletRequest request = request();
        request.setContent("once".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest().getInputStream().readAllBytes())
            .isEqualTo("once".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void rejectsWhenVerifierFailsUnexpectedly() throws Exception {
        CallerVerifier verifier = mock(CallerVerifier.class);
        when(verifier.verifyCaller(any(), any(), any(), any(), any(), any(), any()))
            .thenThrow(new IllegalStateException("boom"));
        PopAuthenticationFilter filter = filter(verifier, Set.of(), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsContentAboveDeclaredBoundWith413() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReadingContent(identity(ANS_NAME)), ROOT_KEYS, null, Set.of(), Set.of(), 4);
        MockHttpServletRequest request = request();
        request.setContent("hello".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(413);
        assertThat(response.getErrorMessage()).isEqualTo("payload too large");
        assertThat(chain.getRequest()).isNull();
    }

    @Test
    void rejectsContentAboveBoundWithoutDeclaredLengthWith413() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReadingContent(identity(ANS_NAME)), ROOT_KEYS, null, Set.of(), Set.of(), 4);
        MockHttpServletRequest request = request();
        request.setContent("hello".getBytes(StandardCharsets.UTF_8));
        HttpServletRequest unknownLength = new HttpServletRequestWrapper(request) {
            @Override
            public long getContentLengthLong() {
                return -1;
            }
        };
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(unknownLength, response, chain);

        assertThat(response.getStatus()).isEqualTo(413);
        assertThat(chain.getRequest()).isNull();
    }

    @Test
    void acceptsContentAtTheBound() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReadingContent(identity(ANS_NAME)), ROOT_KEYS, null, Set.of(), Set.of(), 5);
        MockHttpServletRequest request = request();
        request.setContent("hello".getBytes(StandardCharsets.UTF_8));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isNotNull();
        assertThat(chain.getRequest().getInputStream().readAllBytes()).hasSize(5);
    }

    @Test
    void rejectsUnreadableContentWith401() throws Exception {
        CallerVerifier verifier = mock(CallerVerifier.class);
        when(verifier.verifyCaller(any(), any(), any(), any(), any(), any(), any()))
            .thenThrow(new PopException(ErrorType.CONTENT_UNREADABLE, "reset", new IOException("reset")));
        PopAuthenticationFilter filter = filter(verifier, Set.of(), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    // ==================== doFilterInternal - rejections ====================

    @Test
    void rejectsDuplicateSecurityHeader() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/verify");
        request.addHeader(PopHttp.DPOP_HEADER, "proof-a");
        request.addHeader(PopHttp.DPOP_HEADER, "proof-b");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsWhenAuthorityNotTrusted() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), Set.of("rp.example.com"), Set.of());
        MockHttpServletRequest request = request();
        request.addHeader("Host", "evil.example.com");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void acceptsWhenAuthorityIsTrusted() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), Set.of("rp.example.com"), Set.of());
        MockHttpServletRequest request = request();
        request.addHeader("Host", "rp.example.com");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    @Test
    void trustsServerNameWhenHostHeaderAbsent() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), Set.of("localhost"), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    @Test
    void checksAuthorityOnTheUrlHandedToTheVerifierNotTheHostHeader() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), Set.of("rp.example.com"), Set.of());
        MockHttpServletRequest request = request();
        request.addHeader("Host", "rp.example.com");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(withRequestUrl(request, "https://evil.example.com/verify"), response, chain);

        assertRejected(response, chain);
    }

    @Test
    void trustsTheRequestUrlAuthorityEvenWhenTheHostHeaderDiffers() throws Exception {
        CallerVerifier verifier = verifierReturning(identity(ANS_NAME));
        PopAuthenticationFilter filter = filter(verifier, Set.of("gateway.example.com"), Set.of());
        MockHttpServletRequest request = request();
        request.addHeader("Host", "evil.example.com");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(withRequestUrl(request, "https://gateway.example.com/verify"), response, chain);

        assertThat(chain.getRequest()).isNotNull();
        verify(verifier).verifyCaller(any(), any(), eq("POST"), eq("https://gateway.example.com/verify"),
            any(), any(), any());
    }

    @Test
    void rejectsWhenProofMissing() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/verify");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsWhenProofBlank() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/verify");
        request.addHeader(PopHttp.DPOP_HEADER, "   ");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsWhenRootKeysUnavailable() throws Exception {
        Supplier<Map<String, PublicKey>> failing = () -> {
            throw new IllegalStateException("keys down");
        };
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), failing, null, Set.of(), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsWhenVerifierThrows() throws Exception {
        PopAuthenticationFilter filter = filter(verifierRejecting(), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsWhenAnsHostNotInAllowedSet() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), Set.of(), Set.of("other.example.com"));
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void rejectsWhenAnsNameIsUnparseable() throws Exception {
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity("   ")), Set.of(), Set.of("agent.example.com"));
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    // ==================== externalUrl resolution ====================

    @Test
    void usesExternalUrlForAuthorityAndTarget() throws Exception {
        Function<HttpServletRequest, String> externalUrl =
            req -> "https://gateway.example.com" + req.getRequestURI();
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), ROOT_KEYS, externalUrl,
            Set.of("gateway.example.com"), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    @Test
    void rejectsWhenExternalUrlAuthorityIsInvalid() throws Exception {
        Function<HttpServletRequest, String> externalUrl = req -> ":::not a uri:::";
        PopAuthenticationFilter filter = filter(
            verifierReturning(identity(ANS_NAME)), ROOT_KEYS, externalUrl,
            Set.of("gateway.example.com"), Set.of());
        MockHttpServletRequest request = request();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }

    @Test
    void resolvesTargetWithQueryString() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        request.setQueryString("v=1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isSameAs(request);
    }

    // ==================== Builder ====================

    @Test
    void builderBuildsWithDefaults() {
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com:443")
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderBuildsWithPopSkew() {
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com:443", "other.example.com:80")
            .withPoPSkew(Duration.ofSeconds(30))
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderResolvesAllowedAnsNames() {
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withAllowedAnsNames("ans://a.example.com", "ans://b.example.com", "ans://c.example.com")
            .withTrustedHosts("rp.example.com:443")
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderAcceptsPathDependentExternalUrl() {
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withExternalUrl(req -> "https://gateway.example.com" + req.getRequestURI())
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderRejectsInvalidAllowedAnsName() {
        assertThatIllegalArgumentException().isThrownBy(() -> PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com:443")
            .withAllowedAnsNames("ans://")
            .build())
            .withMessageContaining("invalid allowed ans name");
    }

    @Test
    void builderIgnoresNullAndBlankTrustedHosts() {
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com", null, "  ")
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderRejectsWhenAllTrustedHostsEmpty() {
        assertThatIllegalArgumentException().isThrownBy(() -> PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("  ", ""))
            .withMessageContaining("every supplied host was empty");
    }

    @Test
    void builderUsesSuppliedVerifier() {
        CallerVerifier verifier = CallerVerifier.create("issuer.example.com", Duration.ofSeconds(30),
            Duration.ofSeconds(90));
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com:443")
            .withVerifier(verifier)
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderRejectsVerifierCombinedWithPopSkew() {
        assertThatIllegalStateException().isThrownBy(() -> PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com:443")
            .withVerifier(CallerVerifier.create("issuer.example.com"))
            .withPoPSkew(Duration.ofSeconds(30))
            .build())
            .withMessageContaining("mutually exclusive");
    }

    @Test
    void builderAcceptsRootKeyRefresherAndContentBound() {
        RootKeyRefresher refresher = artifactIssuedAt -> Optional.empty();
        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .withTrustedHosts("rp.example.com:443")
            .withRootKeyRefresher(refresher)
            .withMaxContentBytes(64)
            .build();

        assertThat(filter).isNotNull();
    }

    @Test
    void builderRejectsOutOfRangeMaxContentBytes() {
        PopAuthenticationFilter.Builder builder = PopAuthenticationFilter.builder("issuer", ROOT_KEYS, REPLAY);

        assertThatIllegalArgumentException().isThrownBy(() -> builder.withMaxContentBytes(0));
        assertThatIllegalArgumentException().isThrownBy(() -> builder.withMaxContentBytes(Integer.MAX_VALUE));
    }

    @Test
    void builderRejectsWhenNeitherExternalUrlNorTrustedHostsSet() {
        assertThatIllegalStateException().isThrownBy(() -> PopAuthenticationFilter
            .builder("issuer.example.com", ROOT_KEYS, REPLAY)
            .build())
            .withMessageContaining("Host header");
    }

    @Test
    void builderRejectsNullConstructorArguments() {
        assertThatNullPointerException()
            .isThrownBy(() -> PopAuthenticationFilter.builder(null, ROOT_KEYS, REPLAY));
        assertThatNullPointerException()
            .isThrownBy(() -> PopAuthenticationFilter.builder("issuer", null, REPLAY));
        assertThatNullPointerException()
            .isThrownBy(() -> PopAuthenticationFilter.builder("issuer", ROOT_KEYS, null));
    }

    @Test
    void builderRejectsNullSetters() {
        PopAuthenticationFilter.Builder builder =
            PopAuthenticationFilter.builder("issuer", ROOT_KEYS, REPLAY);

        assertThatNullPointerException().isThrownBy(() -> builder.withExternalUrl(null));
        assertThatNullPointerException().isThrownBy(() -> builder.withAllowedAnsNames((String) null));
        assertThatNullPointerException().isThrownBy(() -> builder.withPoPSkew(null));
        assertThatNullPointerException().isThrownBy(() -> builder.withVerifier(null));
        assertThatNullPointerException().isThrownBy(() -> builder.withRootKeyRefresher(null));
    }

    // Sanity: the SCITT header names the filter guards against duplicates for are what we expect.
    @Test
    void rejectsDuplicateScittReceiptHeader() throws Exception {
        PopAuthenticationFilter filter = filter(verifierReturning(identity(ANS_NAME)), Set.of(), Set.of());
        MockHttpServletRequest request = request();
        request.addHeader(ScittHeaders.SCITT_RECEIPT_HEADER, "a");
        request.addHeader(ScittHeaders.SCITT_RECEIPT_HEADER, "b");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertRejected(response, chain);
    }
}