package com.gdu.cashbook.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Mapper
public interface MemberMapper {
	// 이름, 전화번호, 이메일로 아이디 찾기
	public String selectMemberIdByMember(Member member);
	// member 수정
	public int updateMember(Member member);
	// member 삭제
	public int deleteMember(LoginMember loginMember);
	// member 개인 정보
	public Member selectMemberOne(LoginMember loginMember);
	// 회원가입 memberId 중복체크
	public String selectMemberId(String memberIdCheck);
	// 로그인 id, pw확인
	public LoginMember selectLoginMember(LoginMember loginMember);
	// member 추가
	public int insertMember(Member member);
}
