package com.tlog.backend.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tlog.backend.member.domain.Salt;

public interface SaltRepository extends JpaRepository<Salt, Long>{

}
