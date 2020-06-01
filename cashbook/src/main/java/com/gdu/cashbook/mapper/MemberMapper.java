package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.Page;

@Mapper
public interface MemberMapper {
	// 관리자가 일반회원 회원탈퇴시키기
	public int deleteMemberByAdmin(String memberId);
	// 전체 행 수
	public int selectMemberTotalRow(String searchWord);
	// 일반회원 memberList
	public List<Member> selectMemberListByPage(Page page);
	// 이미지파일 이름 찾기
	public String selectMemberPic(String memberId);	
	// 비밀번호 찾기 성공시 랜덤문자열로 비밀번호 변경
	public int updateMemberPw(Member member);
	// 이름, 전화번호, 이메일로 아이디 찾기
	public String selectMemberIdByMember(Member member);
	// member 수정
	public int updateMember(Member member);
	// member 수정 전 비밀번호 확인
	public Member selectMemberByIdAndPw(LoginMember loginMember);
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
