package com.godaddy.ans.sdk.transparency;

import com.godaddy.ans.sdk.exception.AnsServerException;

/**
 * Thrown when a SCITT receipt is not yet available because the leaf is committed but no signed
 * checkpoint covers it yet ({@code 503 TL_LEAF_UNCOMMITTED}).
 *
 * <p>This is a transient, retryable condition, not a hard error. The caller retries the receipt
 * read after the delay in {@link #getRetryAfterSeconds()} (the server's {@code Retry-After}
 * header). {@link #isRetryable()} returns {@code true}.</p>
 */
public class TlLeafUncommittedException extends AnsServerException {

    /** The stable error code the transparency log returns for this condition. */
    public static final String ERROR_CODE = "TL_LEAF_UNCOMMITTED";

    /** The HTTP status this condition always carries. Shared with the response mapper in the same package. */
    static final int STATUS_SERVICE_UNAVAILABLE = 503;

    private final int retryAfterSeconds;

    /**
     * Creates a new exception for an uncommitted-leaf receipt read.
     *
     * @param message the error message
     * @param retryAfterSeconds the retry delay from the {@code Retry-After} header, or 0 if absent
     * @param requestId the request ID from the server response, may be null
     */
    public TlLeafUncommittedException(String message, int retryAfterSeconds, String requestId) {
        super(message, STATUS_SERVICE_UNAVAILABLE, requestId);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Returns the retry delay in seconds from the server's {@code Retry-After} header.
     *
     * @return the retry delay in seconds, or 0 if the server did not provide one
     */
    public int getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
