package com.joongbu.WebSNS.mapper;

import java.util.List;

import com.joongbu.WebSNS.dto.GroupReplyDto;

public interface GroupReplyMapper {
	List<GroupReplyDto> list();
	GroupReplyDto detail(int replyNo);
	int insert(GroupReplyDto reply);
	int update(GroupReplyDto reply);
	int delete(int replyNo);
}
