package com.joongbu.WebSNS.dto;

import java.util.Date;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {
	private String userId;
	private String nickname;
	private String pw;
	private String username;
	private String email;
	private String information;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date signup;
	
	@Builder
	public UserDto(String userId, String nickname, String pw, String username, String email, String information,
			Date signup) {
		this.userId = userId;
		this.nickname = nickname;
		this.pw = pw;
		this.username = username;
		this.email = email;
		this.information = information;
		this.signup = signup;
	}
	
}