package com.joongbu.WebSNS.config.oauth;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.joongbu.WebSNS.config.auth.PrincipalDetails;

@Component
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	//
	//
	//
	// JWT 방식으로 구현하였으나 사용하지 않고  session에 유저정보 저장
	//
	//
	//
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

    	
        PrincipalDetails principalDetail = (PrincipalDetails) authentication.getPrincipal();
        String username = principalDetail.getUsername();
        String password = principalDetail.getPassword();
        System.out.println(username);

       //HMAC Hash암호 방식
		String jwtToken = JWT.create()
				.withSubject(principalDetail.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id", principalDetail.getUser().getUserId())
				.withClaim("username", principalDetail.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));

        System.out.println("jwt 실행 확인");
        System.out.println(jwtToken);
        response.setHeader("Authorization", "Bearer "+jwtToken);
        response.setContentType("application/json; charset=utf-8");
        
        String url = makeRedirectUrl(username);
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("url: " + url);
        getRedirectStrategy().sendRedirect(request, response, url);

    }
    private String makeRedirectUrl(String username) {
        return UriComponentsBuilder.fromUriString("/user/Oauth2Login/"+username)
                .build().toUriString();
    }
}
