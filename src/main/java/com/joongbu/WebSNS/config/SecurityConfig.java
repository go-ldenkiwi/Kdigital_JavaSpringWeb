package com.joongbu.WebSNS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.joongbu.WebSNS.config.oauth.Oauth2SuccessHandler;
import com.joongbu.WebSNS.config.oauth.PrincipalOauth2UserService;
import com.joongbu.WebSNS.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig{	
	
	@Autowired
	UserMapper usermapper;
	
    @Autowired
    private Oauth2SuccessHandler oauth2SuccessHandler;
    
    @Autowired
    private LoginFailureHandler loginFailureHandler;

	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;

	@Autowired
	private CorsConfig corsConfig;
	
	@Bean 
	public BCryptPasswordEncoder passwordEncoder(){
	    return new BCryptPasswordEncoder();
	} 
	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
				.csrf().disable()
				.apply(new MyCustomDsl()) // 커스텀 필터 등록
			.and()
	            .authorizeHttpRequests()
	            .antMatchers().authenticated()
	            .anyRequest().permitAll()
			.and()
				.formLogin()
				.loginPage("/user/login")
				.loginProcessingUrl("/login")
				.successForwardUrl("/user/loginhelp")
				.failureHandler(loginFailureHandler)
//				.defaultSuccessUrl("/", true)
	    	.and()
	    		.oauth2Login()
	    		.successHandler(oauth2SuccessHandler)
	    		.userInfoEndpoint()
	    		.userService(principalOauth2UserService);

	    		
	    		return http.build();
		
		
	}

	public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			http.addFilter(corsConfig.corsFilter());
		}
	}
}