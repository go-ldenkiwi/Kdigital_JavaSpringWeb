package com.joongbu.WebSNS.mapper;

import java.util.List;

import com.joongbu.WebSNS.dto.GroupBoardDto;

public interface GroupBoardMapper {
	List<GroupBoardDto> list(int startRow, int rows);
	int listCount();
	
	GroupBoardDto detail(int BoardNo);
	int insert(GroupBoardDto board);
	int update(GroupBoardDto board);
	int viewUpdate(int groupBoardNo);
	int delete(int groupBoardNo);
}
