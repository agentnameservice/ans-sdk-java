package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

/**
 * Certificate information in V2 schema attestations.
 *
 * <p>Unlike {@link CertificateInfo}, V2 carries an explicit {@code notAfter} expiry and appears
 * inside the {@code identityCerts}/{@code serverCerts} arrays.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateInfoV2 {

    @JsonProperty("fingerprint")
    private String fingerprint;

    @JsonProperty("notAfter")
    private OffsetDateTime notAfter;

    @JsonProperty("type")
    private CertType type;

    public CertificateInfoV2() {
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public OffsetDateTime getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(OffsetDateTime notAfter) {
        this.notAfter = notAfter;
    }

    public CertType getType() {
        return type;
    }

    public void setType(CertType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CertificateInfoV2{"
            + "fingerprint='" + fingerprint + '\''
            + ", notAfter=" + notAfter
            + ", type=" + type
            + '}';
    }
}
