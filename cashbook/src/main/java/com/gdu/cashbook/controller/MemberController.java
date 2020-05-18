package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
import com.gdu.cashbook.vo.MemberForm;
@Controller
public class MemberController {
	@Autowired	// 자동으로 객체 생성
	private MemberService memberService;
	
	@GetMapping("/findMemberPw")
	public String findMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "findMemberPw";
	}
	
	@PostMapping("/findMemberPw")
	public String findMemberPw(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		int row = memberService.getMemberPw(member);
		String msg = "아이디와 메일을 확인하세요";
		if(row == 1) {
			msg = "비밀번호가 메일로 전송되었습니다.";
		}
		System.out.println(msg);
		model.addAttribute("msg", msg);
		return "memberPwView";
	}
	
	@GetMapping("/findMemberId")
	public String findMemberId(HttpSession session) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "findMemberId";
	}
	
	@PostMapping("/findMemberId")
	public String findMemberId(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		String memberIdPart = memberService.getMemberIdByMember(member);
		model.addAttribute("memberIdPart", memberIdPart);
		return "memberIdView";
	}
	
	@GetMapping("/modifyMember")
	public String modifyMember(HttpSession session, Model model) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)session.getAttribute("loginMember"));
		model.addAttribute("member", member);
		return "modifyMember";
	}
	
	@PostMapping("/modifyMember")
	public String modifyMember(HttpSession session, MemberForm memberForm) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		// 이미지 파일이 입력됐을때
		MultipartFile mf = memberForm.getMemberPic();
		if(memberForm.getMemberPic() != null && !mf.getOriginalFilename().equals("")) {
			if(!memberForm.getMemberPic().getContentType().equals("image/png") && !memberForm.getMemberPic().getContentType().equals("image/jpeg") && !memberForm.getMemberPic().getContentType().equals("image/gif")) {
				return "redirect:/modifyMember?imgMsg=n";
			}
		}
		memberService.modifyMember(memberForm);
		return "redirect:/memberInfo";
	}
	
	@GetMapping("/modifyPwConfirm")
	public String modifyConfirm(HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "modifyPwConfirm";
	}
	
	// 비밀번호 확인 후 수정폼
	@PostMapping("/modifyPwConfirm")
	public String modifyConfirm(HttpSession session, Model model, String memberPw) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		LoginMember loginMember = (LoginMember)(session.getAttribute("loginMember"));
		loginMember.setMemberPw(memberPw);
		System.out.println(loginMember);
		Member member = memberService.getMemberByIdAndPw(loginMember);
		System.out.println(member);
		if(member==null) {
			model.addAttribute("msg", "비밀번호를 확인하세요");
			return "modifyPwConfirm";
		}
		model.addAttribute("member", member);
		return "modifyMember";
	}
	
	// 회원탈퇴 비밀번호확인 form
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "removeMember";
	}
	
	// 회원탈퇴 action
	@PostMapping("/removeMember") // @RequestParam("memberPw") String memberPw
	public String removeMember(HttpSession session, Model model, @RequestParam("memberPw") String memberPw) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		LoginMember loginMember = (LoginMember)(session.getAttribute("loginMember"));
		loginMember.setMemberPw(memberPw);
		System.out.println(loginMember);
		
		int result = memberService.removeMember(loginMember);
		System.out.println(result);
		if(result!=1) {
			model.addAttribute("msg", "비밀번호를 다시 확인하세요"); 
			return "removeMember";
		}
		session.invalidate();
		return "redirect:/";
	}
	
	// 회원 정보
	@GetMapping("/memberInfo")
	public String getMemberOne(HttpSession session, Model model) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/";
		}
		Member member = memberService.getMemberOne((LoginMember)session.getAttribute("loginMember"));
		System.out.println(member);
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
			System.out.println(returnLoginMember);
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
