package com.tlog.backend.member.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tlog.backend.member.service.MemberService;
import com.tlog.backend.member.web.dto.MemberSignUpRequest;
import com.tlog.backend.member.web.dto.MemberSignUpResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MemberController {
	
	private final MemberService memberService;
	
	@PostMapping("/member")
	public ResponseEntity<MemberSignUpResponse> signUp(@Validated @RequestBody MemberSignUpRequest request) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new MemberSignUpResponse(memberService.signUp(request)));
	}
}
