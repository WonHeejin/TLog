package com.tlog.backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //�׽�Ʈ ���� ��@WebMvcTest�� �浹�� �������� JpaConfig ���� ����
public class JpaConfig {

}
