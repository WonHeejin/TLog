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
	@DisplayName("ȸ�������� ���������� �Ϸ�ȴ�.")
	protected void signUp_test() throws JsonProcessingException, Exception {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		String nickname = "foo";
		
		//������ �̸���, ��й�ȣ, �г����� json���� ���޵ȴ�.
		//api ȣ�� �� ������ json�� MemberSignUpRequest�� ����.
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
			//�ߺ����� ���� �̸����� ���� �ܰ� ����
		//��й�ȣ�� ����� �Էµƴ��� Ȯ���Ѵ�.
			//��й�ȣ�� ���Է� ��й�ȣ�� ��ġ���� ������ AuthException �߻�
			//��ġ�ϸ� �����ܰ� ����
		//�̸���, ��й�ȣ, �г��� ����
		//��й�ȣ ��ȣȭ �� ������Ʈ
		//status(created) + ������ ȸ��id, �̸���, �г��� response
	}
	
	@Test
	@DisplayName("�̹� ��ϵ� �̸����� ��� exception �߻�")
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
	@DisplayName("��й�ȣ Ȯ�� ���� �� exception �߻�")
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
