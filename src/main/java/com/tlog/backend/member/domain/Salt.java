package com.tlog.backend.member.domain;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Entity
@Table(name = "salt")
public class Salt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	String salt;
	
	@OneToOne
	Member member;

	@Builder
	public Salt(Member member, String salt) {
		this.salt = salt;
		this.member = member;
	}
}
