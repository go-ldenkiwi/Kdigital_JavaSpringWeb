package com.joongbu.WebSNS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joongbu.WebSNS.mapper.BoardReplyMapper;

@RequestMapping("/reply")
@Controller
public class BoardReplyController {

	@Autowired
	BoardReplyMapper boardReplyMapper;
	
	@GetMapping("/list.do")
	public String list() {
		System.out.println("dd");
		return "/reply/list";
	}
}
