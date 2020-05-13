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
		if(session.getAttribute("loginMember") == null) {
			memberService.insertMember(member);
		}
		return "redirect:/index";
	}
	
	@GetMapping("/login")
	public String login(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "login";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping("/login")
	public String login(HttpSession session, Model model, LoginMember loginMember) {
		LoginMember returnLoginMember = memberService.selectMember(loginMember);
		if(returnLoginMember == null) {	// 로그인 실패
			//model.addAttribute("msg", "");
			return "redirect:/login";
		} else { // 로그인 성공
			session.setAttribute("loginMember", loginMember);
			return "redirect:/";
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
	
	@PostMapping("/checkMemberId")
	public String checkMemberId(HttpSession session, Model model, @RequestParam("memberIdCheck") String memberIdCheck) {
		System.out.println(memberIdCheck);
		// 로그인 되있을때
		if(session.getAttribute("loginMember") != null) {
			return "redirect:/";
		}
		String confirmMemberId = memberService.checkMemberId(memberIdCheck);
		System.out.println(confirmMemberId);
		if(confirmMemberId != null) {
			// 아이디 중복
			model.addAttribute("msg", "이미 사용중인 아이디 입니다");
		} else {
			// 아이디 중복 안됨
			model.addAttribute("confirmMemberId", memberIdCheck);
			System.out.println(memberIdCheck);
		}
		return "addMember";
	}
}
