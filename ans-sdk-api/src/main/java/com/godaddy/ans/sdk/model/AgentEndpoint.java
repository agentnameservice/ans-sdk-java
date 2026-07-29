package com.godaddy.ans.sdk.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * AgentEndpoint
 */
@JsonPropertyOrder({
    AgentEndpoint.JSON_PROPERTY_AGENT_URL,
    AgentEndpoint.JSON_PROPERTY_META_DATA_URL,
    AgentEndpoint.JSON_PROPERTY_META_DATA_HASH,
    AgentEndpoint.JSON_PROPERTY_DOCUMENTATION_URL,
    AgentEndpoint.JSON_PROPERTY_PROTOCOL,
    AgentEndpoint.JSON_PROPERTY_FUNCTIONS,
    AgentEndpoint.JSON_PROPERTY_TRANSPORTS
})public class AgentEndpoint {
    public static final String JSON_PROPERTY_AGENT_URL = "agentUrl";


    @Nonnull
    private URI agentUrl;

    public static final String JSON_PROPERTY_META_DATA_URL = "metaDataUrl";

    @Nullable
    private URI metaDataUrl;

    public static final String JSON_PROPERTY_META_DATA_HASH = "metaDataHash";

    @Nullable
    private String metaDataHash;

    public static final String JSON_PROPERTY_DOCUMENTATION_URL = "documentationUrl";

    @Nullable
    private URI documentationUrl;

    public static final String JSON_PROPERTY_PROTOCOL = "protocol";


    @Nonnull
    private Protocol protocol;

    public static final String JSON_PROPERTY_FUNCTIONS = "functions";

    @Nullable
    private List<AgentFunction> functions = new ArrayList<>();

    /**
     * Gets or Sets transports
     */
    public enum TransportsEnum {
        STREAMABLE_HTTP("STREAMABLE_HTTP"),

        SSE("SSE"),

        JSON_RPC("JSON_RPC"),

        GRPC("GRPC"),

        REST("REST"),

        HTTP("HTTP");

        private String value;

        TransportsEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }

        @JsonCreator
        public static TransportsEnum fromValue(String value) {
            for (TransportsEnum b : TransportsEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String JSON_PROPERTY_TRANSPORTS = "transports";

    @Nullable
    private List<TransportsEnum> transports = new ArrayList<>();

    public AgentEndpoint() {
    }

    public AgentEndpoint agentUrl(@Nonnull URI agentUrl) {
        this.agentUrl = agentUrl;
        return this;
    }

    /**
     * Get agentUrl
     * @return agentUrl
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_AGENT_URL, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public URI getAgentUrl() {
        return agentUrl;
    }

    @JsonProperty(value = JSON_PROPERTY_AGENT_URL, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setAgentUrl(@Nonnull URI agentUrl) {
        this.agentUrl = agentUrl;
    }

    public AgentEndpoint metaDataUrl(@Nullable URI metaDataUrl) {
        this.metaDataUrl = metaDataUrl;
        return this;
    }

    /**
     * Optional. The endpoint&#39;s metadata descriptor URL. Must be https and free of whitespace, quotes, and other characters that require SVCB presentation escaping — it is emitted verbatim as the ANS_DNSAID &#x60;cap&#x60; SvcParam (key65400) and is the document &#x60;cap-sha256&#x60; (key65401) digests. When it sits at https://{agentHost}/.well-known/&lt;suffix&gt;, the suffix is also advertised as the &#x60;well-known&#x60; SvcParam (key65409). NOTE: a &#x60;cap&#x60; whose host differs from the agent FQDN is published without an integrity pin unless metaDataHash is also supplied — consumers SHOULD prefer a metaDataHash-pinned descriptor for off-host metadata.
     * @return metaDataUrl
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_META_DATA_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public URI getMetaDataUrl() {
        return metaDataUrl;
    }

    @JsonProperty(value = JSON_PROPERTY_META_DATA_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setMetaDataUrl(@Nullable URI metaDataUrl) {
        this.metaDataUrl = metaDataUrl;
    }

    public AgentEndpoint metaDataHash(@Nullable String metaDataHash) {
        this.metaDataHash = metaDataHash;
        return this;
    }

    /**
     * Optional integrity pin over the metadata descriptor. Meaningless on its own: it MUST be accompanied by
     * metaDataUrl. A metaDataHash without a metaDataUrl is rejected with 422 INVALID_ENDPOINT.
     * @return metaDataHash
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_META_DATA_HASH, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public String getMetaDataHash() {
        return metaDataHash;
    }

    @JsonProperty(value = JSON_PROPERTY_META_DATA_HASH, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setMetaDataHash(@Nullable String metaDataHash) {
        this.metaDataHash = metaDataHash;
    }

    public AgentEndpoint documentationUrl(@Nullable URI documentationUrl) {
        this.documentationUrl = documentationUrl;
        return this;
    }

    /**
     * Get documentationUrl
     * @return documentationUrl
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_DOCUMENTATION_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public URI getDocumentationUrl() {
        return documentationUrl;
    }

    @JsonProperty(value = JSON_PROPERTY_DOCUMENTATION_URL, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setDocumentationUrl(@Nullable URI documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    public AgentEndpoint protocol(@Nonnull Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * Get protocol
     * @return protocol
     */
    @Nonnull
    @JsonProperty(value = JSON_PROPERTY_PROTOCOL, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public Protocol getProtocol() {
        return protocol;
    }

    @JsonProperty(value = JSON_PROPERTY_PROTOCOL, required = true)
    @JsonInclude(value = JsonInclude.Include.ALWAYS)
    public void setProtocol(@Nonnull Protocol protocol) {
        this.protocol = protocol;
    }

    public AgentEndpoint functions(@Nullable List<AgentFunction> functions) {
        this.functions = functions;
        return this;
    }

    public AgentEndpoint addFunctionsItem(AgentFunction functionsItem) {
        if (this.functions == null) {
            this.functions = new ArrayList<>();
        }
        this.functions.add(functionsItem);
        return this;
    }

    /**
     * Get functions
     * @return functions
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_FUNCTIONS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<AgentFunction> getFunctions() {
        return functions;
    }

    @JsonProperty(value = JSON_PROPERTY_FUNCTIONS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setFunctions(@Nullable List<AgentFunction> functions) {
        this.functions = functions;
    }

    public AgentEndpoint transports(@Nullable List<TransportsEnum> transports) {
        this.transports = transports;
        return this;
    }

    public AgentEndpoint addTransportsItem(TransportsEnum transportsItem) {
        if (this.transports == null) {
            this.transports = new ArrayList<>();
        }
        this.transports.add(transportsItem);
        return this;
    }

    /**
     * Get transports
     * @return transports
     */
    @Nullable
    @JsonProperty(value = JSON_PROPERTY_TRANSPORTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public List<TransportsEnum> getTransports() {
        return transports;
    }

    @JsonProperty(value = JSON_PROPERTY_TRANSPORTS, required = false)
    @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
    public void setTransports(@Nullable List<TransportsEnum> transports) {
        this.transports = transports;
    }

    /**
     * Return true if this AgentEndpoint object is equal to o.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AgentEndpoint agentEndpoint = (AgentEndpoint) o;
        return Objects.equals(this.agentUrl, agentEndpoint.agentUrl) &&
                Objects.equals(this.metaDataUrl, agentEndpoint.metaDataUrl) &&
                Objects.equals(this.metaDataHash, agentEndpoint.metaDataHash) &&
                Objects.equals(this.documentationUrl, agentEndpoint.documentationUrl) &&
                Objects.equals(this.protocol, agentEndpoint.protocol) &&
                Objects.equals(this.functions, agentEndpoint.functions) &&
                Objects.equals(this.transports, agentEndpoint.transports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentUrl, metaDataUrl, metaDataHash, documentationUrl, protocol, functions, transports);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AgentEndpoint {\n");
        sb.append("    agentUrl: ").append(toIndentedString(agentUrl)).append("\n");
        sb.append("    metaDataUrl: ").append(toIndentedString(metaDataUrl)).append("\n");
        sb.append("    metaDataHash: ").append(toIndentedString(metaDataHash)).append("\n");
        sb.append("    documentationUrl: ").append(toIndentedString(documentationUrl)).append("\n");
        sb.append("    protocol: ").append(toIndentedString(protocol)).append("\n");
        sb.append("    functions: ").append(toIndentedString(functions)).append("\n");
        sb.append("    transports: ").append(toIndentedString(transports)).append("\n");
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
