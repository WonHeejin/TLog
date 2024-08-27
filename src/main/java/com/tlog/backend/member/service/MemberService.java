package com.tlog.backend.member.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.tlog.backend.global.exception.AuthException;
import com.tlog.backend.global.exception.ExceptionCode;
import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.Role;
import com.tlog.backend.member.domain.Salt;
import com.tlog.backend.member.domain.repository.MemberRepository;
import com.tlog.backend.member.domain.repository.SaltRepository;
import com.tlog.backend.member.web.dto.MemberSignUpRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	
	private final MemberRepository repository;
	private final SaltRepository saltRepository;
	
	@Transactional
	public Member signUp(MemberSignUpRequest request) {
		//�̸��� �ߺ� üũ
		if(repository.findByEmail(request.getEmail()).isPresent()) {
			throw new AuthException(ExceptionCode.INVALID_EMAIL);
		}
		
		//��й�ȣ üũ
		if(!request.getPassword().equals(request.getCheckPassword())) {
			throw new AuthException(ExceptionCode.INVALID_PASSWORD);
		}
		
		//ȸ�� ���� ����
		Member entity = Member.builder()
				.email(request.getEmail())
				.password(request.getPassword())
				.nickname(request.getNickname())
				.role(Role.GUEST)
				.build();
		
		Member savedMember = repository.save(entity);
		//��й�ȣ ��ȣȭ
		String salt = savedMember.encPassword();
		//��ȣȭ ���� ����
		Salt saltEntity = Salt.builder()
				.member(savedMember)
				.salt(salt)
				.build();
		saltRepository.save(saltEntity);
		
		return savedMember;
	}
}
