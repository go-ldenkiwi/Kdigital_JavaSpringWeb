package com.joongbu.WebSNS.mapper;

import java.util.List;

import com.joongbu.WebSNS.dto.GroupDto;

public interface GroupMapper {
	List<GroupDto> list();
	GroupDto detail(int GroupNo);
	int insert(GroupDto group);
	int update(GroupDto group);
	int delete(int GroupNo);

}
