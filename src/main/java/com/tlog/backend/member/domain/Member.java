package com.tlog.backend.member.domain;

import javax.persistence.*;

import com.tlog.backend.global.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Entity
@Table(name="Member", 
		uniqueConstraints
		= {@UniqueConstraint(name="EMAIL_UNIQUE",columnNames = "email")})
public class Member extends BaseTimeEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;
	
	private String password;
	
	private String nickname;
	
	 @Enumerated(EnumType.STRING)
	 @Column(nullable = false)
	 private Role role;
	 
	 @Builder
	 public Member(String email, String password, String nickname, Role role) {
		 this.email = email;
		 this.password = password;
		 this.nickname = nickname;
		 this.role = role;
	 }
	 
	 public String getKey() {
		 return this.role.getKey();
	 }
	 
	 
	 
}
