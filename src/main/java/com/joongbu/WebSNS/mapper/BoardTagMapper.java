package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardTagDto;

@Mapper
public interface BoardTagMapper {

	BoardTagDto findTagByContent(String content);
	int insert(BoardTagDto tag);
}
