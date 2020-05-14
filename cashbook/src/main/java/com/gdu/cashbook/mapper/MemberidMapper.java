package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Memberid;

@Mapper
public interface MemberidMapper {

	// 삭제된 memberId memberid테이블에 추가
	int insertMemberid(Memberid memberid);
}
