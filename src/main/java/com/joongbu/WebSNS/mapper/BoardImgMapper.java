package com.joongbu.WebSNS.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardImgDto;

@Mapper
public interface BoardImgMapper {

	int insert(BoardImgDto boardImgDto);
	int delete(int boardImgNo);
	int update(BoardImgDto boardImgDto);
	List<BoardImgDto> list(int boardNo);
	BoardImgDto detail(int boardImgNo);
}
