package com.tlog.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
	
	INVALID_REQUEST(1000, "잘못된 요청입니다."),
	
	INVALID_PASSWORD(1001, "비밀번호가 일치하지 않습니다."),
	INVALID_EMAIL(1002, "이미 등록된 이메일입니다."),
	EMAIL_NOT_FOUND(1002, "존재하지 않는 이메일입니다."),
	
	
	INVALID_ACCESS_TOKEN(2001, "유효하지 않은 토큰입니다."),
	EXPIRED_PERIOD_ACCESS_TOKEN(2002, "토큰이 만료되었습니다."),
	INVALID_REFRESH_TOKEN(2003, "유효하지 않은 토큰입니다."),
	EXPIRED_PERIOD_REFRESH_TOKEN(2004, "리프레시 토큰이 만료되었습니다. 다시 로그인해주세요."),
	NOT_MATCHING_TOKEN(2005, "유효하지 않은 토큰입니다.");
	private final int code;
	private final String message;
}
