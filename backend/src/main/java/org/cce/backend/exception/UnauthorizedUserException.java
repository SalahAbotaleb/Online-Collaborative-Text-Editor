package org.cce.backend.exception;

public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException(String msg) {
        super(msg);
    }
}
