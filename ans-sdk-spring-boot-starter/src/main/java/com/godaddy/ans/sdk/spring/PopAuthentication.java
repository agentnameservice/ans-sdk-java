package com.godaddy.ans.sdk.spring;

import com.godaddy.ans.sdk.pop.CallerIdentity;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;
import java.util.Optional;

public final class PopAuthentication {

    public static final String CALLER_ATTRIBUTE = PopAuthentication.class.getName() + ".caller";

    private PopAuthentication() {
    }

    public static Optional<CallerIdentity> fromRequest(HttpServletRequest request) {
        Objects.requireNonNull(request, "request");
        Object value = request.getAttribute(CALLER_ATTRIBUTE);
        if (value instanceof CallerIdentity identity) {
            return Optional.of(identity);
        }
        return Optional.empty();
    }
}