package com.joongbu.WebSNS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.GroupMapper;

@RequestMapping("/nav")
@Controller
public class AsideNavControllor {
	@Autowired
	GroupMapper groupMapper;
	@GetMapping("/asideNav")
	public String list(
			Model model,
			UserDto user
			) {
		try {
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
}
