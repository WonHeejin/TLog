package com.tlog.backend.member.web.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberSignUpRequestTest {

	private static Validator validator;
	
	@BeforeAll
	protected static void init() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}
	
	@Test
	@DisplayName("회원가입 시 빈값 체크")
	protected void signUp_request_test() {
		//given
		String email = " ";
		String password = " ";
		String checkPassword = " ";
		String nickname = " ";
		
		MemberSignUpRequest request = MemberSignUpRequest.builder()
				.test_email(email)
				.test_password(password)
				.test_checkPassword(checkPassword)
				.test_nickname(nickname)
				.build();
		 // when
        Set<ConstraintViolation<MemberSignUpRequest>> violations = 
            validator.validate(request);
        
        //에러메세지
        ConstraintViolation<MemberSignUpRequest> violation_email = violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("email"))
                .findFirst()
                .get();
        ConstraintViolation<MemberSignUpRequest> violation_password = violations.stream()
        		.filter(v -> v.getPropertyPath().toString().equals("password"))
        		.findFirst()
        		.get();
        ConstraintViolation<MemberSignUpRequest> violation_checkPassword = violations.stream()
        		.filter(v -> v.getPropertyPath().toString().equals("checkPassword"))
        		.findFirst()
        		.get();
        ConstraintViolation<MemberSignUpRequest> violation_nickname = violations.stream()
        		.filter(v -> v.getPropertyPath().toString().equals("nickname"))
        		.findFirst()
        		.get();
        
        //then
        assertThat(violation_email.getMessage()).isEqualTo("이메일을 입력해주세요.");
        assertThat(violation_password.getMessage()).isEqualTo("비밀번호를 입력해주세요.");
        assertThat(violation_checkPassword.getMessage()).isEqualTo("비밀번호 재확인을 입력해주세요.");
        assertThat(violation_nickname.getMessage()).isEqualTo("닉네임을 입력해주세요.");
	}
}
