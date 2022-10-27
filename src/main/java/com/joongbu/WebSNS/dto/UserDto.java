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

import lombok.Data;

@Data
public class UserDto {

	private String userId;
	private int userNo;
	private String nickname;
	private String pw;
	private String username;
	private String email;
	private String information;
	private Date signup;
}
