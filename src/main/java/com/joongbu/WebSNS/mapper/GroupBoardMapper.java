package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.joongbu.WebSNS.dto.GroupBoardDto;

@Mapper
public interface GroupBoardMapper {
	Page<GroupBoardDto> list();
	
	GroupBoardDto detail(int boardNo);
	int insert(GroupBoardDto board);
	int update(GroupBoardDto board);
	int viewUpdate(int boardNo);
	int delete(int boardNo);
}
