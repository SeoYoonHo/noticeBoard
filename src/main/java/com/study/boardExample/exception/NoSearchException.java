package com.study.boardExample.exception;

import java.io.Serial;

public class NoSearchException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public NoSearchException(String message) {
        super(message);
    }

}