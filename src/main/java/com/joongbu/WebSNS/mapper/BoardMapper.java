package com.joongbu.WebSNS.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardDto;

@Mapper
public interface BoardMapper {
	BoardDto detail(int boardNo);
	List<BoardDto> list();
	List<BoardDto> findByCategory(String category);
	int insert(BoardDto board);
	int update(BoardDto board);
	int delete(int boardNo);
}