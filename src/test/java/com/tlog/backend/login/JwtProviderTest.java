package com.tlog.backend.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.tlog.backend.global.exception.ExpiredPeriodJwtException;
import com.tlog.backend.global.exception.InvalidJwtException;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.yml")
public class JwtProviderTest {

	@Value("${jwt.secret}")
	String secretKey;
	@Value("${jwt.refresh_expiration_time}")
	Long refreshTime;
	@Value("${jwt.access_expiration_time}")
	Long accessTime;
	
	
	@Test
	@DisplayName("yml파일에서 secret 값을 읽어온다.")
	void secretKey_test() {
		assertThat(secretKey).isNotNull();
	}
	
	@Test
	@DisplayName("토큰이 생성된다.")
	void create_token_test() {
		//given
		Long memberId = new Random().nextLong();
		JwtProvider provider = new JwtProvider(secretKey, refreshTime, accessTime);
		
		//when
		String token = provider.createToken(memberId, accessTime);
		
		//then
		assertThat(token).isNotNull();
		
		System.out.println("token:"+token);
	}
	
	@Test
	@DisplayName("토근에서 멤버 아이디를 추출한다.")
	void get_emberId_from_token() {
		//given
		JwtProvider provider = new JwtProvider(secretKey, refreshTime, accessTime);
		Long memberId = new Random().nextLong();
		String token = provider.createToken(memberId, accessTime);
		
		System.out.println("memberId: "+memberId);

		//when
		Long token_memberId = provider.getMemberId(token);
		
		//then
		assertThat(token_memberId).isEqualTo(memberId);

	}
	
	@Test
	@DisplayName("토큰이 만료되었을 때 ExpiredPeriodJwtException 발생한다.")
	void validateTokens_test1() {
		//given
		JwtProvider provider = new JwtProvider(secretKey, refreshTime, accessTime);
		Long memberId = new Random().nextLong();
		String token = provider.createToken(memberId, 0L);
		
		//when, then
		assertThrows(ExpiredPeriodJwtException.class, ()->{provider.validateTokens(token);});
		
	}
	
	@Test
	@DisplayName("토큰 값이 잘못된 경우 InvalidJwtException 발생한다.")
	void validateTokens_test2() {
		//given
		JwtProvider provider = new JwtProvider(secretKey, refreshTime, accessTime);
		String token = null;
		
		//when, then
		assertThrows(InvalidJwtException.class, ()->{provider.validateTokens(token);});
	}
	
}
