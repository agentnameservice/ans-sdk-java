package com.godaddy.ans.sdk.registration;

import static com.godaddy.ans.sdk.util.Identifiers.requireUuid;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

/**
 * Builds ANS Verified-Identity API paths on the RA management host.
 *
 * <p>All identity path branching lives here so the identity service never
 * string-concatenates paths inline. Identities are a v2-only feature, so every
 * path is rooted at the fixed {@code /v2/ans/identities} collection.</p>
 */
final class IdentityPaths {

    private static final String COLLECTION = "/v2/ans/identities";

    private IdentityPaths() {
    }

    /**
     * Returns the identities collection path.
     *
     * @return {@code /v2/ans/identities}
     */
    static String identitiesCollectionPath() {
        return COLLECTION;
    }

    /**
     * Builds the paginated identities collection path with optional query parameters.
     *
     * <p>Mirrors the agent-list cursor convention: {@code limit} (1..100) and an
     * opaque {@code cursor}. Null arguments are omitted so the server applies its
     * defaults.</p>
     *
     * @param limit optional page size, or {@code null} for the server default
     * @param cursor optional opaque page cursor, or {@code null} for the first page
     * @return {@code /v2/ans/identities} with an appended query string when needed
     */
    static String listPath(Integer limit, String cursor) {
        StringJoiner query = new StringJoiner("&");
        if (limit != null) {
            query.add("limit=" + limit);
        }
        if (cursor != null) {
            query.add("cursor=" + URLEncoder.encode(cursor, StandardCharsets.UTF_8));
        }
        return query.length() == 0 ? COLLECTION : COLLECTION + "?" + query;
    }

    /**
     * Builds an identity-scoped path: collection + identityId + any trailing segments.
     *
     * @param identityId the identity ID
     * @param segments optional trailing path segments (e.g. {@code "verify-control"})
     * @return the joined path, e.g. {@code /v2/ans/identities/{identityId}/verify-control}
     */
    static String identityPath(String identityId, String... segments) {
        StringBuilder path = new StringBuilder(COLLECTION)
            .append('/')
            .append(requireUuid(identityId, "identityId"));
        for (String segment : segments) {
            path.append('/').append(segment);
        }
        return path.toString();
    }

    /**
     * Returns the control-proof submission path for an identity.
     *
     * @param identityId the identity ID
     * @return {@code /v2/ans/identities/{identityId}/verify-control}
     */
    static String verifyControlPath(String identityId) {
        return identityPath(identityId, "verify-control");
    }

    /**
     * Returns the revocation path for an identity.
     *
     * @param identityId the identity ID
     * @return {@code /v2/ans/identities/{identityId}/revoke}
     */
    static String revokePath(String identityId) {
        return identityPath(identityId, "revoke");
    }

    /**
     * Returns the links collection path for an identity.
     *
     * @param identityId the identity ID
     * @return {@code /v2/ans/identities/{identityId}/links}
     */
    static String linksPath(String identityId) {
        return identityPath(identityId, "links");
    }

    /**
     * Returns the path for a single identity-to-agent link.
     *
     * @param identityId the identity ID
     * @param agentId the linked agent ID
     * @return {@code /v2/ans/identities/{identityId}/links/{agentId}}
     */
    static String linkPath(String identityId, String agentId) {
        return identityPath(identityId, "links", requireUuid(agentId, "agentId"));
    }
}
