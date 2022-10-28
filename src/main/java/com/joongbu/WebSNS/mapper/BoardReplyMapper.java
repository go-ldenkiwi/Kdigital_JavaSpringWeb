package com.joongbu.WebSNS.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardReplyDto;

@Mapper
public interface BoardReplyMapper {

	int insert(BoardReplyDto reply);
	int delete(int replyNo);
	int update(BoardReplyDto reply);
	List<BoardReplyDto> list(int boardNo);
	BoardReplyDto detail(int replyNo);
}
