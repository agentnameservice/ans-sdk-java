package com.godaddy.ans.examples.a2anomtls;

import com.godaddy.ans.sdk.pop.CaffeineReplayCache;
import com.godaddy.ans.sdk.pop.ReplayCache;
import com.godaddy.ans.sdk.spring.PopAuthenticationFilter;
import com.godaddy.ans.sdk.transparency.TransparencyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.PublicKey;
import java.util.Map;
import java.util.function.Supplier;

@Configuration
public class PopSecurityConfig {

    @Bean
    public TransparencyClient transparencyClient(@Value("${pop.tl-url:}") String tlUrl) {
        if (tlUrl != null && !tlUrl.isBlank()) {
            return TransparencyClient.builder().baseUrl(tlUrl).build();
        }
        return TransparencyClient.createOte();
    }

    @Bean
    public ReplayCache replayCache() {
        return CaffeineReplayCache.create(100_000);
    }

    @Bean
    public FilterRegistrationBean<PopAuthenticationFilter> popAuthenticationFilter(
            TransparencyClient transparencyClient,
            ReplayCache replayCache,
            @Value("${pop.expected-issuer}") String expectedIssuer,
            @Value("${pop.trusted-host}") String trustedHost) {

        Supplier<Map<String, PublicKey>> rootKeys = () -> transparencyClient.getRootKeysAsync().join();

        PopAuthenticationFilter filter = PopAuthenticationFilter
            .builder(expectedIssuer, rootKeys, replayCache)
            .withTrustedHosts(trustedHost)
            .build();

        FilterRegistrationBean<PopAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.addUrlPatterns("/a2a/*");
        return registration;
    }
}