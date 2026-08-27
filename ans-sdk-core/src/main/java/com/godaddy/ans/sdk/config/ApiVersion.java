package com.godaddy.ans.sdk.config;

/**
 * Selects which ANS API lane the SDK targets.
 *
 * <p>New clients default to {@link #V2}. {@link #V1} remains available as an
 * explicit opt-in for existing integrators, since both API lanes stay live.</p>
 */
public enum ApiVersion {

    /**
     * Legacy {@code /v1/agents} API lane.
     */
    V1,

    /**
     * Current {@code /v2/ans/agents} API lane (default for new clients).
     */
    V2
}
