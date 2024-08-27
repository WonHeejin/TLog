package com.tlog.backend.member.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberSignUpRequest {
	@NotBlank(message = "이메일을 입력해주세요.")
	private String email;
	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;
	@NotBlank(message = "비밀번호 재확인을 입력해주세요.")
	private String checkPassword;
	@NotBlank(message = "닉네임을 입력해주세요.")
	private String nickname;
	
	@Builder(builderMethodName = "testDataBuilder") //테스트 데이터 생성용 빌더
	public MemberSignUpRequest(String test_email, String test_password, String test_checkPassword, String test_nickname) {
		this.email = test_email;
		this.password = test_password;
		this.checkPassword = test_checkPassword;
		this.nickname = test_nickname;
	}
	
}
