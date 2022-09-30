package com.study.boardExample.exception;

public class NoSearchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSearchException() {
		super();
	}
	
	public NoSearchException(String message) {
		super(message);
	}
	
	public NoSearchException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoSearchException(Throwable cause) {
		super(cause);
	}
	
}