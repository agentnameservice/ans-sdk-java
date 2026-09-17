package com.godaddy.ans.sdk.pop;

import java.io.IOException;

/**
 * Supplies the request content a DPoP proof binds through {@code ans_content_digest}
 * (ANS-6 §7.13). The verifier reads it once, after the proof is bound to a live ANS
 * identity and before the proof id is recorded, so an unauthenticated caller never
 * causes content to be read or hashed. The bytes must be the content octets exactly as
 * received: after transfer codings are removed, with any content coding still applied
 * (RFC 9110 §6.4). A request without content is an absent source or an empty array.
 */
@FunctionalInterface
public interface ContentSource {

    /**
     * Returns the received content octets; an empty array for a request without content.
     *
     * @throws IOException when the content cannot be read, including when it exceeds a caller's bound
     */
    byte[] read() throws IOException;
}
