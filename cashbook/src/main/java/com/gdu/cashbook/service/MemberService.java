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
	
	public int modifyMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
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
