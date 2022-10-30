package com.joongbu.WebSNS.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.joongbu.WebSNS.dto.GroupDto;
import com.joongbu.WebSNS.dto.GroupInviteDto;
import com.joongbu.WebSNS.dto.GroupRegisterDto;

@Mapper
public interface GroupUserInteractionMapper {
	int registerStack(GroupRegisterDto registerStack);
	int inviteStack(GroupInviteDto invitestack);
	
	
}
