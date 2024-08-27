package com.tlog.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
	
	INVALID_REQUEST(1000, "�߸��� ��û�Դϴ�."),
	
	INVALID_PASSWORD(1001, "��й�ȣ�� ��ġ���� �ʽ��ϴ�."),
	INVALID_EMAIL(1002, "�̹� ��ϵ� �̸����Դϴ�.");
	
	private final int code;
	private final String message;
}
