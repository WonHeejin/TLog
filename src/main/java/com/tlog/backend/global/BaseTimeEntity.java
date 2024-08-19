package com.tlog.backend.global;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass //JPA Enitiy Ŭ�������� BaseTimeEntity�� ����� ��� �ʵ�鵵 Į������ �ν��ϵ��� ��.
@EntityListeners(AuditingEntityListener.class) //Ŭ������ Auditing ����� ���Խ�Ŵ
public class BaseTimeEntity {
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

}
