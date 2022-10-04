package com.study.boardExample.exception;

import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

public class JwtInvalidException extends AuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    public JwtInvalidException(String message) {
        super(message);
    }
    public JwtInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
