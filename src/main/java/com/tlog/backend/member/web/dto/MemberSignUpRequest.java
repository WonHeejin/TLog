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
	@NotBlank(message = "�̸����� �Է����ּ���.")
	private String email;
	@NotBlank(message = "��й�ȣ�� �Է����ּ���.")
	private String password;
	@NotBlank(message = "��й�ȣ ��Ȯ���� �Է����ּ���.")
	private String checkPassword;
	@NotBlank(message = "�г����� �Է����ּ���.")
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
