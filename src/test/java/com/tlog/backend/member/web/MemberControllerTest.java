package com.tlog.backend.member.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlog.backend.member.web.dto.MemberSignUpRequest;

//@WebMvcTest(controllers = MemberController.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {
	
	@Autowired
	protected MockMvc mockMvc;
	
	//@Autowired
	ObjectMapper mapper = new ObjectMapper(); 

	//@Autowired
	private WebApplicationContext context;
	
	/*
	 * @BeforeEach void setUp() { mockMvc = MockMvcBuilders
	 * .standaloneSetup(MemberController.class) .alwaysDo(print()) .build() ;
	 * 
	 * }
	 */

	
	@Test
	@DisplayName("회원가입이 정상적으로 완료된다.")
	protected void signUp_test() throws JsonProcessingException, Exception {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		String nickname = "foo";
		
		//가입할 이메일, 비밀번호, 닉네임이 json으로 전달된다.
		//api 호출 시 전달한 json이 MemberSignUpRequest에 담긴다.
		MemberSignUpRequest request = MemberSignUpRequest.builder()
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
			//중복되지 않은 이메일은 다음 단계 수행
		//비밀번호가 제대로 입력됐는지 확인한다.
			//비밀번호와 재입력 비밀번호가 일치하지 않으면 AuthException 발생
			//일치하면 다음단계 수행
		//이메일, 비밀번호, 닉네임 저장
		//비밀번호 암호화 후 업데이트
		//status(created) + 생성된 회원id, 이메일, 닉네임 response
	}
	
	@Test
	@DisplayName("이미 등록된 이메일일 경우 exception 발생")
	protected void invalid_email_test() throws Exception {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		String nickname = "foo";
		
		MemberSignUpRequest request = MemberSignUpRequest.builder()
				.test_email(email)
				.test_password(password)
				.test_checkPassword(password)
				.test_nickname(nickname)
				.build();
		
		String json = mapper.writeValueAsString(request);
		mockMvc.perform(post("/member")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
		
		//when
		ResultActions actions = mockMvc.perform(post("/member")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
		
		//then
		actions.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.code").value("1002"));
		
	}
	
	@Test
	@DisplayName("비밀번호 확인 실패 시 exception 발생")
	protected void test_invalid_password() throws Exception {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		String checkPassword = "2345";
		String nickname = "foo";
		
		MemberSignUpRequest request = MemberSignUpRequest.builder()
				.test_email(email)
				.test_password(password)
				.test_checkPassword(checkPassword)
				.test_nickname(nickname)
				.build();
		
		String json = mapper.writeValueAsString(request);
		
		//when
		ResultActions actions = mockMvc.perform(post("/member")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
		
		//then
		actions.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.code").value("1001"));
	
	}
}
