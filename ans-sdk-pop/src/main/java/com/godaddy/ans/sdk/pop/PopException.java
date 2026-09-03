package com.godaddy.ans.sdk.pop;

import java.util.Objects;

public class PopException extends Exception {

    private static final long serialVersionUID = 1L;

    private final ErrorType category;

    public PopException(ErrorType category, String message) {
        super(message);
        this.category = Objects.requireNonNull(category, "category");
    }

    public PopException(ErrorType category, String message, Throwable cause) {
        super(message, cause);
        this.category = Objects.requireNonNull(category, "category");
    }

    public ErrorType category() {
        return category;
    }
}
