package com.tlog.backend.login;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tlog.backend.global.exception.ExceptionCode;
import com.tlog.backend.global.exception.ExpiredPeriodJwtException;
import com.tlog.backend.global.exception.InvalidJwtException;
import com.tlog.backend.login.domain.MemberToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	
	
	private final SecretKey secretKey;
	private final Long refreshExpirationTime;
	private final Long accessExpirationTime;
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public JwtProvider(@Value("${jwt.secret}") final String stringKey,
			@Value("${jwt.refresh_expiration_time}") final Long refreshExpirationTime,
			@Value("${jwt.access_expiration_time}") final Long accessExpirationTime) {
	  this.secretKey = Keys.hmacShaKeyFor(stringKey.getBytes(StandardCharsets.UTF_8));
	  this.refreshExpirationTime = refreshExpirationTime;
	  this.accessExpirationTime = accessExpirationTime;
	  
	}
	
	public MemberToken generateLoginToken(Long memberId) {
		String refreshToken = createToken(memberId, refreshExpirationTime);
		String accessToken = createToken(memberId, accessExpirationTime);
		
		return new MemberToken(accessToken, refreshToken);
	}
	
	public String createToken(Long memberId, Long expirationTime) {
		Date now = new Date();
		Date expiration = new Date(now.getTime() + expirationTime);
		
		return Jwts.builder()
				.header().add("type", "jwt")
				.and()
				.claim("memberId", memberId)
				.issuedAt(now)
				.expiration(expiration)
				.signWith(secretKey)
				.compact();
	}
	
	public void validateTokens(final String token) {
		try {
			parseToken(token);
		} catch (final ExpiredJwtException e) {
            throw new ExpiredPeriodJwtException(ExceptionCode.EXPIRED_PERIOD_ACCESS_TOKEN);
        } catch (final MalformedJwtException | IllegalArgumentException e) {
            throw new InvalidJwtException(ExceptionCode.INVALID_ACCESS_TOKEN);
        }
	}
	
	public void validateRefreshTokens(final String token) {
		try {
			parseToken(token);
		} catch (final ExpiredJwtException e) {
			throw new ExpiredPeriodJwtException(ExceptionCode.EXPIRED_PERIOD_REFRESH_TOKEN);
		} catch (final MalformedJwtException | IllegalArgumentException e) {
			throw new InvalidJwtException(ExceptionCode.INVALID_REFRESH_TOKEN);
		}
	}
	
	public String extractAccessToken(String authHeader) {
		
		if(authHeader.isBlank() || authHeader == null) {
			throw new InvalidJwtException(ExceptionCode.INVALID_ACCESS_TOKEN);
		} else {
			return authHeader.replace(TOKEN_PREFIX, "");
		}
	}
	
	public String regenerateAccessToken(Long memberId, String accessToken) {
		try {
			parseToken(accessToken);
		} catch (ExpiredJwtException e) {
			// 신규 토큰 발급
			return createToken(memberId, accessExpirationTime);
		}
		//기간 만료되지 않았다면 기존 토큰 그대로 리턴
		return accessToken;
	}
	
	private Jws<Claims> parseToken(final String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parse(token)
				.accept(Jws.CLAIMS);
				
	}
	public Long getMemberId(final String token) {
		return (Long) parseToken(token)
				.getPayload()
				.get("memberId");
				
	}
 
}
