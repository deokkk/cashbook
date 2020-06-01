package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
	// 관리자로 변경
	public int insertAdmin(String memberId);
	// 관리자 계정인지 확인
	public String selectAdmin(String memberId);
}
