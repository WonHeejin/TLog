package com.tlog.backend.global.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
	
	private final int code;
	private final String message;
	
	public AuthException(final ExceptionCode code) {
		this.code = code.getCode();
		this.message = code.getMessage();
	}
	
}
