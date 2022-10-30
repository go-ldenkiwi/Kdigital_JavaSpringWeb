package com.joongbu.WebSNS.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

import com.joongbu.WebSNS.config.auth.PrincipalDetails;
import com.joongbu.WebSNS.config.oauth.JwtProperties;
import com.joongbu.WebSNS.dto.BoardDto;
import com.joongbu.WebSNS.dto.MailDto;
import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.BoardMapper;
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
	
	@Value("${spring.servlet.multipart.location}")
	String imgPath;
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	UserMapper usermapper;
	
	@Autowired
	BoardMapper boardmapper;
	
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
		 user = usermapper.findbyuserId(userId);
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
	
	@GetMapping("/checknickname.do")
	public @ResponseBody CheckUser checkNickname(@RequestParam(required=true) String nickname) {
		CheckUser checkUser = new CheckUser();
		UserDto user = null;
		try {
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
    
    @GetMapping("/profile.do")
    public String mypage(
    		@SessionAttribute(required = false)UserDto loginUser,
    		@RequestParam(required = true)String userId,
    		Model model,
			HttpServletResponse resp
    		) {
    	UserDto user = null;
    	BoardDto board = null; // 게시물 처리
    	user = usermapper.findbyuserId(userId);
    	model.addAttribute("user", user);
    	return "/user/profile";
    }
    
    @GetMapping("/profileupdate.do")
    public String profileupdate(
			@SessionAttribute UserDto loginUser, 
			@RequestParam(required = true)String userId,
			Model model
    		) { 
    	UserDto user = null;
    	user = usermapper.findbyuserId(userId);
    	model.addAttribute("user", user);
    	return "/user/profileupdate";
    }
    
    @PostMapping("/profileupdate.do")
    public String profileupdate(
			UserDto user,
			MultipartFile img,
			@SessionAttribute(required=false) UserDto loginUser,
			HttpSession session
    		) {
    	String nickname = user.getNickname();
    	String userId = user.getUserId();
    	String msg = "";
    	UserDto userck = usermapper.findbynicknameNotuserId(nickname, userId);
    	int update = 0;
    	
    	if(userck == null) {
    	String imgPaths = "";
    	System.out.println("중복닉네임체크 성공");
		try {
				if(img !=null && !img.isEmpty()) {
					String contentTypes[] = img.getContentType().split("/");
					if(contentTypes[0].equals("image")) {
						String fileName="user_"+System.currentTimeMillis()+"_"+((int)(Math.random()*10000))+"."+contentTypes[1];
						Path path=Paths.get(imgPath+"/user/"+fileName);
						img.transferTo(path);
						if(user.getImgPath()!=null) {
							File imgFile=new File(imgPath+"/"+user.getImgPath());
							System.out.println("이미지 파일 삭제:"+imgFile.delete());
						}
						user.setImgPath(fileName);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}	
			update = usermapper.update(user);
		
			if (update >0) {
				System.out.println("프로필 수정 성공");
				msg = "프로필 수정 성공";
				session.setAttribute("msg", msg);
				System.out.println(user.getUserId());
				return "redirect:/user/profile.do?userId="+user.getUserId();
			}else {
				System.out.println("프로필 수정 실패");
				msg = "프로필 수정 실패";
				session.setAttribute("msg", msg);
				return "redirect:/user/profile.do?userId="+user.getUserId();
			}
        }
    	else {
    		System.out.println("중복닉네임 실패");
			msg = "중복되는 닉네임이 있습니다. 다시 시도해주세요.";
			session.setAttribute("msg", msg);
			return "redirect:/user/profileupdate.do?userId="+user.getUserId();
    	}
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
