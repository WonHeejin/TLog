package com.tlog.backend.global.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestEncryption {

	String origin = "1234";
	String sha256 = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";
	
	Encryption enc = new Encryption();
	
	@Test
	@DisplayName("sha256 암호화 적용 확인")
	void test_sha256() {
		String encData = enc.sha256encode(origin);
		
		assertThat(encData).isEqualTo(sha256).as("sha256 암호화 적용 완료");
	}

	@Test
	@DisplayName("sha256+salt 암호화 적용 확인")
	void test_sha256WithSalt() {
		String salt = enc.getSalt(); 
		String encData1 = enc.sha256encode(origin, salt);
		assertThat(encData1).isNotEqualTo(sha256).as("sha256+salt 암호화 적용 완료");
		
	}
	
	@Test
	@DisplayName("암호화 전 비밀번호+salt == db에 저장된 암호화 비밀번호")
	void test_saltLogin() {
		String salt = enc.getSalt(); 
		String dbPw = enc.sha256encode(origin, salt);
		
		//로그인 시 저장되어있는 encData와 전달받은 password+salt 동일한지 확인
		String loginPw = enc.sha256encode(origin, salt);
		assertThat(loginPw).isEqualTo(dbPw).as("sha256+salt 로그인 성공");
	}
}
