package com.joongbu.WebSNS.config.auth;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserMapper userMapper;
	
	
    // 스프링이 로그인 요청을 가로챌 때 , username password 변수를 가로채는데
    // password 부분 처리는 알아서함
    // username이 DB에 있는지만 확인해서 리턴해주면 됨
    // 이거 안만들어주면  username 이 그냥 user로 들어감 강제로 넣어줘야댐
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService : 진입");
		UserDto user = userMapper.findbyuserId(userId);
		
		// session.setAttribute("loginUser", user);
		return new PrincipalDetails(user);
	}
}