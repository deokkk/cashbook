package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper
public interface MemberMapper {
	public String selectMemberId(String memberIdCheck);
	public LoginMember selectLoginMember(LoginMember loginMember);
	public int insertMember(Member member);
}
