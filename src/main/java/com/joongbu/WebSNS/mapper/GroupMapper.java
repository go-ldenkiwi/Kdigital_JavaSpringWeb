package com.joongbu.WebSNS.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.GroupDto;
@Mapper
public interface GroupMapper {
	List<GroupDto> list();
	List<GroupDto> myList();
	GroupDto detail(int groupNo);
	int insert(GroupDto group);
	int update(GroupDto group);
	int delete(int groupNo);
	
}
