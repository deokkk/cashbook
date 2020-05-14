package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper
public interface MemberMapper {
	// member 개인 정보
	public Member selectMemberOne(LoginMember loginMember);
	// 회원가입 memberId 중복체크
	public String selectMemberId(String memberIdCheck);
	// 로그인 id, pw확인
	public LoginMember selectLoginMember(LoginMember loginMember);
	// member 추가
	public int insertMember(Member member);
}
