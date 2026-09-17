package com.godaddy.ans.sdk.pop;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class VerifyOptionsTest {

    @Test
    void noneHasNoBindings() {
        VerifyOptions options = VerifyOptions.none();

        assertThat(options.accessToken()).isNull();
        assertThat(options.receivedContent()).isNull();
    }

    @Test
    void withAccessTokenSetsTokenOnly() {
        VerifyOptions options = VerifyOptions.withAccessToken("token");

        assertThat(options.accessToken()).isEqualTo("token");
        assertThat(options.receivedContent()).isNull();
    }

    @Test
    void withReceivedContentSetsSource() throws Exception {
        byte[] body = {1, 2, 3};
        ContentSource source = () -> body;
        VerifyOptions options = VerifyOptions.none().withReceivedContent(source);

        assertThat(options.receivedContent()).isSameAs(source);
        assertThat(options.receivedContent().read()).isEqualTo(body);
    }

    @Test
    void withReceivedContentPreservesAccessToken() {
        VerifyOptions options = VerifyOptions.withAccessToken("token").withReceivedContent(() -> new byte[0]);

        assertThat(options.accessToken()).isEqualTo("token");
        assertThat(options.receivedContent()).isNotNull();
    }

    @Test
    void withReceivedContentRejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> VerifyOptions.none().withReceivedContent(null));
    }

    @Test
    void withAccessTokenRejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> VerifyOptions.withAccessToken(null));
    }
}
