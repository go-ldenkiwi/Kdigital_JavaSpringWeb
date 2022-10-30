package com.joongbu.WebSNS.dto;

/*
 +-------------+--------------+------+-----+-------------------+-------------------+
| Field       | Type         | Null | Key | Default           | Extra             |
+-------------+--------------+------+-----+-------------------+-------------------+
| userId      | varchar(255) | NO   | UNI | NULL              |                   |
| userNo      | int          | NO   | PRI | NULL              | auto_increment    |
| nickname    | varchar(255) | NO   | UNI | NULL              |                   |
| pw          | varchar(255) | NO   |     | NULL              |                   |
| username    | varchar(255) | NO   |     | NULL              |                   |
| email       | varchar(255) | NO   |     | NULL              |                   |
| information | varchar(255) | NO   |     | NULL              |                   |
| signup      | datetime     | YES  |     | CURRENT_TIMESTAMP | DEFAULT_GENERATED |
+-------------+--------------+------+-----+-------------------+-------------------+
 */


import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {

	private String userId;
	private int userNo;
	private String nickname;
	private String pw;
	private String username;
	private String email;
	private String info;
	private String imgPath;
	private String content;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date signup;
	
	private List<GroupUserRegisteredDto> registeredGroupList; //User:GroupUserRegistered -> 1:N JOIN

	
	@Builder
	public UserDto(String userId, String nickname, String pw, String username, String email, String info,String content,
			Date signup) {
		this.userId = userId;
		this.nickname = nickname;
		this.pw = pw;
		this.username = username;
		this.email = email;
		this.info = info;
		this.signup = signup;
		this.content = content;
	}
	
}
