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
		
		//�̸��� �����ϴ��� Ȯ��
		Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new AuthException(ExceptionCode.EMAIL_NOT_FOUND));
		
		//��й�ȣ ��ġ�ϴ��� Ȯ��
		encoded = encryption.sha256encode(request.getPassword(), 
				saltRepository.findByMemberId(member.getId()).get().getSalt());
		if(!member.getPassword().equals(encoded)) {
			throw new AuthException(ExceptionCode.INVALID_PASSWORD);
		}

		//��ū ����
		MemberToken tokens = jwtProvider.generateLoginToken(member.getId());
		
		//�������� ��ū ����
		RefreshToken refreshToken = new RefreshToken(tokens.getRefreshToken(), member.getId());
		refreshTokenRepository.save(refreshToken);
		
		//��ū ����
		return tokens;
	}
	
	public void deleteRefreshToken(String refreshToken) {
		//��ū ��ȿ�� ����
		jwtProvider.validateRefreshTokens(refreshToken);
		//ȸ�� id ����
		Long memberId = jwtProvider.getMemberId(refreshToken);
		//entity ����
		RefreshToken entity = new RefreshToken(refreshToken, memberId);
		//��ū ����
		refreshTokenRepository.delete(entity);
	}
	
	public MemberToken regenerateToken(Long memberId, String refreshToken) {
		String dbRefreshToken, newAccessToken;
		//refresh token ��ȿ�� ����
		jwtProvider.validateRefreshTokens(refreshToken);
		
		//refresh token db ��ȸ -> null�̰ų� ��ġ���� ������ not matching token(��ȿ���� ���� ��ū) exception
		dbRefreshToken = refreshTokenRepository.findByMemberId(memberId).orElseThrow(() -> new AuthException(ExceptionCode.NOT_MATCHING_TOKEN)).getToken();
		if(!refreshToken.equals(dbRefreshToken)) {
			throw new AuthException(ExceptionCode.NOT_MATCHING_TOKEN);
		}
		
		//access token ��߱�
		newAccessToken = jwtProvider.regenerateAccessToken(memberId);
		
		return new MemberToken(newAccessToken, refreshToken);
	}
	
	

}
