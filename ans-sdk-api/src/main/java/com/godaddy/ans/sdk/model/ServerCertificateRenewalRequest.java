package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nullable;

/**
 * ServerCertificateRenewalRequest
 */
@JsonPropertyOrder({
    ServerCertificateRenewalRequest.JSON_PROPERTY_SERVER_CSR_P_E_M,
    ServerCertificateRenewalRequest.JSON_PROPERTY_SERVER_CERTIFICATE_P_E_M,
    ServerCertificateRenewalRequest.JSON_PROPERTY_SERVER_CERTIFICATE_CHAIN_P_E_M
})
public class ServerCertificateRenewalRequest {
    public static final String JSON_PROPERTY_SERVER_CSR_P_E_M = "serverCsrPEM";

    @Nullable
    private String serverCsrPEM;

    public static final String JSON_PROPERTY_SERVER_CERTIFICATE_P_E_M = "serverCertificatePEM";

    @Nullable
    private String serverCertificatePEM;

    public static final String JSON_PROPERTY_SERVER_CERTIFICATE_CHAIN_P_E_M = "serverCertificateChainPEM";

    @Nullable
    private String serverCertificateChainPEM;

    public ServerCertificateRenewalRequest() {
    }

    public ServerCertificateRenewalRequest serverCsrPEM(@Nullable String serverCsrPEM) {
        this.serverCsrPEM = serverCsrPEM;
        return this;
    }

    /**
     * Get serverCsrPEM
     * @return serverCsrPEM
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_SERVER_CSR_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getServerCsrPEM() {
        return serverCsrPEM;
    }

    @JsonProperty(value = JSON_PROPERTY_SERVER_CSR_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServerCsrPEM(@Nullable String serverCsrPEM) {
        this.serverCsrPEM = serverCsrPEM;
    }

    public ServerCertificateRenewalRequest serverCertificatePEM(@Nullable String serverCertificatePEM) {
        this.serverCertificatePEM = serverCertificatePEM;
        return this;
    }

    /**
     * Get serverCertificatePEM
     * @return serverCertificatePEM
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_SERVER_CERTIFICATE_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getServerCertificatePEM() {
        return serverCertificatePEM;
    }

    @JsonProperty(value = JSON_PROPERTY_SERVER_CERTIFICATE_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServerCertificatePEM(@Nullable String serverCertificatePEM) {
        this.serverCertificatePEM = serverCertificatePEM;
    }

    public ServerCertificateRenewalRequest serverCertificateChainPEM(@Nullable String serverCertificateChainPEM) {
        this.serverCertificateChainPEM = serverCertificateChainPEM;
        return this;
    }

    /**
     * Get serverCertificateChainPEM
     * @return serverCertificateChainPEM
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_SERVER_CERTIFICATE_CHAIN_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getServerCertificateChainPEM() {
        return serverCertificateChainPEM;
    }

    @JsonProperty(value = JSON_PROPERTY_SERVER_CERTIFICATE_CHAIN_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setServerCertificateChainPEM(@Nullable String serverCertificateChainPEM) {
        this.serverCertificateChainPEM = serverCertificateChainPEM;
    }

    /**
     * Return true if this ServerCertificateRenewalRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ServerCertificateRenewalRequest serverCertificateRenewalRequest = (ServerCertificateRenewalRequest) o;
        return Objects.equals(this.serverCsrPEM, serverCertificateRenewalRequest.serverCsrPEM) &&
                Objects.equals(this.serverCertificatePEM, serverCertificateRenewalRequest.serverCertificatePEM) &&
                Objects.equals(this.serverCertificateChainPEM,
                        serverCertificateRenewalRequest.serverCertificateChainPEM);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverCsrPEM, serverCertificatePEM, serverCertificateChainPEM);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ServerCertificateRenewalRequest {\n");
        sb.append("    serverCsrPEM: ").append(toIndentedString(serverCsrPEM)).append("\n");
        sb.append("    serverCertificatePEM: ").append(toIndentedString(serverCertificatePEM)).append("\n");
        sb.append("    serverCertificateChainPEM: ").append(toIndentedString(serverCertificateChainPEM)).append("\n");
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
