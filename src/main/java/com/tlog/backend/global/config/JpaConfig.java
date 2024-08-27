package com.tlog.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //테스트 진행 시@WebMvcTest와 충돌을 막기위해 JpaConfig 별도 생성
public class JpaConfig {

}
