package com.joongbu.WebSNS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joongbu.WebSNS.dto.GroupBoardDto;
import com.joongbu.WebSNS.dto.GroupUserRegisteredDto;
import com.joongbu.WebSNS.dto.SearchDto;
import com.joongbu.WebSNS.dto.UserDto;
import com.joongbu.WebSNS.mapper.GroupBoardMapper;
import com.joongbu.WebSNS.mapper.GroupMapper;

@Service
public class GroupService {
	@Autowired
	GroupBoardMapper groupBoardMapper;
	@Autowired
	GroupMapper groupMapper;
	public PageInfo<GroupBoardDto> groupBoardPaging(SearchDto search){
		PageHelper.startPage(search.getPage(),search.getROWS(),search.getOrderBy());
		return PageInfo.of(groupBoardMapper.list(),search.getNavSize());
	}
	
	public List<GroupUserRegisteredDto> myList(UserDto user){
		int userNo = user.getUserNo();
		
	}
	
	
	
	
	
}
