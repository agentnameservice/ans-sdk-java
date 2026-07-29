package com.godaddy.ans.sdk.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Names one DNS record family the RA can emit for an agent registration. Used as the element type of discoveryProfiles[]. Each value provisions a complete record set, not a single record — the bullets below enumerate exactly what an operator must publish for that profile.  - ANS_DNSAID: the DNS-AID-aligned Consolidated Approach (RFC   9460), and the server default when discoveryProfiles is   omitted. Provisions:     1. One SVCB row per protocol endpoint at the bare FQDN        (ServiceMode &#x60;1 .&#x60;), carrying &#x60;alpn&#x60; (the protocol        token: a2a / mcp / x-http), &#x60;port&#x60; (the endpoint&#39;s TLS        port), &#x60;key65402&#x60; (bap, the agent protocol, always        present), and — when the endpoint supplies them —        &#x60;key65400&#x60; (cap, the metadataUrl capability locator),        &#x60;key65401&#x60; (cap-sha256, the base64url capability digest,        present only when the endpoint declared a metaDataHash),        and &#x60;key65409&#x60; (the well-known path suffix, emitted only        when metadataUrl sits at        https://{fqdn}/.well-known/&lt;suffix&gt;).        key65400/key65401/key65402/key65409 are the RFC 9460        §14.3.1 Private Use presentation of the DNS-AID draft-02        cap / cap-sha256 / bap / well-known SvcParams, which have        no IANA code point yet; the named forms are unpublishable        (strict RFC 9460 parsers reject them), so the keyNNNNN        forms are what reaches DNS. They switch back to named        forms if/when IANA registers the keys.     2. One &#x60;_ans-badge&#x60; TXT at &#x60;_ans-badge.{fqdn}&#x60; (the        transparency-log discovery hint).     3. One TLSA per distinct TLS port at &#x60;_&lt;port&gt;._tcp.{fqdn}&#x60;        binding the server certificate (DANE-EE, full cert,        SHA-256), emitted only when a server certificate exists. - ANS_TXT: original &#x60;_ans&#x60; TXT shape (one row per protocol at   &#x60;_ans.{fqdn}&#x60;), supported indefinitely for operators with   existing zone-edit tooling that targets &#x60;_ans.{fqdn}&#x60;;   opt-in via an explicit discoveryProfiles value. Emits an   HTTPS RR at the bare FQDN alongside carrying only &#x60;alpn&#x3D;h2&#x60;   (an IANA-registered SvcParam — no keyNNNNN form is needed   here; the asymmetry with ANS_DNSAID is intentional), since   &#x60;_ans&#x60; TXT carries no connection hints. The HTTPS RR is   best-effort: operators on CNAME-fronted apexes cannot publish   a record at that name (RFC 1034 §3.6.2) and verification does   not require it. Provisions the same &#x60;_ans-badge&#x60; TXT and   per-port TLSA records as ANS_DNSAID.
 */
public enum DiscoveryProfile {

    ANS_DNSAID("ANS_DNSAID"),

    ANS_TXT("ANS_TXT");

    private String value;

    DiscoveryProfile(String value) {
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
    public static DiscoveryProfile fromValue(String value) {
        for (DiscoveryProfile b : DiscoveryProfile.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

}
