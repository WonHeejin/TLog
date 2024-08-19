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
	@DisplayName("회원가입 후 salt값 저장 확인")
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
		Member savedUser = repository.save(member); //member 등록

		Salt saltEntity = Salt.builder() //해당 멤버의 salt 등록
				.member(savedUser)
				.salt(salt)
				.build();
		
		Salt savedSalt = saltRepository.save(saltEntity);

		assertThat(savedUser.getId()).isEqualTo(savedSalt.getMember().getId()); //salt가 해당 멤버에 맞게 저장됐는지 확인
		assertThat(savedUser.getPassword()).isEqualTo(encPass); //암호화된 비밀번호 확인
		assertThat(encryption.sha256encode(password, savedSalt.getSalt())).isEqualTo(savedUser.getPassword()); //저장된 salt+입력한 비밀번호 = 저장된 암호화 비밀번호
		assertThat(savedUser.getCreatedDate()).isNotNull();
	}
}
