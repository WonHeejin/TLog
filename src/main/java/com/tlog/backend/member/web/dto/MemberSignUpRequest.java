package com.tlog.backend.member.web.dto;

import javax.validation.constraints.NotBlank;

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
	
	@Builder(builderMethodName = "testDataBuilder") //�׽�Ʈ ������ ������ ����
	public MemberSignUpRequest(String test_email, String test_password, String test_checkPassword, String test_nickname) {
		this.email = test_email;
		this.password = test_password;
		this.checkPassword = test_checkPassword;
		this.nickname = test_nickname;
	}
	
}
