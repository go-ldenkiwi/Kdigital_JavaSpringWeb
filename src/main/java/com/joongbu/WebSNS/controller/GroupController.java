package com.joongbu.WebSNS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joongbu.WebSNS.dto.GroupDto;
import com.joongbu.WebSNS.mapper.GroupMapper;

@RequestMapping("/group")
@Controller
public class GroupController {
	@Autowired
	GroupMapper groupMapper;
	@GetMapping("/detail.do")
	public String detail(
			@RequestParam(required=true)int groupNo,
			Model model
			) {
		GroupDto group = null;
		try {
			group=groupMapper.detail(groupNo);
			System.out.println(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(group!=null) {
			model.addAttribute("group",group);
			return "/group/detail";
		}else {
			return "redirect:/";
		}
	}

	

}
