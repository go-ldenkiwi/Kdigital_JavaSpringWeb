package com.joongbu.WebSNS.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardReplyDto;

@Mapper
public interface BoardReplyMapper {
	List<BoardReplyDto> list(int boardNo, int startRow, int rows);
	BoardReplyDto detail(int replyNo);
	int insert(BoardReplyDto boardReply);
	int update(BoardReplyDto boardReply);
	int delete(int replyNo);
}
