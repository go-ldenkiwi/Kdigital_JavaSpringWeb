package com.joongbu.WebSNS.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.joongbu.WebSNS.dto.UserDto;

@Mapper // xml로 작성한 sql을 맵핑하는 Type 지정 
public interface UserMapper {
	UserDto detail(String userId);
	int insert(UserDto user);
	UserDto login(String userId,String pw);
	UserDto findbyuserId(String username);
}