package com.tlog.backend.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ExceptionResponse> handleAuthException(AuthException e) {
		log.warn(e.getMessage(), e);
		return ResponseEntity.badRequest().body(new ExceptionResponse(e.getCode(), e.getMessage()));
	}
}
