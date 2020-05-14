package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.MemberService;
import com.gdu.cashbook.vo.LoginMember;
import com.gdu.cashbook.vo.Member;
@Controller
public class MemberController {
	@Autowired	// 자동으로 객체 생성
	private MemberService memberService;
	
	@PostMapping("/modifyMember")
	public String modifyMember(HttpSession session, Member member) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		memberService.modifyMember(member);
		return "redirect:/memberInfo";
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
	
	@PostMapping("/removeMember")
	public String removeMember(HttpSession session, Model model, Member member) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		System.out.println(member);
		if(memberService.getConfirmMemberCount(member)!=0) {
			int result = memberService.removeMember(member.getMemberId());
			System.out.println(result + " < -- result");
			if(result==1) {
				session.invalidate();
				return "redirect:/";
			}
		}
		model.addAttribute("msg", "비밀번호를 다시 확인하세요");
		return "removeMember";
	}
	
	@GetMapping("/removeMember")
	public String removeMember(HttpSession session) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "removeMember";
	}
	
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
	
	@GetMapping("/addMember")
	public String addMember(HttpSession session) {
		// 로그인중일때
		if(session.getAttribute("loginMember")!=null) {
			return "redirect:/";
		}
		return "addMember";
	}
	
	@PostMapping("/addMember")
	public String addMember(HttpSession session, Member member) {
		//System.out.println("controller" + member.toString());
		if(session.getAttribute("loginMember") == null) { // 로그인 안되있을때
			memberService.addMember(member);
		}
		return "redirect:/index";
	}
	
	@GetMapping("/login")
	public String login(HttpSession session) {
		if(session.getAttribute("loginMember") == null) { // 로그인 안되있을때
			return "login";
		} else { // 로그인 되있을때
			return "redirect:/";
		}
	}
	
	@PostMapping("/login")
	public String login(HttpSession session, Model model, LoginMember loginMember) {
		LoginMember returnLoginMember = memberService.getMember(loginMember);
		if(returnLoginMember == null) {	// 로그인 실패
			model.addAttribute("msg", "아이디 또는 비밀번호를 확인하세요");
			return "login";
		} else { // 로그인 성공
			session.setAttribute("loginMember", returnLoginMember);
			System.out.println(returnLoginMember);
			return "redirect:/home";
		}
	}
	
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
