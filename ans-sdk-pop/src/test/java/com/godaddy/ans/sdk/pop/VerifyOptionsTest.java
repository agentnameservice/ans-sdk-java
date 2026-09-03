package com.godaddy.ans.sdk.pop;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class VerifyOptionsTest {

    @Test
    void noneHasNoBindings() {
        VerifyOptions options = VerifyOptions.none();

        assertThat(options.accessToken()).isNull();
        assertThat(options.contentSha256()).isNull();
        assertThat(options.requireContentBinding()).isFalse();
    }

    @Test
    void withContentSha256CopiesArray() {
        byte[] hash = new byte[32];
        hash[0] = 1;
        VerifyOptions options = VerifyOptions.none().withContentSha256(hash);

        hash[0] = 2;

        assertThat(options.contentSha256()[0]).isEqualTo((byte) 1);
    }

    @Test
    void withContentSha256PreservesAccessToken() {
        VerifyOptions options = VerifyOptions.withAccessToken("token").withContentSha256(new byte[32]);

        assertThat(options.accessToken()).isEqualTo("token");
        assertThat(options.contentSha256()).hasSize(32);
    }

    @Test
    void withRequiredContentBindingSetsFlag() {
        VerifyOptions options = VerifyOptions.none().withContentSha256(new byte[32]).withRequiredContentBinding();

        assertThat(options.requireContentBinding()).isTrue();
        assertThat(options.contentSha256()).hasSize(32);
    }

    @Test
    void withContentSha256RejectsNull() {
        assertThatNullPointerException().isThrownBy(() -> VerifyOptions.none().withContentSha256(null));
    }
}