package com.tlog.backend.member.web.dto;

import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.Role;

import lombok.Getter;

@Getter
public class MemberSignUpResponse {
	
	private Long id;
	private String email;
	private String nickname;
	private Role roll;
	
	public MemberSignUpResponse(Member entity) {
		this.id = entity.getId();
		this.email = entity.getEmail();
		this.nickname = entity.getNickname();
		this.roll = entity.getRole();
	}
}
