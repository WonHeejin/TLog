package com.tlog.backend.global.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import com.google.common.hash.Hashing;

public class Encryption {
	
	private final SecureRandom random = new SecureRandom();
	public String sha256encode(String origin) {
		
		return Hashing.sha256()
				.hashString(origin, StandardCharsets.UTF_8)
				.toString();
	}
	public String sha256encode(String origin, String salt) {
		
		return Hashing.sha256()
				.hashString(origin+salt, StandardCharsets.UTF_8)
				.toString();
	}
	
	public String getSalt() {
		byte[] salt = new byte[32];
		random.nextBytes(salt); //난수 생성
		
		return Base64.getEncoder().encodeToString(salt);
	}

}
