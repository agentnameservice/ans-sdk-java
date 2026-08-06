package com.godaddy.ans.sdk.registration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link IdentityPaths}, the single source of RA Verified-Identity paths.
 *
 * <p>Each method is pinned to an exact string, so a typo in a path constant fails
 * here at the source, not as an opaque WireMock stub miss. Every identity path sits
 * under {@code /v2/ans/identities}.</p>
 */
class IdentityPathsTest {

    private static final String IDENTITY_ID = "id-550e8400-e29b-41d4-a716-446655440000";
    private static final String AGENT_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String COLLECTION = "/v2/ans/identities";

    @Test
    @DisplayName("identitiesCollectionPath returns the identities collection root")
    void identitiesCollectionPath() {
        assertThat(IdentityPaths.identitiesCollectionPath()).isEqualTo(COLLECTION);
    }

    @Test
    @DisplayName("identityPath with no trailing segments returns collection/{identityId}")
    void identityPathNoSegments() {
        assertThat(IdentityPaths.identityPath(IDENTITY_ID)).isEqualTo(COLLECTION + "/" + IDENTITY_ID);
    }

    @Test
    @DisplayName("identityPath appends trailing segments in order")
    void identityPathMultipleSegments() {
        assertThat(IdentityPaths.identityPath(IDENTITY_ID, "links", AGENT_ID))
            .isEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links/" + AGENT_ID);
    }

    @Test
    @DisplayName("verifyControlPath targets the verify-control sub-resource")
    void verifyControlPath() {
        assertThat(IdentityPaths.verifyControlPath(IDENTITY_ID))
            .isEqualTo(COLLECTION + "/" + IDENTITY_ID + "/verify-control");
    }

    @Test
    @DisplayName("revokePath targets the revoke sub-resource")
    void revokePath() {
        assertThat(IdentityPaths.revokePath(IDENTITY_ID))
            .isEqualTo(COLLECTION + "/" + IDENTITY_ID + "/revoke");
    }

    @Test
    @DisplayName("linksPath targets the links collection")
    void linksPath() {
        assertThat(IdentityPaths.linksPath(IDENTITY_ID))
            .isEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links");
    }

    @Test
    @DisplayName("linkPath targets a single identity-to-agent link")
    void linkPath() {
        assertThat(IdentityPaths.linkPath(IDENTITY_ID, AGENT_ID))
            .isEqualTo(COLLECTION + "/" + IDENTITY_ID + "/links/" + AGENT_ID);
    }

    @Test
    @DisplayName("every identity path is rooted at /v2/ans/identities")
    void allPathsRootedAtCollection() {
        assertThat(IdentityPaths.identitiesCollectionPath()).startsWith(COLLECTION);
        assertThat(IdentityPaths.identityPath(IDENTITY_ID)).startsWith(COLLECTION);
        assertThat(IdentityPaths.verifyControlPath(IDENTITY_ID)).startsWith(COLLECTION);
        assertThat(IdentityPaths.revokePath(IDENTITY_ID)).startsWith(COLLECTION);
        assertThat(IdentityPaths.linksPath(IDENTITY_ID)).startsWith(COLLECTION);
        assertThat(IdentityPaths.linkPath(IDENTITY_ID, AGENT_ID)).startsWith(COLLECTION);
    }
}
