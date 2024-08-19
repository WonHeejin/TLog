package com.tlog.backend.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.tlog.backend.global.config.JpaConfig;
import com.tlog.backend.global.utils.Encryption;
import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.Role;
import com.tlog.backend.member.domain.Salt;
import com.tlog.backend.member.domain.repository.MemberRepository;
import com.tlog.backend.member.domain.repository.SaltRepository;

@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberTest {

	@Autowired
	private MemberRepository repository;
	
	@Autowired
	private SaltRepository saltRepository;
	
	private Encryption encryption = new Encryption();
	
	@Test
	@DisplayName("ȸ������ �� salt�� ���� Ȯ��")
	void test_member() {
		String email = "abc@googole.com";
		String password = "1234";
		String nickname = "foo";
		String salt = encryption.getSalt();
		String encPass = encryption.sha256encode(password, salt);
		
		Member member = Member.builder()
				.email(email)
				.password(encPass)
				.nickname(nickname)
				.role(Role.GUEST)
				.build();
		Member savedUser = repository.save(member); //member ���

		Salt saltEntity = Salt.builder() //�ش� ����� salt ���
				.member(savedUser)
				.salt(salt)
				.build();
		
		Salt savedSalt = saltRepository.save(saltEntity);

		assertThat(savedUser.getId()).isEqualTo(savedSalt.getMember().getId()); //salt�� �ش� ����� �°� ����ƴ��� Ȯ��
		assertThat(savedUser.getPassword()).isEqualTo(encPass); //��ȣȭ�� ��й�ȣ Ȯ��
		assertThat(encryption.sha256encode(password, savedSalt.getSalt())).isEqualTo(savedUser.getPassword()); //����� salt+�Է��� ��й�ȣ = ����� ��ȣȭ ��й�ȣ
		assertThat(savedUser.getCreatedDate()).isNotNull();
	}
}
