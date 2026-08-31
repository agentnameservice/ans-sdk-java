package com.godaddy.ans.sdk.spring;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Tests for {@link PopAuthentication}.
 */
class PopAuthenticationTest {

    private static CallerIdentity identity() {
        return new CallerIdentity("ans://agent.example.com", "agent-1", new byte[] {1, 2, 3}, "jkt");
    }

    @Test
    void returnsIdentityWhenAttributePresent() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        CallerIdentity identity = identity();
        request.setAttribute(PopAuthentication.CALLER_ATTRIBUTE, identity);

        assertThat(PopAuthentication.fromRequest(request)).contains(identity);
    }

    @Test
    void returnsEmptyWhenAttributeMissing() {
        assertThat(PopAuthentication.fromRequest(new MockHttpServletRequest())).isEmpty();
    }

    @Test
    void returnsEmptyWhenAttributeWrongType() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute(PopAuthentication.CALLER_ATTRIBUTE, "not-an-identity");

        assertThat(PopAuthentication.fromRequest(request)).isEmpty();
    }

    @Test
    void rejectsNullRequest() {
        assertThatNullPointerException()
            .isThrownBy(() -> PopAuthentication.fromRequest(null))
            .withMessage("request");
    }
}