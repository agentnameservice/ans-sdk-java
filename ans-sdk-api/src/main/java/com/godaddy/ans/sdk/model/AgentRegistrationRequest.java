package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * AgentRegistrationRequest
 */
@JsonPropertyOrder({
    AgentRegistrationRequest.JSON_PROPERTY_AGENT_DISPLAY_NAME,
    AgentRegistrationRequest.JSON_PROPERTY_AGENT_DESCRIPTION,
    AgentRegistrationRequest.JSON_PROPERTY_VERSION,
    AgentRegistrationRequest.JSON_PROPERTY_AGENT_HOST,
    AgentRegistrationRequest.JSON_PROPERTY_ENDPOINTS,
    AgentRegistrationRequest.JSON_PROPERTY_SERVER_CSR_P_E_M,
    AgentRegistrationRequest.JSON_PROPERTY_SERVER_CERTIFICATE_P_E_M,
    AgentRegistrationRequest.JSON_PROPERTY_SERVER_CERTIFICATE_CHAIN_P_E_M,
    AgentRegistrationRequest.JSON_PROPERTY_IDENTITY_CSR_P_E_M,
    AgentRegistrationRequest.JSON_PROPERTY_DISCOVERY_PROFILES
})
public class AgentRegistrationRequest {
    public static final String JSON_PROPERTY_AGENT_DISPLAY_NAME = "agentDisplayName";

    @Nonnull
    private String agentDisplayName;

    public static final String JSON_PROPERTY_AGENT_DESCRIPTION = "agentDescription";

    @Nullable
    private String agentDescription;

    public static final String JSON_PROPERTY_VERSION = "version";

    @Nonnull
    private String version;

    public static final String JSON_PROPERTY_AGENT_HOST = "agentHost";

    @Nonnull
    private String agentHost;

    public static final String JSON_PROPERTY_ENDPOINTS = "endpoints";

    @Nonnull
    private List<AgentEndpoint> endpoints = new ArrayList<>();

    public static final String JSON_PROPERTY_SERVER_CSR_P_E_M = "serverCsrPEM";

    @Nullable
    private String serverCsrPEM;

    public static final String JSON_PROPERTY_SERVER_CERTIFICATE_P_E_M = "serverCertificatePEM";

    @Nullable
    private String serverCertificatePEM;

    public static final String JSON_PROPERTY_SERVER_CERTIFICATE_CHAIN_P_E_M = "serverCertificateChainPEM";

    @Nullable
    private String serverCertificateChainPEM;

    public static final String JSON_PROPERTY_IDENTITY_CSR_P_E_M = "identityCsrPEM";

    @Nullable
    private String identityCsrPEM;

    public static final String JSON_PROPERTY_DISCOVERY_PROFILES = "discoveryProfiles";

    @Nullable
    private Set<DiscoveryProfile> discoveryProfiles = new LinkedHashSet<>();

    public AgentRegistrationRequest() {
    }

    public AgentRegistrationRequest agentDisplayName(@Nonnull String agentDisplayName) {
        this.agentDisplayName = agentDisplayName;
        return this;
    }

    /**
     * Get agentDisplayName
     * @return agentDisplayName
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_DISPLAY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAgentDisplayName() {
        return agentDisplayName;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_DISPLAY_NAME, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentDisplayName(@Nonnull String agentDisplayName) {
        this.agentDisplayName = agentDisplayName;
    }

    public AgentRegistrationRequest agentDescription(@Nullable String agentDescription) {
        this.agentDescription = agentDescription;
        return this;
    }

    /**
     * Get agentDescription
     * @return agentDescription
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_AGENT_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getAgentDescription() {
        return agentDescription;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_DESCRIPTION, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setAgentDescription(@Nullable String agentDescription) {
        this.agentDescription = agentDescription;
    }

    public AgentRegistrationRequest version(@Nonnull String version) {
        this.version = version;
        return this;
    }

    /**
     * Get version
     * @return version
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_VERSION, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getVersion() {
        return version;
    }

    @JsonProperty(value = JSON_PROPERTY_VERSION, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setVersion(@Nonnull String version) {
        this.version = version;
    }

    public AgentRegistrationRequest agentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
        return this;
    }

    /**
     * Get agentHost
     * @return agentHost
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_HOST, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public String getAgentHost() {
        return agentHost;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_HOST, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentHost(@Nonnull String agentHost) {
        this.agentHost = agentHost;
    }

    public AgentRegistrationRequest endpoints(@Nonnull List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    public AgentRegistrationRequest addEndpointsItem(AgentEndpoint endpointsItem) {
        if (this.endpoints == null) {
            this.endpoints = new ArrayList<>();
        }
        this.endpoints.add(endpointsItem);
        return this;
    }

    /**
     * One or more protocol endpoints. Each protocol may appear at most once across the array — a second endpoint with
     * an already-used protocol is rejected with 422 DUPLICATE_PROTOCOL, and an exact protocol+agentUrl repeat with
     * 422 DUPLICATE_ENDPOINT. (By-field uniqueness is not expressible in OpenAPI/JSON Schema; enforced in
     * internal/domain/endpoint.go AgentEndpoints.Validate.)
     * @return endpoints
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_ENDPOINTS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public List<AgentEndpoint> getEndpoints() {
        return endpoints;
    }

    @JsonProperty(value = JSON_PROPERTY_ENDPOINTS, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setEndpoints(@Nonnull List<AgentEndpoint> endpoints) {
        this.endpoints = endpoints;
    }

    public AgentRegistrationRequest serverCsrPEM(@Nullable String serverCsrPEM) {
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

    public AgentRegistrationRequest serverCertificatePEM(@Nullable String serverCertificatePEM) {
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

    public AgentRegistrationRequest serverCertificateChainPEM(@Nullable String serverCertificateChainPEM) {
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

    public AgentRegistrationRequest identityCsrPEM(@Nullable String identityCsrPEM) {
        this.identityCsrPEM = identityCsrPEM;
        return this;
    }

    /**
     * Optional. When supplied, the RA issues an identity certificate from this CSR at verify-acme. When omitted, the
     * agent registers without an identity certificate and cannot add one later — it must register a new version
     * instead.
     * @return identityCsrPEM
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_IDENTITY_CSR_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getIdentityCsrPEM() {
        return identityCsrPEM;
    }

    @JsonProperty(value = JSON_PROPERTY_IDENTITY_CSR_P_E_M, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setIdentityCsrPEM(@Nullable String identityCsrPEM) {
        this.identityCsrPEM = identityCsrPEM;
    }

    public AgentRegistrationRequest discoveryProfiles(@Nullable Set<DiscoveryProfile> discoveryProfiles) {
        this.discoveryProfiles = discoveryProfiles;
        return this;
    }

    public AgentRegistrationRequest addDiscoveryProfilesItem(DiscoveryProfile discoveryProfilesItem) {
        if (this.discoveryProfiles == null) {
            this.discoveryProfiles = new LinkedHashSet<>();
        }
        this.discoveryProfiles.add(discoveryProfilesItem);
        return this;
    }

    /**
     * Set of DNS record families the RA tells the operator to publish and emits in the AGENT_REGISTERED TL event&#39;s
     * attestations.dnsRecordsProvisioned[]. The computed records surface to the client on GET /v2/ans/agents/{agentId}
     * as registrationPending.dnsRecords[] once the agent reaches PENDING_DNS — not on the 202 register response, which
     * returns only the ACME challenge (production records are deferred until verify-acme proves domain control and
     * issues the certificates the TLSA binding depends on).  Each value names one record family; an operator publishing
     * the union (DNS-AID-aligned SVCB plus the original &#x60;_ans&#x60; TXT shape) sends both. Order is not
     * significant.  Optional. Omitted (or an explicit empty array) normalizes to the default [\&quot;ANS_DNSAID\&quot;]
     * server-side; opt into the legacy [\&quot;ANS_TXT\&quot;] shape explicitly. The
     * &#x60;minItems&#x60;/&#x60;uniqueItems&#x60; schema constraints are the canonical client contract for a present
     * array — validate before sending. A non-conformant request (an explicit empty array, or duplicate values) is
     * handled defensively rather than rejected: empty is treated as omitted and duplicates are ignored, but conformant
     * clients never send either.
     * @return discoveryProfiles
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DISCOVERY_PROFILES, required = false)
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public Set<DiscoveryProfile> getDiscoveryProfiles() {
        return discoveryProfiles;
    }

    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonProperty(value = JSON_PROPERTY_DISCOVERY_PROFILES, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDiscoveryProfiles(@Nullable Set<DiscoveryProfile> discoveryProfiles) {
        this.discoveryProfiles = discoveryProfiles;
    }

    /**
     * Return true if this AgentRegistrationRequest object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentRegistrationRequest agentRegistrationRequest = (AgentRegistrationRequest) o;
        return Objects.equals(this.agentDisplayName, agentRegistrationRequest.agentDisplayName) &&
                Objects.equals(this.agentDescription, agentRegistrationRequest.agentDescription) &&
                Objects.equals(this.version, agentRegistrationRequest.version) &&
                Objects.equals(this.agentHost, agentRegistrationRequest.agentHost) &&
                Objects.equals(this.endpoints, agentRegistrationRequest.endpoints) &&
                Objects.equals(this.serverCsrPEM, agentRegistrationRequest.serverCsrPEM) &&
                Objects.equals(this.serverCertificatePEM, agentRegistrationRequest.serverCertificatePEM) &&
                Objects.equals(this.serverCertificateChainPEM, agentRegistrationRequest.serverCertificateChainPEM) &&
                Objects.equals(this.identityCsrPEM, agentRegistrationRequest.identityCsrPEM) &&
                Objects.equals(this.discoveryProfiles, agentRegistrationRequest.discoveryProfiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentDisplayName, agentDescription, version, agentHost, endpoints, serverCsrPEM,
                serverCertificatePEM, serverCertificateChainPEM, identityCsrPEM, discoveryProfiles);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentRegistrationRequest {\n");
        sb.append("    agentDisplayName: ").append(toIndentedString(agentDisplayName)).append("\n");
        sb.append("    agentDescription: ").append(toIndentedString(agentDescription)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    agentHost: ").append(toIndentedString(agentHost)).append("\n");
        sb.append("    endpoints: ").append(toIndentedString(endpoints)).append("\n");
        sb.append("    serverCsrPEM: ").append(toIndentedString(serverCsrPEM)).append("\n");
        sb.append("    serverCertificatePEM: ").append(toIndentedString(serverCertificatePEM)).append("\n");
        sb.append("    serverCertificateChainPEM: ").append(toIndentedString(serverCertificateChainPEM)).append("\n");
        sb.append("    identityCsrPEM: ").append(toIndentedString(identityCsrPEM)).append("\n");
        sb.append("    discoveryProfiles: ").append(toIndentedString(discoveryProfiles)).append("\n");
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
