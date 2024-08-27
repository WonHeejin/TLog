package com.tlog.backend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	
	GUEST("ROLE_GUEST", "������ȸ��"),
	MEMBER("ROLE_MEMBER", "����ȸ��");
	
	private final String key;
	private final String value;
	
}
