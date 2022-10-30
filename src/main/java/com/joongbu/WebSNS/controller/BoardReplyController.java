package com.joongbu.WebSNS.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.joongbu.WebSNS.dto.BoardReplyDto;
import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.BoardReplyMapper;
import com.joongbu.WebSNS.service.BoardReplyService;

import lombok.Data;

@RequestMapping("/reply")
@Controller
public class BoardReplyController {

	@Autowired
	BoardReplyMapper replyMapper;
	
	@Autowired
	BoardReplyService replyService;
	
	@Data
	class CheckStatus{
		private int status;
		// 0: 등록 실패, 1: 등록 성공, -1: 로그인하세요, -2: 글쓴이만 수정가능
	}
	// 아직 로그인 시 예외처리 안함
	
	@PostMapping("/insert.do")
	public @ResponseBody CheckStatus insert(
			BoardReplyDto reply,
			@SessionAttribute(required = false) UserDto loginUser
			) {
		CheckStatus checkStatus=new CheckStatus();
		if(loginUser!=null) {
			reply.setUserNo(loginUser.getUserNo());
			int insert=0;
			try {
				insert=replyMapper.insert(reply);
			} catch (Exception e) {
				e.printStackTrace();
			}
			checkStatus.setStatus(insert);
		}else {
			checkStatus.setStatus(-1);
		}
		return checkStatus;
	}
	
	@GetMapping("/update.do")
	public String update(
			@RequestParam(required = true) int replyNo,
			@SessionAttribute(required = false) UserDto loginUser,
			Model model,
			HttpServletResponse resp
			) {
		BoardReplyDto reply=null;
		reply=replyMapper.detail(replyNo);
		if(loginUser.getUserNo()==reply.getUserNo()) {			
			model.addAttribute("reply",reply);
			return "/reply/update";
		}else {
			resp.setStatus(401); // Unauthorized
			return null;
		}
	}
	
	@GetMapping("/delete.do")
	public @ResponseBody CheckStatus delete(
			@RequestParam(required = true) int replyNo,
			@SessionAttribute(required = false) UserDto loginUser
			) {
		CheckStatus checkStatus=new CheckStatus();
		if(loginUser==null) {
			checkStatus.setStatus(-1);
			return checkStatus;
		}
		BoardReplyDto reply=null;
		int delete=0;
		try {
			reply=replyMapper.detail(replyNo);
			if(reply.getUserNo()==loginUser.getUserNo()) {				
				delete=replyMapper.delete(replyNo);
			}else {
				checkStatus.setStatus(-2);
				return checkStatus;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		checkStatus.setStatus(delete);
		return checkStatus;
	}
	
	@PostMapping("/update.do")
	public @ResponseBody CheckStatus update(
			BoardReplyDto reply,
			@SessionAttribute(required = false) UserDto loginUser
			) {
		int update=0;  

		CheckStatus checkStatus=new CheckStatus();
		if(loginUser==null) {
			checkStatus.setStatus(-1);
			return checkStatus;
		} else if(loginUser.getUserNo()!=reply.getUserNo()) {
			checkStatus.setStatus(-2);
			return checkStatus;
		}
		try {
			update=replyMapper.update(reply);
		} catch (Exception e) {
			e.printStackTrace();
		}
		checkStatus.setStatus(update);
		return checkStatus;
	}
	
	@GetMapping("/list.do")
	public String list(
			@RequestParam(required = true) int boardNo,
			Model model
			) {
		List<BoardReplyDto> replyList=null;
		try {
			replyList=replyMapper.list(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("replyList", replyList);
		return "/reply/list";
	}
}
