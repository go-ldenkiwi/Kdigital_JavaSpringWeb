package com.joongbu.WebSNS.config.oauth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.joongbu.WebSNS.config.auth.PrincipalDetails;
import com.joongbu.WebSNS.config.oauth.provider.KakaoUserInfo;
import com.joongbu.WebSNS.config.oauth.provider.NaverUserInfo;
import com.joongbu.WebSNS.config.oauth.provider.OAuth2UserInfo;
import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Autowired 
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// userRequest 는 code를 받아서 accessToken을 응답 받은 객체
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest); // 회원 프로필 조회

		// code를 통해 구성한 정보
		System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
		
		// token을 통해 응답받은 회원정보
		System.out.println("oAuth2User : " + oAuth2User);
	
		return processOAuth2User(userRequest, oAuth2User);
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

		int insert = 0;
		// Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
			System.out.println("네이버 로그인 요청");
			 oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
		}  else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")){
            System.out.println("카카오 로그인 요청");
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }
		else {
			System.out.println("오류");
		}
		
		// 	token을 통해 응답받은 회원정보 확인가능
		System.out.println("oAuth2UserInfoEmail:" + oAuth2UserInfo.getEmail());
		System.out.println("oAuth2UserInfoName:" + oAuth2UserInfo.getName());
		System.out.println("oAuth2UserInfoUsername:" + oAuth2UserInfo.getUsername());
		System.out.println("oAuth2UserInfo.getProvider() : " + oAuth2UserInfo.getProvider());
		System.out.println("oAuth2UserInfo.getProviderId() : " + oAuth2UserInfo.getProviderId());
		
		String userId = oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProvider();
		String pw = System.currentTimeMillis() +  oAuth2UserInfo.getProviderId();
		String bCryptpw = bCryptPasswordEncoder.encode(pw);
		
		UserDto user = userMapper.findbyuserId(userId);
		
		if(user == null) {
			user = UserDto.builder()
                    .userId(userId)
                    .nickname(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProvider())
                    .pw(bCryptpw)
                    .username(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .build();
            
		insert = userMapper.insert(user);
		
		}else {
			System.out.println("이미 존재하는 회원입니다.");
		}
		
		if(insert > 0 ) {
			System.out.println("회원가입 / 로그인 성공");
		}
		
		return new PrincipalDetails(user, oAuth2User.getAttributes());
	}
}