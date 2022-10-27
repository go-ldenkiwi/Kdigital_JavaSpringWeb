package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardReplyDto;

@Mapper
public interface BoardReplyMapper {

	int insert(BoardReplyDto reply);
	int delete(int replyNo);
	int update(BoardReplyDto reply);
}
