package com.godaddy.ans.examples.a2anomtls;

import com.godaddy.ans.sdk.pop.PopHttp;
import com.godaddy.ans.sdk.pop.PopSigner;
import com.godaddy.ans.sdk.transparency.TransparencyClient;
import com.godaddy.ans.sdk.transparency.scitt.ScittHeaders;

import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public final class PopClientExample {

    private PopClientExample() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 5) {
            System.out.println("Usage: runClient <serverUrl> <keystorePath> <keystorePassword> "
                + "<keyAlias> <agentId>");
            System.out.println("Example: runClient https://server.example.com:8443/a2a/whoami "
                + "client.p12 changeit agent-key my-agent-id");
            System.exit(1);
        }

        String serverUrl = args[0];
        String keystorePath = args[1];
        String keystorePassword = args[2];
        String keyAlias = args[3];
        String agentId = args[4];

        System.out.println("===========================================");
        System.out.println("ANS SDK - A2A no-mTLS Client (DPoP over server-auth HTTPS)");
        System.out.println("===========================================");
        System.out.println("Target: " + serverUrl);

        PopSigner signer = loadSigner(keystorePath, keystorePassword, keyAlias);
        System.out.println("DPoP signer ready. jkt=" + signer.jkt());

        Map<String, List<String>> scittHeaders = fetchScittHeaders(agentId);
        System.out.println("SCITT headers fetched for agent " + agentId);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
            .uri(URI.create(serverUrl))
            .GET();

        PopHttp.attachIdentity(builder, signer, scittHeaders, null);
        System.out.println("Attached DPoP proof and SCITT identity headers");

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());

        System.out.println("Response status: " + response.statusCode());
        System.out.println("Response body: " + response.body());
    }

    private static PopSigner loadSigner(String keystorePath, String keystorePassword, String keyAlias)
            throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream in = new FileInputStream(keystorePath)) {
            keyStore.load(in, keystorePassword.toCharArray());
        }
        ECPrivateKey privateKey = (ECPrivateKey) keyStore.getKey(keyAlias, keystorePassword.toCharArray());
        X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
        return PopSigner.create(privateKey, cert.getEncoded());
    }

    private static Map<String, List<String>> fetchScittHeaders(String agentId) {
        try (TransparencyClient transparency = TransparencyClient.createOte()) {
            String receipt = Base64.getEncoder().encodeToString(transparency.getReceipt(agentId));
            String statusToken = Base64.getEncoder().encodeToString(transparency.getStatusToken(agentId));
            return Map.of(
                ScittHeaders.SCITT_RECEIPT_HEADER, List.of(receipt),
                ScittHeaders.STATUS_TOKEN_HEADER, List.of(statusToken));
        }
    }
}