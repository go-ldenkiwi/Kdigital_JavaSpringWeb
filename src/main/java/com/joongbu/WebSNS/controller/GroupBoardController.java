package com.joongbu.WebSNS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageInfo;
import com.joongbu.WebSNS.dto.GroupBoardDto;
import com.joongbu.WebSNS.dto.SearchDto;
import com.joongbu.WebSNS.service.GroupService;

@RequestMapping("/group/board")
@Controller
public class GroupBoardController {
	@Autowired
	GroupService groupService;
	
	@GetMapping("/list.do")
	public String list(
			Model model,
			SearchDto search
			) {
		PageInfo<GroupBoardDto> paging=null;
		try {
			if(search.getOrderBy()==null) {
				search.setOrderBy("groupBoardNo DESC");
			}
			paging = groupService.groupBoardPaging(search);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("paging",paging);
		System.out.println(paging);
		return "/group/board/list";
	}
	
}
