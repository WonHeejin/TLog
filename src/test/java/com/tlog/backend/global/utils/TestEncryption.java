package com.tlog.backend.global.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestEncryption {

	String origin = "1234";
	String sha256 = "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4";
	
	Encryption enc = new Encryption();
	
	@Test
	@DisplayName("sha256 ��ȣȭ ���� Ȯ��")
	void test_sha256() {
		String encData = enc.sha256encode(origin);
		
		assertThat(encData).isEqualTo(sha256).as("sha256 ��ȣȭ ���� �Ϸ�");
	}

	@Test
	@DisplayName("sha256+salt ��ȣȭ ���� Ȯ��")
	void test_sha256WithSalt() {
		String salt = enc.getSalt(); 
		String encData1 = enc.sha256encode(origin, salt);
		assertThat(encData1).isNotEqualTo(sha256).as("sha256+salt ��ȣȭ ���� �Ϸ�");
		
	}
	
	@Test
	@DisplayName("��ȣȭ �� ��й�ȣ+salt == db�� ����� ��ȣȭ ��й�ȣ")
	void test_saltLogin() {
		String salt = enc.getSalt(); 
		String dbPw = enc.sha256encode(origin, salt);
		
		//�α��� �� ����Ǿ��ִ� encData�� ���޹��� password+salt �������� Ȯ��
		String loginPw = enc.sha256encode(origin, salt);
		assertThat(loginPw).isEqualTo(dbPw).as("sha256+salt �α��� ����");
	}
}
