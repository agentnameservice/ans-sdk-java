package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * One computed identities[] entry on the agent badge (transparency-log per-link view).
 *
 * <p>The transparency log computes this view at query time. It carries the shared identity fields
 * plus the per-link key material and audit references that the RA {@code LinkedIdentity} model does
 * not model: {@link #getKeys()}, {@link #getKeysLogId()}, and {@link #getLinkLogId()}. A REVOKED
 * identity stays visible, but its {@code keys} and {@code keysLogId} are withheld.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedIdentityView {

    @JsonProperty("identityId")
    private String identityId;

    @JsonProperty("kind")
    private String kind;

    @JsonProperty("value")
    private String value;

    @JsonProperty("identityStatus")
    private String identityStatus;

    @JsonProperty("linkedAt")
    private String linkedAt;

    @JsonProperty("keys")
    private List<Map<String, Object>> keys;

    @JsonProperty("keysLogId")
    private String keysLogId;

    @JsonProperty("linkLogId")
    private String linkLogId;

    public LinkedIdentityView() {
    }

    /**
     * Returns the identity's opaque identifier.
     *
     * @return the identity id
     */
    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    /**
     * Returns the identity kind ({@code did:web}, {@code did:key}, or {@code lei}).
     *
     * @return the kind, or null if not provided
     */
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * Returns the identity value (the DID or LEI).
     *
     * @return the value, or null if not provided
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the identity's current stream status ({@code VERIFIED} or {@code REVOKED}).
     *
     * @return the identity status, or null if not provided
     */
    public String getIdentityStatus() {
        return identityStatus;
    }

    public void setIdentityStatus(String identityStatus) {
        this.identityStatus = identityStatus;
    }

    /**
     * Returns the producer timestamp of the sealed link event that bound this identity.
     *
     * @return the link timestamp, or null if not provided
     */
    public String getLinkedAt() {
        return linkedAt;
    }

    public void setLinkedAt(String linkedAt) {
        this.linkedAt = linkedAt;
    }

    /**
     * Returns the CURRENT proven key set, quoted verbatim from the latest sealed proof event —
     * verification methods only. Withheld when the identity is REVOKED.
     *
     * @return the proven key set, or null when withheld / not provided
     */
    public List<Map<String, Object>> getKeys() {
        return keys;
    }

    public void setKeys(List<Map<String, Object>> keys) {
        this.keys = keys;
    }

    /**
     * Returns the sealed proof event that {@link #getKeys()} is quoted from — fetch for signed-proof /
     * offline evidence. Withheld when the identity is REVOKED.
     *
     * @return the keys log id, or null when withheld / not provided
     */
    public String getKeysLogId() {
        return keysLogId;
    }

    public void setKeysLogId(String keysLogId) {
        this.keysLogId = keysLogId;
    }

    /**
     * Returns the sealed {@code IDENTITY_LINKED} entry on the identity stream — fetch for link evidence.
     *
     * @return the link log id, or null if not provided
     */
    public String getLinkLogId() {
        return linkLogId;
    }

    public void setLinkLogId(String linkLogId) {
        this.linkLogId = linkLogId;
    }

    @Override
    public String toString() {
        return "LinkedIdentityView{"
            + "identityId='" + identityId + '\''
            + ", kind='" + kind + '\''
            + ", identityStatus='" + identityStatus + '\''
            + '}';
    }
}