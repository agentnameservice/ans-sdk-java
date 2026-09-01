/**
 * Sender-constrained, application-layer caller authentication for ANS
 * agent-to-agent (A2A) traffic — proof-of-possession without mutual TLS.
 *
 * <h2>Why</h2>
 * Today an ANS caller proves its identity to a callee with an mTLS client
 * certificate. mTLS breaks through L7 proxies and gateways (which terminate TLS
 * and drop the client identity), carries no delegation semantics, and is
 * operationally heavy. This package moves the caller's proof to the application
 * layer as a DPoP proof (RFC 9449) — the RFC-stable form of the IETF WIMSE
 * Workload Proof Token. The proof travels in a standard {@code DPoP} HTTP header
 * over ordinary server-authenticated HTTPS. The handshake presents no client
 * certificate.
 *
 * <h2>The three-proof model</h2>
 * A2A caller authentication is three independent proofs, all bound to one
 * identity certificate:
 * <ul>
 *   <li><b>Identity</b> — the caller's name and identity certificate are in the
 *       transparency log. The SCITT receipt supplies this.</li>
 *   <li><b>Liveness</b> — that certificate is currently valid (ACTIVE, not
 *       revoked). The status token's valid identity fingerprints supply this.</li>
 *   <li><b>Possession</b> — the caller holds the certificate's private key, for
 *       THIS request. The DPoP proof in this package supplies this. This is the
 *       proof that replaces the mTLS handshake.</li>
 * </ul>
 * This package composes with SCITT. It does not replace it. The receipt and
 * status token are verified unchanged. This package adds the possession proof
 * and binds all three to the same certificate.
 *
 * <h2>Binding</h2>
 * The proof header carries both the bare public key (jwk, required by RFC 9449
 * §4.2) and the caller's identity certificate (x5c, RFC 7515 §4.1.6) — which
 * MUST present the same key. A verifier (a) confirms that equality and verifies
 * the JWS under the single key, (b) confirms SHA-256(cert) is among the status
 * token's valid identity fingerprints, and (c) confirms the certificate's own
 * {@code ans://} URI SAN equals the status token's ANS name. To pass, a caller
 * must hold the private key for a certificate its own transparency-log-signed
 * status token vouches for. The status token (TL-signed) is the trust
 * statement — there is no CA-chain validation, and the certificate's own
 * validity dates, key usage, and cert-type entry are deliberately not consulted.
 * A captured receipt and status token (both public) are useless without the key,
 * and the proof's jti/htm/htu/iat defeat replay and redirection.
 *
 * <p>The htu binding is only as trustworthy as the URL the callee compares
 * against. The callee MUST derive that URL from its own externally-visible
 * origin, not from a client-controlled Host header, so a proof captured from a
 * call to another origin cannot be replayed here with a spoofed Host. Note also
 * that htu excludes the query string (RFC 9449 §4.2), so a proof does not bind
 * request parameters.
 *
 * <h2>RFC 9449 conformance and OAuth 2.0</h2>
 * Proofs are wire-conformant RFC 9449 DPoP: a textbook §4.3 verifier validates
 * them via the jwk header and ignores the x5c. The profile adds two restrictions
 * the RFC permits a deployment to impose: ES256 only, and no JOSE header
 * parameters beyond {@code {typ, alg, jwk, x5c}} (strict decoding, so a
 * private-key "d" member or any extra field fails closed).
 *
 * <p>OAuth 2.0 composes on top, unchanged from the RFC. When a request presents
 * a DPoP-bound access token ({@code Authorization: DPoP <token>}, RFC 9449 §7.1),
 * the proof binds it via the ath claim. The rule is strict in both directions:
 * ath is present exactly when a token is presented. Without OAuth there is no
 * access token and no ath — the SCITT receipt and status token are the
 * credential, and the proof's absence of ath is itself RFC-conformant (ath is
 * required only when a token is presented).
 *
 * <h2>Authentication is not authorization</h2>
 * {@link com.godaddy.ans.sdk.pop.CallerVerifier} AUTHENTICATES the caller — it
 * returns the cryptographically proven identity. It does NOT authorize it. A
 * returned {@link com.godaddy.ans.sdk.pop.CallerIdentity} means "this request
 * genuinely came from ans://…X", never "X is allowed to do this." The callee
 * MUST apply its own authorization to the returned identity. Use
 * {@link com.godaddy.ans.sdk.pop.CallerOptions#withExpectedPeer(String)} to pin
 * a specific peer when the callee only accepts a known caller.
 *
 * <h2>What dropping mTLS gives up</h2>
 * DPoP provides sender-constraint (possession), but not the channel binding,
 * mutual endpoint authentication, or credential confidentiality that mTLS
 * provided. The channel is still server-authenticated HTTPS. A caller induced to
 * connect to a hostile callee discloses its (public) receipt and status token
 * and a single-use, htu-bound proof. Deployments that need channel binding or
 * mutual endpoint auth keep mTLS or add token binding.
 *
 * <h2>Scope</h2>
 * This package implements the autonomous A2A model (no Authorization Server):
 * the callee verifies the three proofs and authorizes locally. It does NOT
 * implement delegation (an agent acting on behalf of a user across a call
 * chain) — that is a separate, higher-risk concern. It reuses the caller's
 * existing identity certificate and the status token, minting no new credential
 * and adding no wire format.
 */
package com.godaddy.ans.sdk.pop;