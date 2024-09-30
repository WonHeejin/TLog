package com.tlog.backend.global.exception;

import lombok.Getter;

@Getter
public class ExpiredPeriodJwtException extends AuthException{

	public ExpiredPeriodJwtException(ExceptionCode exceptionCode) {
		super(exceptionCode);
	}
}
