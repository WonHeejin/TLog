package com.tlog.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
	
	INVALID_REQUEST(1000, "�߸��� ��û�Դϴ�."),
	
	INVALID_PASSWORD(1001, "��й�ȣ�� ��ġ���� �ʽ��ϴ�."),
	INVALID_EMAIL(1002, "�̹� ��ϵ� �̸����Դϴ�."),
	EMAIL_NOT_FOUND(1002, "�������� �ʴ� �̸����Դϴ�."),
	
	
	INVALID_ACCESS_TOKEN(2001, "��ȿ���� ���� ��ū�Դϴ�."),
	EXPIRED_PERIOD_ACCESS_TOKEN(2002, "��ū�� ����Ǿ����ϴ�."),
	INVALID_REFRESH_TOKEN(2003, "��ȿ���� ���� ��ū�Դϴ�."),
	EXPIRED_PERIOD_REFRESH_TOKEN(2004, "�������� ��ū�� ����Ǿ����ϴ�. �ٽ� �α������ּ���."),
	NOT_MATCHING_TOKEN(2005, "��ȿ���� ���� ��ū�Դϴ�.");
	private final int code;
	private final String message;
}
