package com.tlog.backend.login.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberToken {

	private final String accessToken;
	private final String refreshToken;
}
