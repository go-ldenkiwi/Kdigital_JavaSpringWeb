package com.joongbu.WebSNS.mapper;

import com.joongbu.WebSNS.dto.BoardDto;

public interface BoardMapper {
	BoardDto detail(int boardNo);
	int insert(BoardDto board);
	int update(BoardDto board);
	int viewUpdate(int boardNo);
	int delete(int boardNo);
}