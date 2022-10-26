package com.joongbu.WebSNS.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
	private String username;
	private String password;
}