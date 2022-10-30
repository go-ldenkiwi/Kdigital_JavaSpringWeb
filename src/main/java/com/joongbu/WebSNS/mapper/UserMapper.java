package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.UserDto;

@Mapper // xml로 작성한 sql을 맵핑하는 Type 지정 
public interface UserMapper {
	UserDto detail(int userNo);
	int insert(UserDto user);
	UserDto login(String userId,String pw);
	UserDto findbyuserId(String userId);
	UserDto findbynicknameNotuserId(String nickname,String userId);
	UserDto findbyusername(String username);
	int update(UserDto user);
}