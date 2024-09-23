package com.tlog.backend.global.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tlog.backend.global.exception.ExceptionCode;
import com.tlog.backend.global.exception.InvalidJwtException;
import com.tlog.backend.login.JwtProvider;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter{

	private final JwtProvider jwtProvider;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		logger.info("jwt filter start");
		//로그인이나 회원가입이면 필터 종료
		if(request.getMethod().equals("POST")&&request.getRequestURI().startsWith("/member")
				|| request.getMethod().equals("POST")&&request.getRequestURI().startsWith("/login")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		//토큰 검사
		String jwtHeader = request.getHeader(HEADER_STRING);
		
		if(jwtHeader.isBlank() || jwtHeader == null) {
			throw new InvalidJwtException(ExceptionCode.INVALID_ACCESS_TOKEN);
		} else {
			String accessToken = jwtHeader.replace(TOKEN_PREFIX, "");
			jwtProvider.getMemberId(accessToken);
			jwtProvider.validateTokens(accessToken);
			
			filterChain.doFilter(request, response);
			return;
			
		}
			
		
	}

}
