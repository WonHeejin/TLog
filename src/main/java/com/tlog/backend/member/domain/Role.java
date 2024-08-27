package com.tlog.backend.member.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	
	GUEST("ROLE_GUEST", "미인증회원"),
	MEMBER("ROLE_MEMBER", "인증회원");
	
	private final String key;
	private final String value;
	
}
