package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * CertificateResponse
 */
@JsonPropertyOrder({
    CertificateResponse.JSON_PROPERTY_CSR_ID,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_SUBJECT,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_ISSUER,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_SERIAL_NUMBER,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_VALID_FROM,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_VALID_TO,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_P_E_M,
    CertificateResponse.JSON_PROPERTY_CHAIN_P_E_M,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_PUBLIC_KEY_ALGORITHM,
    CertificateResponse.JSON_PROPERTY_CERTIFICATE_SIGNATURE_ALGORITHM
})
public class CertificateResponse {
    public static final String JSON_PROPERTY_CSR_ID = "csrId";

    @Nonnull
    private UUID csrId;

    public static final String JSON_PROPERTY_CERTIFICATE_SUBJECT = "certificateSubject";

    @Nullable
    private String certificateSubject;

    public static final String JSON_PROPERTY_CERTIFICATE_ISSUER = "certificateIssuer";

    @Nullable
    private String certificateIssuer;

    public static final String JSON_PROPERTY_CERTIFICATE_SERIAL_NUMBER = "certificateSerialNumber";

    @Nullable
    private String certificateSerialNumber;

    public static final String JSON_PROPERTY_CERTIFICATE_VALID_FROM = "certificateValidFrom";

    @Nonnull
    private OffsetDateTime certificateValidFrom;

    public static final String JSON_PROPERTY_CERTIFICATE_VALID_TO = "certificateValidTo";

    @Nonnull
    private OffsetDateTime certificateValidTo;

    public static final String JSON_PROPERTY_CERTIFICATE_P_E_M = "certificatePEM";

    @Nonnull
    private String certificatePEM;

    public static final String JSON_PROPERTY_CHAIN_P_E_M = "chainPEM";

    @Nullable
    private String chainPEM;

    public static final String JSON_PROPERTY_CERTIFICATE_PUBLIC_KEY_ALGORITHM = "certificatePublicKeyAlgorithm";

    @Nullable
    private String certificatePublicKeyAlgorithm;

    public static final String JSON_PROPERTY_CERTIFICATE_SIGNATURE_ALGORITHM = "certificateSignatureAlgorithm";

    @Nullable
    private String certificateSignatureAlgorithm;

    public CertificateResponse() {
    }

    public CertificateResponse csrId(@Nonnull UUID csrId) {
        this.csrId = csrId;
        return this;
    }

    /**
     * Get csrId
     * @return csrId
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CSR_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public UUID getCsrId() {
        return csrId;
    }

    @JsonProperty(value = JSON_PROPERTY_CSR_ID, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCsrId(@Nonnull UUID csrId) {
        this.csrId = csrId;
    }

    public CertificateResponse certificateSubject(@Nullable String certificateSubject) {
        this.certificateSubject = certificateSubject;
        return this;
    }

    /**
     * Get certificateSubject
     * @return certificateSubject
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_SUBJECT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCertificateSubject() {
        return certificateSubject;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_SUBJECT, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCertificateSubject(@Nullable String certificateSubject) {
        this.certificateSubject = certificateSubject;
    }

    public CertificateResponse certificateIssuer(@Nullable String certificateIssuer) {
        this.certificateIssuer = certificateIssuer;
        return this;
    }

    /**
     * Get certificateIssuer
     * @return certificateIssuer
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_ISSUER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCertificateIssuer() {
        return certificateIssuer;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_ISSUER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCertificateIssuer(@Nullable String certificateIssuer) {
        this.certificateIssuer = certificateIssuer;
    }

    public CertificateResponse certificateSerialNumber(@Nullable String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
        return this;
    }

    /**
     * Get certificateSerialNumber
     * @return certificateSerialNumber
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_SERIAL_NUMBER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCertificateSerialNumber() {
        return certificateSerialNumber;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_SERIAL_NUMBER, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCertificateSerialNumber(@Nullable String certificateSerialNumber) {
        this.certificateSerialNumber = certificateSerialNumber;
    }

    public CertificateResponse certificateValidFrom(@Nonnull OffsetDateTime certificateValidFrom) {
        this.certificateValidFrom = certificateValidFrom;
        return this;
    }

    /**
     * Get certificateValidFrom
     * @return certificateValidFrom
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_VALID_FROM, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getCertificateValidFrom() {
        return certificateValidFrom;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_VALID_FROM, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCertificateValidFrom(@Nonnull OffsetDateTime certificateValidFrom) {
        this.certificateValidFrom = certificateValidFrom;
    }

    public CertificateResponse certificateValidTo(@Nonnull OffsetDateTime certificateValidTo) {
        this.certificateValidTo = certificateValidTo;
        return this;
    }

    /**
     * Get certificateValidTo
     * @return certificateValidTo
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_VALID_TO, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public OffsetDateTime getCertificateValidTo() {
        return certificateValidTo;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_VALID_TO, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCertificateValidTo(@Nonnull OffsetDateTime certificateValidTo) {
        this.certificateValidTo = certificateValidTo;
    }

    public CertificateResponse certificatePEM(@Nonnull String certificatePEM) {
        this.certificatePEM = certificatePEM;
        return this;
    }

    /**
     * Get certificatePEM
     * @return certificatePEM
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_P_E_M, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getCertificatePEM() {
        return certificatePEM;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_P_E_M, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setCertificatePEM(@Nonnull String certificatePEM) {
        this.certificatePEM = certificatePEM;
    }

    public CertificateResponse chainPEM(@Nullable String chainPEM) {
        this.chainPEM = chainPEM;
        return this;
    }

    /**
     * Get chainPEM
     * @return chainPEM
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CHAIN_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getChainPEM() {
        return chainPEM;
    }

    @JsonProperty(value = JSON_PROPERTY_CHAIN_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setChainPEM(@Nullable String chainPEM) {
        this.chainPEM = chainPEM;
    }

    public CertificateResponse certificatePublicKeyAlgorithm(@Nullable String certificatePublicKeyAlgorithm) {
        this.certificatePublicKeyAlgorithm = certificatePublicKeyAlgorithm;
        return this;
    }

    /**
     * Get certificatePublicKeyAlgorithm
     * @return certificatePublicKeyAlgorithm
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_PUBLIC_KEY_ALGORITHM, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCertificatePublicKeyAlgorithm() {
        return certificatePublicKeyAlgorithm;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_PUBLIC_KEY_ALGORITHM, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCertificatePublicKeyAlgorithm(@Nullable String certificatePublicKeyAlgorithm) {
        this.certificatePublicKeyAlgorithm = certificatePublicKeyAlgorithm;
    }

    public CertificateResponse certificateSignatureAlgorithm(@Nullable String certificateSignatureAlgorithm) {
        this.certificateSignatureAlgorithm = certificateSignatureAlgorithm;
        return this;
    }

    /**
     * Get certificateSignatureAlgorithm
     * @return certificateSignatureAlgorithm
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_SIGNATURE_ALGORITHM, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getCertificateSignatureAlgorithm() {
        return certificateSignatureAlgorithm;
    }

    @JsonProperty(value = JSON_PROPERTY_CERTIFICATE_SIGNATURE_ALGORITHM, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setCertificateSignatureAlgorithm(@Nullable String certificateSignatureAlgorithm) {
        this.certificateSignatureAlgorithm = certificateSignatureAlgorithm;
    }

    /**
     * Return true if this CertificateResponse object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateResponse certificateResponse = (CertificateResponse) o;
        return Objects.equals(this.csrId, certificateResponse.csrId) &&
                Objects.equals(this.certificateSubject, certificateResponse.certificateSubject) &&
                Objects.equals(this.certificateIssuer, certificateResponse.certificateIssuer) &&
                Objects.equals(this.certificateSerialNumber, certificateResponse.certificateSerialNumber) &&
                Objects.equals(this.certificateValidFrom, certificateResponse.certificateValidFrom) &&
                Objects.equals(this.certificateValidTo, certificateResponse.certificateValidTo) &&
                Objects.equals(this.certificatePEM, certificateResponse.certificatePEM) &&
                Objects.equals(this.chainPEM, certificateResponse.chainPEM) &&
                Objects.equals(this.certificatePublicKeyAlgorithm, certificateResponse.certificatePublicKeyAlgorithm) &&
                Objects.equals(this.certificateSignatureAlgorithm, certificateResponse.certificateSignatureAlgorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csrId, certificateSubject, certificateIssuer, certificateSerialNumber, certificateValidFrom,
                certificateValidTo, certificatePEM, chainPEM, certificatePublicKeyAlgorithm,
                certificateSignatureAlgorithm);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CertificateResponse {\n");
        sb.append("    csrId: ").append(toIndentedString(csrId)).append("\n");
        sb.append("    certificateSubject: ").append(toIndentedString(certificateSubject)).append("\n");
        sb.append("    certificateIssuer: ").append(toIndentedString(certificateIssuer)).append("\n");
        sb.append("    certificateSerialNumber: ").append(toIndentedString(certificateSerialNumber)).append("\n");
        sb.append("    certificateValidFrom: ").append(toIndentedString(certificateValidFrom)).append("\n");
        sb.append("    certificateValidTo: ").append(toIndentedString(certificateValidTo)).append("\n");
        sb.append("    certificatePEM: ").append(toIndentedString(certificatePEM)).append("\n");
        sb.append("    chainPEM: ").append(toIndentedString(chainPEM)).append("\n");
        sb.append("    certificatePublicKeyAlgorithm: ").append(toIndentedString(certificatePublicKeyAlgorithm))
                .append("\n");
        sb.append("    certificateSignatureAlgorithm: ").append(toIndentedString(certificateSignatureAlgorithm))
                .append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

}
