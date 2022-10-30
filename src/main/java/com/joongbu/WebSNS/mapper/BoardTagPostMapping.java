package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.BoardTagPostMappingDto;

@Mapper
public interface BoardTagPostMapping {
	int insertTagPost(BoardTagPostMappingDto tagPostMapping);
}
