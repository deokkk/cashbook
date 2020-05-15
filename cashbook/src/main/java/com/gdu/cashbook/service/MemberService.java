package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.Memberid;

@Service
@Transactional
public class MemberService {
	@Autowired private MemberMapper memberMapper;
	@Autowired private MemberidMapper memberidMapper;
	
	// 이름, 전화번호, 이메일로 아이디 찾기
	public String getMemberIdByMember(Member member) {
		return memberMapper.selectMemberIdByMember(member);
	}
	
	// member 수정
	public int modifyMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
	// member 삭제
	public int removeMember(LoginMember loginMember) {
		// member 테이블에서 삭제
		int deleteResult = memberMapper.deleteMember(loginMember);
		int insertResult = 0;
		if(deleteResult==1) {
			// 삭제할 id memberid테이블에 추가
			Memberid memberid = new Memberid();
			memberid.setMemberid(loginMember.getMemberId());
			insertResult = memberidMapper.insertMemberid(memberid);
		}
		
		return insertResult;
	}
	
	// member 개인 정보
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	// 회원가입 memberId 중복체크
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck);
	}
	
	// 로그인 id, pw확인
	public LoginMember getLoginMember(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	// member 추가
	public int addMember(Member member) {
		//System.out.println("service" + member.toString());
		return memberMapper.insertMember(member);
	}
}
