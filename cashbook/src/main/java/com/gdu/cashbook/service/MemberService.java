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
	
	public int insertMember(Member member) {
		//System.out.println("service" + member.toString());
		return memberMapper.insertMember(member);
	}
	
	public LoginMember selectMember(LoginMember loginMember) {
		return memberMapper.selectLoginMember(loginMember);
	}
	
	public String checkMemberId(String memberIdCheck) {
		return memberMapper.selectMemberId(memberIdCheck);
	}

}
