package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Attestations in V2 schema.
 *
 * <p>V2 replaces the V0/V1 {@code dnsRecordsProvisioned} map with a list of typed records, and the
 * singular {@code identityCert}/{@code serverCert} objects with {@code identityCerts}/{@code serverCerts}
 * arrays.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttestationsV2 {

    @JsonProperty("dnsRecordsProvisioned")
    private List<DnsRecordV2> dnsRecordsProvisioned;

    @JsonProperty("domainValidation")
    private String domainValidation;

    @JsonProperty("identityCerts")
    private List<CertificateInfoV2> identityCerts;

    @JsonProperty("serverCerts")
    private List<CertificateInfoV2> serverCerts;

    public AttestationsV2() {
    }

    public List<DnsRecordV2> getDnsRecordsProvisioned() {
        return dnsRecordsProvisioned;
    }

    public void setDnsRecordsProvisioned(List<DnsRecordV2> dnsRecordsProvisioned) {
        this.dnsRecordsProvisioned = dnsRecordsProvisioned;
    }

    public String getDomainValidation() {
        return domainValidation;
    }

    public void setDomainValidation(String domainValidation) {
        this.domainValidation = domainValidation;
    }

    public List<CertificateInfoV2> getIdentityCerts() {
        return identityCerts;
    }

    public void setIdentityCerts(List<CertificateInfoV2> identityCerts) {
        this.identityCerts = identityCerts;
    }

    public List<CertificateInfoV2> getServerCerts() {
        return serverCerts;
    }

    public void setServerCerts(List<CertificateInfoV2> serverCerts) {
        this.serverCerts = serverCerts;
    }

    @Override
    public String toString() {
        return "AttestationsV2{"
            + "dnsRecordsProvisioned=" + dnsRecordsProvisioned
            + ", domainValidation='" + domainValidation + '\''
            + ", identityCerts=" + identityCerts
            + ", serverCerts=" + serverCerts
            + '}';
    }
}
