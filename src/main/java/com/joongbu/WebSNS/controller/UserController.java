package com.joongbu.WebSNS.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.joongbu.WebSNS.config.auth.PrincipalDetails;
import com.joongbu.WebSNS.config.oauth.JwtProperties;
import com.joongbu.WebSNS.dto.MailDto;
import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.UserMapper;
import com.joongbu.WebSNS.service.UserService;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.oauth2.sdk.Response;

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
	
	@Autowired
	UserService userService;
	
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
	public String insert(UserDto user, HttpSession session) {
		int insert = 0;
		String msg = "";
		String pw = bCryptPasswordEncoder.encode(user.getPw());
		user.setPw(pw);
		try {
			insert= usermapper.insert(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		if(insert > 0 )
		{
			msg = "회원가입 성공";
			session.setAttribute("msg", msg);
			return "redirect:/user/login.do";
		}else {
			msg = "회원가입 실패";
			session.setAttribute("msg", msg);
			return "redirect:/user/signup.do";
		}
	}
	
	@GetMapping("/login.do")
	public void login(HttpServletRequest req, HttpSession session
			) {
		String refererPage = req.getHeader("Referer"); // 로그인 폼 오기 전 페이지
		session.setAttribute("redirectPage", refererPage);
	}
	
	@GetMapping("/test.do")
	public void login(
			HttpSession session,
			Authentication authentication1,
			HttpServletResponse response,
			HttpServletRequest request,
			@SessionAttribute(required = false) UserDto token,
			@AuthenticationPrincipal Principal aaaaaa,
			@SessionAttribute(required = false) String authentication,
			@SessionAttribute(required = false,name="loginUser")UserDto loginUser,
			@AuthenticationPrincipal PrincipalDetails principal
			
			) {
		System.out.println(loginUser);
		System.out.println(authentication);
	}
	
	@GetMapping("/Oauth2Login/{username}")
	public String oauth2Login(@PathVariable String username, HttpSession session, Authentication authentication){
		
		System.out.println("oauth2Login :" + username);
		UserDto user = usermapper.findbyuserId(username);
		if(user != null)
		{
			String msg = "소셜로그인 성공";
			session.setAttribute("loginUser", user);
			session.setAttribute("msg", msg);
		}
		return "redirect:/";
	}
	
	@GetMapping("/findPw.do")
	public void findPw() {}
	
	@PostMapping("/findPw.do")
	public String findPw(String userId, String email, HttpSession session) {
		System.out.println("userid" +userId );
		System.out.println("email" +email );
		
		String msg = "";
		
		if(userId == null && email == null)
		{
			msg = "아이디와 이메일을 확인해주세요.";
		}else	
		{
			UserDto user = usermapper.findbyuserId(userId);
			if(user == null)
			{
				msg = "등록 되지 않은 아이디 입니다.";
			}else {
				String ckEmail = user.getEmail();
					if(!user.getEmail().equals(ckEmail))
					{
						msg = "등록하신 이메일이 아닙니다.";
					}
					else {

						MailDto dto = userService.createMailAndChangePassword(userId, email); //이메일 전송
						userService.mailSend(dto);
					}
			}
		}
		session.setAttribute("msg", msg);
		return "redirect:/user/login";
	}
	
	
	@GetMapping("/loginhelp")
	public String ddd(){
		return "redirect:/";
	}
	
	@PostMapping("/loginhelp")
	public String loginhelp(HttpServletRequest request, HttpServletResponse response,
			HttpSession session, String username ){
		
		String msg = "";
		System.out.println("loginhelp" + username);
		UserDto user = usermapper.findbyuserId(username);
		if(user != null)
		{
			msg = "로그인 완료";
			session.setAttribute("loginUser", user);
			session.setAttribute("msg", msg);
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/logout.do")
	public String logout(HttpSession session) {
		String msg = "로그아웃 되었습니다.";
		session.removeAttribute("loginUser");
		session.setAttribute("msg", msg);
		return "redirect:/";
	}
	
    @GetMapping("/failLogin")
    public String failLogin(@RequestParam(value = "error", required = false)String error,
    		@RequestParam(value = "exception", required = false)String exception,                        
    		HttpSession session) { 
    	System.out.println(exception);
    	session.setAttribute("msg", exception);
    	return "redirect:/user/login.do";
    	}

	
	
//	@PostMapping("/login.do")
//	public String login(
//			@RequestParam(required=true)String userId, 
//			@RequestParam(required=true)String pw, 
//			HttpSession session,
//			@SessionAttribute(required=false) String redirectPage
//			){
//		System.out.println(userId+"/" + pw);
//		String pwck = bCryptPasswordEncoder.encode(pw);
//		System.out.println(redirectPage);
//		UserDto loginUser = null;
//		System.out.println(pwck);
//		
//		try {
//			loginUser = usermapper.login(userId, pwck);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println("loginUser" +loginUser);
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
