package com.tlog.backend.login;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlog.backend.login.web.dto.LoginRequest;
import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.Role;
import com.tlog.backend.member.domain.repository.MemberRepository;
import com.tlog.backend.member.web.dto.MemberSignUpRequest;

@TestInstance(Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@SpringBootTest
public class LoginControllerTest {

	@Autowired
	protected MockMvc mockMvc;
	
	ObjectMapper mapper = new ObjectMapper(); 

	@Autowired
	MemberRepository memberRepository;
	
	@BeforeAll
	void signUp_for_test_data() throws Exception {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		String nickname = "foo";
		
		//가입할 이메일, 비밀번호, 닉네임이 json으로 전달된다.
		//api 호출 시 전달한 json이 MemberSignUpRequest에 담긴다.
		MemberSignUpRequest request = MemberSignUpRequest.testDataBuilder()
				.test_email(email)
				.test_password(password)
				.test_checkPassword(password)
				.test_nickname(nickname)
				.build();
		
		String json = mapper.writeValueAsString(request);
		System.out.println(json);
		
		mockMvc.perform(post("/member")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isCreated())
		.andDo(print());
		
	}
	
	@Test
	@DisplayName("로그인이 정상적으로 완료되어 토큰이 반환된다.")
	void login_test() throws Exception {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		
		LoginRequest request = LoginRequest.testBuilder()
				.email(email)
				.password(password)
				.build();
		
		String jsonReq = mapper.writeValueAsString(request);
		
		mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonReq))
		.andExpect(status().isCreated())
		.andDo(print());
	}
}
