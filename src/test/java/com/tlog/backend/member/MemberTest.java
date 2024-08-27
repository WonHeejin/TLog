package com.tlog.backend.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.tlog.backend.global.config.JpaConfig;
import com.tlog.backend.global.utils.Encryption;
import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.Role;
import com.tlog.backend.member.domain.Salt;
import com.tlog.backend.member.domain.repository.MemberRepository;
import com.tlog.backend.member.domain.repository.SaltRepository;
import com.tlog.backend.member.web.dto.MemberSignUpRequest;

@ActiveProfiles("test")
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
		Member savedMember = repository.save(member); //member ���

		Salt saltEntity = Salt.builder() //�ش� ����� salt ���
				.member(savedMember)
				.salt(salt)
				.build();
		
		Salt savedSalt = saltRepository.save(saltEntity);

		assertThat(savedMember.getId()).isEqualTo(savedSalt.getMember().getId()); //salt�� �ش� ����� �°� ����ƴ��� Ȯ��
		assertThat(savedMember.getPassword()).isEqualTo(encPass); //��ȣȭ�� ��й�ȣ Ȯ��
		assertThat(encryption.sha256encode(password, savedSalt.getSalt())).isEqualTo(savedMember.getPassword()); //����� salt+�Է��� ��й�ȣ = ����� ��ȣȭ ��й�ȣ
		assertThat(savedMember.getCreatedDate()).isNotNull();
	}
	
	@Test
	@DisplayName("Member ��ƼƼ �ȿ� encPassword �׽�Ʈ")
	public void test_member_encpass() {
		//given
		String email = "abc@googole.com";
		String password = "1234";
		String nickname = "foo";
		MemberSignUpRequest req = MemberSignUpRequest
				.builder()
				.test_email(email)
				.test_nickname(nickname)
				.test_password(password)
				.test_checkPassword(password)
				.build();
		
		//when
		Member entity = Member.builder()
				.email(req.getEmail())
				.password(req.getPassword())
				.nickname(req.getNickname())
				.role(Role.GUEST)
				.build();
		Member savedMember = repository.save(entity);
		
		String salt = savedMember.encPassword();
		Salt saltEntity = Salt.builder()
				.member(savedMember)
				.salt(salt)
				.build();
		Salt savedSalt = saltRepository.save(saltEntity);
		
		//then
		assertThat(savedMember.getId()).isEqualTo(savedSalt.getMember().getId()); //salt�� �ش� ����� �°� ����ƴ��� Ȯ��
		//��й�ȣ ��ȣȭ �ƴ��� Ȯ��
		assertThat(savedMember.getPassword()).isNotNull();
		assertThat(savedMember.getPassword()).isNotEqualTo(password);
		System.out.println(savedMember.getPassword());
		
	}
	
	
}
