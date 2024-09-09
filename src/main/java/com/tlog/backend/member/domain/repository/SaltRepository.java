package com.tlog.backend.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tlog.backend.member.domain.Salt;

public interface SaltRepository extends JpaRepository<Salt, Long>{
	
	Optional<Salt> findByMemberId(Long MemberId);
}
