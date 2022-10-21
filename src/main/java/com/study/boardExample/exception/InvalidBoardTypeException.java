package com.study.boardExample.exception;

import java.io.Serial;

public class InvalidBoardTypeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidBoardTypeException(String message) {
        super(message);
    }
}
