# A2A no-mTLS Example

This example shows sender-constrained caller authentication for ANS agent-to-agent (A2A) traffic without mutual TLS. The client proves that it holds its private key with a DPoP proof (RFC 9449). It sends the proof over a normal server-authenticated HTTPS connection. The server verifies the proof and the ANS identity with the `PopAuthenticationFilter`.

DPoP works together with SCITT. SCITT gives identity and liveness. The DPoP proof binds the request to the key that the caller holds. DPoP does not replace SCITT.

## What it shows

- **Client attach** (`PopClientExample`) — makes a DPoP proof with `PopSigner`. It attaches the proof and the SCITT identity headers to an outbound request with `PopHttp.attachIdentity`.
- **Server filter** (`PopSecurityConfig` + `ProtectedController`) — registers `PopAuthenticationFilter` to verify the caller. It then reads the resolved `CallerIdentity` with `PopAuthentication.fromRequest`.

## Prerequisites

- A PKCS12 keystore that holds the caller EC P-256 private key and its leaf certificate. The certificate must carry an `ans://` SAN.
- The agent must be registered in the ANS transparency log. The server fetches a receipt and a status token for the agent.
- Java 17 or later.

## Run the server

The server listens on port 8443 and protects `/a2a/*`. Set the trusted host to the public authority that clients use to reach the server. This pins the source of the `htu` binding.

```bash
export POP_TRUSTED_HOST=server.example.com:8443
./gradlew :ans-sdk-spring-boot-starter:examples:a2a-no-mtls:bootRun
```

The configuration is in `application.yml`:

- `pop.expected-issuer` — the transparency log domain that issues the status token.
- `pop.trusted-host` — the authority that the filter trusts for the request URL.

## Run the client

```bash
./gradlew :ans-sdk-spring-boot-starter:examples:a2a-no-mtls:runClient \
  --args="https://server.example.com:8443/a2a/whoami client.p12 changeit agent-key my-agent-id"
```

The arguments, in order:

1. `serverUrl` — the full URL of the protected endpoint.
2. `keystorePath` — the path to the PKCS12 keystore.
3. `keystorePassword` — the keystore password.
4. `keyAlias` — the alias of the key entry in the keystore.
5. `agentId` — the agent ID that fetches the SCITT receipt and status token.

On success the server returns the caller identity:

```json
{
  "ansName": "ans://my-agent.example.com",
  "agentId": "my-agent-id",
  "fingerprint": "…",
  "jkt": "…"
}
```

## Notes

- The channel is server-authenticated HTTPS. The client does not present a certificate in the TLS handshake.
- The filter fails closed. A missing, duplicate, or invalid header returns `401`.
- This module is an example. Coverage checks do not include it.