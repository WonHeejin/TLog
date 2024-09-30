package com.tlog.backend.login.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginRequest {
	
	private String email;
	private String password;
	
	@Builder(builderMethodName = "testBuilder")
	public LoginRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
