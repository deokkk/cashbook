package com.gdu.cashbook.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.service.AdminService;
import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;
@Controller
public class MemberController {
	@Autowired	// 자동으로 객체 생성
	private MemberService memberService;
	@Autowired private AdminService adminService;
	
	// 관리자로 변경
	@GetMapping("/addAdmin")
	public String addAdmin(HttpSession session, @RequestParam(value = "memberId") String memberId) {
		// 로그인 안되있을때 이거나 멤버타입이 일반회원일 경우
		if(session.getAttribute("loginMember")==null || session.getAttribute("memberType").equals("normal")) {
			return "redirect:/";
		}
		adminService.addAdmin(memberId);
		return "redirect:/getMemberListByPage";
	}
	
	// 관리자가 일반회원 탈퇴시키기
	@GetMapping("/removeMembeByAdmin")
	public String removeMemberByAdmin(HttpSession session, Model model, @RequestParam(value = "memberId") String memberId) {
		// 로그인 안되있을때 이거나 멤버타입이 일반회원일 경우
		if(session.getAttribute("loginMember")==null || session.getAttribute("memberType").equals("normal")) {
			return "redirect:/";
		}
		System.out.println(memberId + "memberId");
		
		int result = memberService.removeMemberByAdmin(memberId);
		System.out.println(result);
		return "redirect:/getMemberListByPage";
	}
	
	// 일반회원 memberList
	@GetMapping("/getMemberListByPage")
	public String getMemberList(HttpSession session, Model model, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage, @RequestParam(required = false, value = "searchWord") String searchWord) {
		System.out.println(session.getAttribute("memberType") + " <--getMemberList memberType");
		// 로그인 안되있을때 이거나 멤버타입이 일반회원일 경우
		if(session.getAttribute("loginMember")==null || session.getAttribute("memberType").equals("normal")) {
			return "redirect:/";
		}
		if(searchWord==null) {
			searchWord="";
		}
		// 페이징
		System.out.println(searchWord + " <--searchWord");
		System.out.println(currentPage + " <-- currentPage");
		Map<String, Object> map = memberService.getMemberListByPage(currentPage, searchWord);
		System.out.println(map.get("memberList") + " <--controller");
		System.out.println(map.get("page") + " <--controller");
		model.addAttribute("memberList", map.get("memberList"));
		model.addAttribute("page", map.get("page"));
		return "getMemberListByPage";
	}
	
	// 비밀번호찾기 form
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		// 로그인되어 있을때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "findMemberPw";
	}
	
	// 비밀번호 찾기 action
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session, Model model, Member member) {
		// 로그인되어 있을때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		int row = memberService.getMemberPw(member); // 일치하는 회원정보가 없을때
		String msg = "입력한 정보와 일치하는 정보가 없습니다.";
		if(row == 1) { // 일치하는 회원정보가 있을때
			msg = "비밀번호가 메일로 전송되었습니다.";
		}
		System.out.println(msg);
		model.addAttribute("msg", msg);
		return "memberPwView";
	}
	
	// 아이디 찾기 form
	@GetMapping("/findMemberId")
	public String findMemberId(HttpSession session) {
		// 로그인되어 있을때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "findMemberId";
	}
	
	// 아이디 찾기 action
	@PostMapping("/findMemberId")
	public String findMemberId(HttpSession session, Model model, Member member) {
		// 로그인되어 있을때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		String memberIdPart = memberService.getMemberIdByMember(member);
		if(memberIdPart == null) { // 일치하는 회원정보가 없을때
			System.out.println("아이디 없음");
			model.addAttribute("msg", "입력한 정보와 일치하는 아이디가 없습니다.");
		}
		model.addAttribute("memberIdPart", memberIdPart);
		return "memberIdView";
	}
	
	// 회원정보 수정 form
	@GetMapping("/modifyMember")
	public String modifyMember(HttpSession session, Model model) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)session.getAttribute("loginMember"));
		model.addAttribute("member", member);
		return "modifyMember";
	}
	
	// 회원정보 수정 action
	@PostMapping("/modifyMember")
	public String modifyMember(HttpSession session, MemberForm memberForm) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		MultipartFile mf = memberForm.getMemberPic();
		// 이미지 파일이 입력됐을때
		if(memberForm.getMemberPic() != null && !mf.getOriginalFilename().equals("")) {
			// 입력된 파일형식이 이미지가 아닐 경우
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif")) {
				return "redirect:/modifyMember?imgMsg=n";
			}
		}
		memberService.modifyMember(memberForm);
		return "redirect:/memberInfo";
	}
	
	// 회원 정보 수정 전 비밀번호 확인폼
	@GetMapping("/modifyPwConfirm")
	public String modifyConfirm(HttpSession session) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "modifyPwConfirm";
	}
	
	// 비밀번호 확인 후 회원정보 수정폼
	@PostMapping("/modifyPwConfirm")
	public String modifyConfirm(HttpSession session, Model model, String memberPw) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		LoginMember loginMember = (LoginMember)(session.getAttribute("loginMember"));
		loginMember.setMemberPw(memberPw);
		System.out.println(loginMember);
		Member member = memberService.getMemberByIdAndPw(loginMember);
		System.out.println(member);
		if(member==null) { // 비밀번호가 일치하지 않을 경우
			model.addAttribute("msg", "비밀번호를 확인하세요");
			return "modifyPwConfirm";
		}
		model.addAttribute("member", member);
		return "modifyMember";
	}
	
	// 회원탈퇴 비밀번호확인 form
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "removeMember";
	}
	
	// 회원탈퇴 action
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session, Model model, @RequestParam("memberPw") String memberPw) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		LoginMember loginMember = (LoginMember)(session.getAttribute("loginMember"));
		loginMember.setMemberPw(memberPw);
		System.out.println(loginMember + "loginMember");
		
		int result = memberService.removeMember(loginMember);
		System.out.println(result);
		if(result!=1) {
			model.addAttribute("msg", "비밀번호를 확인하세요"); 
			return "removeMember";
		}
		session.invalidate();
		return "redirect:/";
	}
	
	// 회원 정보
	@GetMapping("/memberInfo")
	public String getMemberOne(HttpSession session, Model model) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)session.getAttribute("loginMember"));
		System.out.println(member + " <--MemberController.memberInfo member");
		model.addAttribute("member", member);
		return "memberInfo";
	}
	
	// 회원가입 form
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		// 로그인중일때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "addMember";
	}
	
	// 회원가입 action
	@PostMapping("/addMember")
	public String addMember(HttpSession session, MemberForm memberForm) {
		//System.out.println("controller" + member.toString());
		//System.out.println(memberForm + " <--memberController.addmember memberForm");
		if(session.getAttribute("loginMember") == null) { // 로그인 안되있을때
			MultipartFile mf = memberForm.getMemberPic();
			// 이미지 파일이 입력됐을때
			if(memberForm.getMemberPic() != null && !mf.getOriginalFilename().equals("")) {
				if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif")) {
					return "redirect:/addMember?imgMsg=n";
				}
			}
			memberService.addMember(memberForm);
		}
		return "redirect:/index";
	}
	
	// 로그인 form
	@GetMapping("/login")
	public String login(HttpSession session) {
		if(session.getAttribute("loginMember") == null) { // 로그인 안되있을때
			return "login";
		} else { // 로그인 되있을때
			return "redirect:/";
		}
	}
	
	// 로그인 action
	@PostMapping("/login")
	public String login(HttpSession session, Model model, LoginMember loginMember) {
		LoginMember returnLoginMember = memberService.getLoginMember(loginMember);
		if(returnLoginMember == null) {	// 로그인 실패
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			return "login";
		} else { // 로그인 성공
			session.setAttribute("loginMember", returnLoginMember);
			session.setAttribute("memberPic", memberService.getMemberPic(loginMember.getMemberId()));
			System.out.println(returnLoginMember);
			
			// 로그인한 계정이 관리자계정일때
			if(adminService.getAdmin(loginMember.getMemberId())!=null) {
				System.out.println(adminService.getAdmin(loginMember.getMemberId()) + " <-- adminMember");
				session.setAttribute("memberType", "admin");
			} else {
				session.setAttribute("memberType", "normal");
			}
			return "redirect:/home";
		}
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// 로그인 되있을때
		if(session.getAttribute("loginMember") != null) {
			session.invalidate();
		}
		return "redirect:/";
	}
	
	// 아이디 중복체크
	@PostMapping("/checkMemberId")
	public String checkMemberId(HttpSession session, Model model, @RequestParam("memberIdCheck") String memberIdCheck) {
		// 로그인 되있을때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		String confirmMemberId = memberService.checkMemberId(memberIdCheck);
		if(confirmMemberId != null) {
			// 아이디 중복
			model.addAttribute("msg", "이미 사용중인 아이디 입니다");
		} else {
			// 아이디 중복 안됨
			model.addAttribute("confirmMemberId", memberIdCheck);
		}
		return "addMember";
	}
}
