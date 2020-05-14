package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;

@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	
	public int modifyMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
	public int removeMember(String memberId) {
		// 삭제할 id memberid테이블에 추가
		memberMapper.insertMemberid(memberId);
		// member 테이블에서 삭제
		int result = memberMapper.deleteMember(memberId);
		return result;
	}
	
	public int getConfirmMemberCount(Member member) {
		return memberMapper.selectConfirmMemberCount(member);
	}
	
	public Member getMemberOne(LoginMember loginMember) {
		return memberMapper.selectMemberOne(loginMember);
	}
	
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck);
	}
	
	public LoginMember getMember(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	public int addMember(Member member) {
		//System.out.println("service" + member.toString());
		return memberMapper.insertMember(member);
	}
}
