package com.gdu.cashbook.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.AdminMapper;
import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.mapper.CommentMapper;
import com.gdu.cashbook.mapper.MemberMapper;
import com.gdu.cashbook.mapper.MemberidMapper;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;
import com.gdu.cashbook.vo.Memberid;
import com.gdu.cashbook.vo.Page;

@Service
@Transactional
public class MemberService {
	@Autowired private MemberMapper memberMapper;
	@Autowired private MemberidMapper memberidMapper;
	@Autowired private JavaMailSender javaMailSender;
	@Autowired private CashMapper cashMapper;
	@Autowired private BoardMapper boardMapper;
	@Autowired private CommentMapper commentMapper;
	
	// 경로 : linux(/), windows(\\)
	//@Value("D:\\sts-deok\\maven.1590373000896\\cashbook\\src\\main\\resources\\static\\upload\\")
	//@Value("http://deokk95.cafe24.com/cashbook/upload/")
	@Value("/deokk95/tomcat/webapps/cashbook/WEB-INF/classes/static/upload/")
	private String path;
	
	// 관리자가 일반회원 회원탈퇴시키기
	public int removeMemberByAdmin(String memberId) {
		String memberPic = memberMapper.selectMemberPic(memberId);
		File file = new File(path+memberPic);
		// member 삭제 전 그 memberId로 작성된 가계부 삭제
		cashMapper.deleteCashByMember(memberId);
		// member 삭제 전 그 memberId로 작성된 게시글 삭제
		boardMapper.deleteBoardByMember(memberId);
		// member 삭제 전 그 memberId로 작성된 댓글 삭제
		commentMapper.deleteCommentByMember(memberId);
		
		// 테이블에서 삭제
		int deleteResult = memberMapper.deleteMemberByAdmin(memberId);
		
		System.out.println(deleteResult + " <-- deleteResult");
		int insertResult = 0;
		// 비밀번호 확인이 맞아서 삭제됬을때
		if(deleteResult==1) {
			// 삭제할 id memberid테이블에 추가
			Memberid memberid = new Memberid();
			memberid.setMemberid(memberId);
			insertResult = memberidMapper.insertMemberid(memberid);
		}
		System.out.println(insertResult + " <-- insertResult");
		// member테이블에서 삭제 및 memberid테이블 추가 성공시 파일도 물리적으로 삭제
		if(insertResult==1 && file.exists() && !memberPic.equals("default.jpg")) {
			file.delete();
		}
		return insertResult;
	}
	
	// 일반회원 memberList
	public Map<String, Object> getMemberListByPage(int currentPage, String searchWord) {
		System.out.println(searchWord + " <-- memberList searchWord");
		Page page = new Page();
		int rowPerPage = 10;	// 페이지당 행수
		int beginRow = (currentPage-1)*rowPerPage;
		page.setBeginRow(beginRow);
		page.setRowPerPage(rowPerPage);
		page.setSearchWord(searchWord);
		page.setCurrentPage(currentPage);
		System.out.println(beginRow + " <--beginRow, " + rowPerPage + " <--rowPerPage");
		List<Member> memberList = memberMapper.selectMemberListByPage(page);
		System.out.println(memberList);
		int totalRow = memberMapper.selectMemberTotalRow(searchWord);
		System.out.println(totalRow + " <--member totalRow");
		int lastPage = totalRow%rowPerPage!=0 ? totalRow/rowPerPage+1 : totalRow/rowPerPage;
		page.setLastPage(lastPage);
		
		int pagePerGroup = 5; // 몇페이지씩 그룹
		page.setPagePerGroup(pagePerGroup);
		
		int currentPageGroup = (currentPage-1)%pagePerGroup==0 ? currentPage : (currentPage-1)/pagePerGroup*pagePerGroup+1;
		page.setCurrentPageGroup(currentPageGroup);
		
		int lastPageGroup = lastPage%pagePerGroup!=0 ? lastPage/pagePerGroup+1 : lastPage/pagePerGroup;
		page.setLastPageGroup(lastPageGroup);
		
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("memberList", memberList);
		
		return map;
	}
	
	// 로그인한 memberPic
	public String getMemberPic(String memberId) {
		return memberMapper.selectMemberPic(memberId);
	}
	
	// 비밀번호찾기 - 비밀번호 랜덤문자열로 변경 후 이메일로 전송
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
	public int modifyMember(MemberForm memberForm) {
		//System.out.println("service" + memberForm.toString());
		String originMemberPic = memberMapper.selectMemberPic(memberForm.getMemberId());
		MultipartFile mf = memberForm.getMemberPic();
		String originName = mf.getOriginalFilename();
		System.out.println(originName + " <--originName");
		String memberPic = null;
		if(originName.equals("")) { // 파일 입력안되면 그전 파일 이름이랑 같게
			memberPic = originMemberPic;
		} else { // 확장자
			File originFile = new File(path+originMemberPic);
			// 새로운 파일 입력되면 그 전 파일 삭제
			if(originFile.exists() && !originMemberPic.equals("default.jpg")) {
				originFile.delete();
			}
			int lastDot = originName.lastIndexOf(".");
			String extension = originName.substring(lastDot);
			System.out.println(extension);
			memberPic = memberForm.getMemberId()+extension;
			System.out.println(memberPic);
		}
		System.out.println(memberPic);
		// MemberForm타입 -> Member타입으로 service로
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberPic(memberPic);
		System.out.println(member + " <--memberService.modifyMember member");
		int row = memberMapper.updateMember(member);
		
		if(!originName.equals("")) {
			// file -> 물리적으로 저장			
			File file = new File(path+memberPic);
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(); // 예외처리 안해도 되는 예외
			}
		}
		return row;
	}
	
	// member 수정 전 비밀번호 확인
	public Member getMemberByIdAndPw(LoginMember loginMember) {
		return memberMapper.selectMemberByIdAndPw(loginMember);
	}
	
	// member 삭제
	public int removeMember(LoginMember loginMember) {
		String memberPic = memberMapper.selectMemberPic(loginMember.getMemberId());
		File file = new File(path+memberPic);
		// member 삭제 전 그 memberId로 작성된 가계부 삭제
		cashMapper.deleteCashByMember(loginMember.getMemberId());
		// member 삭제 전 그 memberId로 작성된 게시글 삭제
		boardMapper.deleteBoardByMember(loginMember.getMemberId());
		// member 삭제 전 그 memberId로 작성된 댓글 삭제
		commentMapper.deleteCommentByMember(loginMember.getMemberId());
		
		// 테이블에서 삭제
		int deleteResult = memberMapper.deleteMember(loginMember);
		
		System.out.println(deleteResult + " <-- deleteResult");
		int insertResult = 0;
		// 비밀번호 확인이 맞아서 삭제됬을때
		if(deleteResult==1) {
			// 삭제할 id memberid테이블에 추가
			Memberid memberid = new Memberid();
			memberid.setMemberid(loginMember.getMemberId());
			insertResult = memberidMapper.insertMemberid(memberid);
		}
		System.out.println(insertResult + " <-- insertResult");
		// member테이블에서 삭제 및 memberid테이블 추가 성공시 파일도 물리적으로 삭제
		if(insertResult==1 && file.exists() && !memberPic.equals("default.jpg")) {
			file.delete();
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
	public int addMember(MemberForm memberForm) {
		//System.out.println("service" + member.toString());
		MultipartFile mf = memberForm.getMemberPic();
		// 확장자 필요(이미지 파일만 받고 싶으면 if(!mf.getContextType().equals("image/png")) 이런식으로 분기
		String originName = mf.getOriginalFilename();
		System.out.println(originName + " <--originName");
		String memberPic = null;
		if(originName.equals("")) {
			System.out.println(" originName == '' ");
			memberPic = "default.jpg";
		} else {
			int lastDot = originName.lastIndexOf(".");
			String extension = originName.substring(lastDot);
			System.out.println(extension);
			memberPic = memberForm.getMemberId()+extension;
			System.out.println(memberPic);
		}
		
		// MemberForm타입 -> Member타입으로 service로
		Member member = new Member();
		member.setMemberId(memberForm.getMemberId());
		member.setMemberPw(memberForm.getMemberPw());
		member.setMemberName(memberForm.getMemberName());
		member.setMemberPhone(memberForm.getMemberPhone());
		member.setMemberAddr(memberForm.getMemberAddr());
		member.setMemberEmail(memberForm.getMemberEmail());
		member.setMemberPic(memberPic);
		System.out.println(member + " <--memberService.addMember member");
		int row = memberMapper.insertMember(member);
		if(!originName.equals("")) {
			// file -> 물리적으로 저장			
			File file = new File(path+memberPic);
			System.out.println(path);
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(); // 예외처리 안해도 되는 예외
			}
		}
		//return memberMapper.insertMember(member);
		return row;
	}
}
