package com.tlog.backend.login.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tlog.backend.login.domain.MemberToken;
import com.tlog.backend.login.service.LoginService;
import com.tlog.backend.login.web.dto.LoginRequest;
import com.tlog.backend.login.web.dto.LoginResponse;
import com.tlog.backend.member.web.dto.MemberSignUpResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class LoginController {

	private final LoginService loginService;
	
	private final Long COOKIE_MAX_AGE = 604800000L;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
		
		MemberToken tokens = loginService.login(request);
		
		 final ResponseCookie cookie = ResponseCookie.from("refresh-token", tokens.getRefreshToken())
                .maxAge(COOKIE_MAX_AGE)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
		 
		response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(tokens.getAccessToken()));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@CookieValue("refresh-token") String refreshToken) {
		//DB에 저장된 리프레시토큰 삭제
		loginService.deleteRefreshToken(refreshToken);
		//쿠키에 담았던 리프레시토큰 삭제
		final ResponseCookie cookie = ResponseCookie.from("refresh-token", null)
                .maxAge(COOKIE_MAX_AGE)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
		
		return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
	}
	
	@PostMapping("/token/{memberId}")
	public ResponseEntity<LoginResponse> extendLogin(
			@CookieValue("refresh-token") String refreshToken,
			@RequestHeader("Authorization") String jwtHeader,
			@PathVariable Long memberId) {
		
		MemberToken tokens = loginService.regenerateToken(memberId, refreshToken, jwtHeader);
		return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(tokens.getAccessToken()));
		
	}
}
