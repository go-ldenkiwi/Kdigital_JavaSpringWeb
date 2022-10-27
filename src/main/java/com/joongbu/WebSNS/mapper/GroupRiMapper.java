package com.joongbu.WebSNS.mapper;

import com.joongbu.WebSNS.dto.GroupInviteDto;
import com.joongbu.WebSNS.dto.GroupRegisterDto;

public interface GroupRiMapper {
	int registerStack(GroupRegisterDto registerStack);
	int inviteStack(GroupInviteDto invitestack);
}
