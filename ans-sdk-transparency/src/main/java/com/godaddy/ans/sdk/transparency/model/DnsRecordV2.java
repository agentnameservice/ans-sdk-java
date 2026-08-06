package com.godaddy.ans.sdk.transparency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A provisioned DNS record in V2 schema attestations.
 *
 * <p>V2 carries {@code dnsRecordsProvisioned} as a list of typed records, unlike the V0/V1
 * name-to-value map.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DnsRecordV2 {

    @JsonProperty("data")
    private String data;

    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    public DnsRecordV2() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DnsRecordV2{"
            + "name='" + name + '\''
            + ", type='" + type + '\''
            + ", data='" + data + '\''
            + '}';
    }
}
