package com.tlog.backend.member.web.dto;

import javax.validation.constraints.NotBlank;

import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.Role;

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
	
	@Builder
	public MemberSignUpRequest(String email, String password, String checkPassword, String nickname) {
		this.email = email;
		this.password = password;
		this.checkPassword = checkPassword;
		this.nickname = nickname;
	}
	
	public Member toEntity() {
		return Member.builder()
				.email(email)
				.nickname(nickname)
				.password(password)
				.role(Role.GUEST)
				.build();
	}
}
