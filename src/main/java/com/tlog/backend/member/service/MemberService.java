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
		//이메일 중복 체크
		if(repository.findByEmail(request.getEmail()).isPresent()) {
			throw new AuthException(ExceptionCode.INVALID_EMAIL);
		}
		
		//비밀번호 체크
		if(!request.getPassword().equals(request.getCheckPassword())) {
			throw new AuthException(ExceptionCode.INVALID_PASSWORD);
		}
		
		//회원 정보 저장
		Member entity = Member.builder()
				.email(request.getEmail())
				.password(request.getPassword())
				.nickname(request.getNickname())
				.role(Role.GUEST)
				.build();
		
		Member savedMember = repository.save(entity);
		//비밀번호 암호화
		String salt = savedMember.encPassword();
		//암호화 정보 저장
		Salt saltEntity = Salt.builder()
				.member(savedMember)
				.salt(salt)
				.build();
		saltRepository.save(saltEntity);
		
		return savedMember;
	}
}
