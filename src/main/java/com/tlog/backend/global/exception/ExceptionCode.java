package com.tlog.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
	
	INVALID_REQUEST(1000, "잘못된 요청입니다."),
	
	INVALID_PASSWORD(1001, "비밀번호가 일치하지 않습니다."),
	INVALID_EMAIL(1002, "이미 등록된 이메일입니다.");
	
	private final int code;
	private final String message;
}
