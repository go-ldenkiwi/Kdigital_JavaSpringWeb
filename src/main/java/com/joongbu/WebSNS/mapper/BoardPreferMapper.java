package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardPreferDto;

@Mapper
public interface BoardPreferMapper {

	BoardPreferDto detail(int userNo, int boardNo);
	int insert(BoardPreferDto prefer);
	int delete(int boardPreferNo);
	int update(BoardPreferDto prefer);
}
