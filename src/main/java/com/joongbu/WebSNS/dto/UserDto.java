package com.joongbu.WebSNS.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
	private List<GroupUserRegisteredDto> registeredGroupList; //User:GroupUserRegistered -> 1:N JOIN
}