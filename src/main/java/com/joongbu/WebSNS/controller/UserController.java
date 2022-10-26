package com.joongbu.WebSNS.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.UserMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequestMapping("/user")
@Controller 	// Spring MVC 컨테이너에서 관리하는 servlet
@RequiredArgsConstructor // final 붙은 필드의 생성자를 자동으로 생성해주는 lombok 어노테이션
public class UserController {
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserMapper usermapper;
	
	@Getter@Setter
	class CheckUser{
		private int check; // 0: 없음, 1: 존재함, -1: 통신오류
		private UserDto user;
	}

	@GetMapping("/checkUserId.do")
	public @ResponseBody CheckUser checkUserId(@RequestParam(required=true) String userId) {
		CheckUser checkUser = new CheckUser();
		UserDto user = null;
		try {
		 user = usermapper.detail(userId);
		 if(user != null) {
			 checkUser.setCheck(1);
			 checkUser.setUser(user);
		 }
		} catch (Exception e) {
			e.printStackTrace();
			 checkUser.setCheck(-1);
		}
		return checkUser;
	}
	
	@GetMapping("/signup.do")
	public void insert() {}
	
	@PostMapping("/signup.do")
	public String insert(UserDto user) {
		int insert = 0;
		
		String pw = bCryptPasswordEncoder.encode(user.getPw());
		user.setPw(pw);
		try {
			insert= usermapper.insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(insert > 0 )
		{
			return "redirect:/user/login.do";
		}else {
			return "redirect:/user/signup.do";
		}
	}
	
	@GetMapping("/login.do")
	public void login(
			HttpServletRequest req,
			HttpSession session
			) {
		String refererPage = req.getHeader("Referer"); // 로그인 폼 오기 전 페이지
		session.setAttribute("redirectPage", refererPage);
		
	}
	
//	@PostMapping("/login.do")
//	public String login(
//			@RequestParam(required=true)String userId, 
//			@RequestParam(required=true)String pw, 
//			HttpSession session,
//			@SessionAttribute(required=false) String redirectPage
//			){
//		System.out.println(userId+"/" + pw);
//		System.out.println(redirectPage);
//		UserDto loginUser = null;
//		
//		try {
//			loginUser = usermapper.login(userId, pw);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		if(loginUser != null) {
//			System.out.println("로그인 성공");
//			session.setAttribute("loginUser", loginUser);
//			if(redirectPage == null) {
//			return "redirect:/"; 
//			}else {
//				return "redirect:"+redirectPage; 
//			}
//		}
//		return "redirect:/user/login.do";
//	}
}
