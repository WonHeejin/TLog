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
@MappedSuperclass //JPA Enitiy 클래스들이 BaseTimeEntity을 상속할 경우 필드들도 칼럼으로 인식하도록 함.
@EntityListeners(AuditingEntityListener.class) //클래스에 Auditing 기능을 포함시킴
public class BaseTimeEntity {
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;

}
