package com.tlog.backend.login.service;

import org.springframework.stereotype.Service;

import com.tlog.backend.global.exception.AuthException;
import com.tlog.backend.global.exception.ExceptionCode;
import com.tlog.backend.global.utils.Encryption;
import com.tlog.backend.login.JwtProvider;
import com.tlog.backend.login.domain.MemberToken;
import com.tlog.backend.login.domain.RefreshToken;
import com.tlog.backend.login.domain.RefreshTokenRepository;
import com.tlog.backend.login.web.dto.LoginRequest;
import com.tlog.backend.member.domain.Member;
import com.tlog.backend.member.domain.repository.MemberRepository;
import com.tlog.backend.member.domain.repository.SaltRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class LoginService {

	private final MemberRepository memberRepository;
	private final SaltRepository saltRepository;
	private final JwtProvider jwtProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public MemberToken login(LoginRequest request) {
		Encryption encryption = new Encryption();
		String encoded = null;
		
		//이메일 존재하는지 확인
		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new AuthException(ExceptionCode.EMAIL_NOT_FOUND));
		
		//비밀번호 일치하는지 확인
		encoded = encryption.sha256encode(request.getPassword(), 
				saltRepository.findByMemberId(member.getId()).get().getSalt());
		if(!member.getPassword().equals(encoded)) {
			throw new AuthException(ExceptionCode.INVALID_PASSWORD);
		}

		//토큰 발행
		MemberToken tokens = jwtProvider.generateLoginToken(member.getId());
		
		//리프레시 토큰 저장
		RefreshToken refreshToken = new RefreshToken(tokens.getRefreshToken(), member.getId());
		refreshTokenRepository.save(refreshToken);
		
		//토큰 전달
		return tokens;
	}
	
	public void deleteRefreshToken(String refreshToken) {
		//토큰 유효성 검증
		jwtProvider.validateRefreshTokens(refreshToken);
		//회원 id 추출
		Long memberId = jwtProvider.getMemberId(refreshToken);
		//entity 생성
		RefreshToken entity = new RefreshToken(refreshToken, memberId);
		//토큰 삭제
		refreshTokenRepository.delete(entity);
	}
	
	public MemberToken regenerateToken(Long memberId, String refreshToken) {
		String dbRefreshToken, newAccessToken;
		//refresh token 유효성 검증
		jwtProvider.validateRefreshTokens(refreshToken);
		
		//refresh token db 조회 -> null이거나 일치하지 않으면 not matching token(유효하지 않은 토큰) exception
		dbRefreshToken = refreshTokenRepository.findByMemberId(memberId).orElseThrow(() -> new AuthException(ExceptionCode.NOT_MATCHING_TOKEN)).getToken();
		if(!refreshToken.equals(dbRefreshToken)) {
			throw new AuthException(ExceptionCode.NOT_MATCHING_TOKEN);
		}
		
		//access token 재발급
		newAccessToken = jwtProvider.regenerateAccessToken(memberId);
		
		return new MemberToken(newAccessToken, refreshToken);
	}
	
	

}
