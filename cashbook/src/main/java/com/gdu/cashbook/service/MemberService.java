package com.gdu.cashbook.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
	@Autowired private JavaMailSender javaMailSender;
	
	
	public int getMemberPw(Member member) {
		// pw 추가
		UUID uuid = UUID.randomUUID(); // 랜덤문자열 생성 라이브러리
		String memberPw = uuid.toString().substring(0, 8);
		member.setMemberPw(memberPw);
		int row = memberMapper.updateMemberPw(member);
		if(row==1) {
			System.out.println(memberPw + " <-- update memberPw");
			// 메일로 update 성공한 랜덤 pw 전송 (JavaMailSender 객체 사용)
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setTo(member.getMemberEmail());
			simpleMailMessage.setFrom("deokk95@gmial.com");
			simpleMailMessage.setSubject("Cashbook 비밀번호 찾기 결과");
			simpleMailMessage.setText("변경된 비밀번호 : " + memberPw);
			javaMailSender.send(simpleMailMessage);
		}
		return row;
	}
	
	// 이름, 전화번호, 이메일로 아이디 찾기
	public String getMemberIdByMember(Member member) {
		return memberMapper.selectMemberIdByMember(member);
	}
	
	// member 수정
	public int modifyMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
	// member 수정 전 비밀번호 확인
	public Member getMemberByIdAndPw(LoginMember loginMember) {
		return memberMapper.selectMemberByIdAndPw(loginMember);
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
