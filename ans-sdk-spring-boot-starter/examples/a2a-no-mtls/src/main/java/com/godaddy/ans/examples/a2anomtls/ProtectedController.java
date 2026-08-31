package com.godaddy.ans.examples.a2anomtls;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import com.godaddy.ans.sdk.spring.PopAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/a2a")
public class ProtectedController {

    @GetMapping("/whoami")
    public ResponseEntity<Map<String, String>> whoami(HttpServletRequest request) {
        Optional<CallerIdentity> caller = PopAuthentication.fromRequest(request);
        if (caller.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        CallerIdentity identity = caller.get();
        return ResponseEntity.ok(Map.of(
            "ansName", identity.ansName(),
            "agentId", identity.agentId(),
            "fingerprint", identity.fingerprintHex(),
            "jkt", identity.jkt()
        ));
    }
}