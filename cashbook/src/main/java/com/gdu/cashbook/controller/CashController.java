package com.gdu.cashbook.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.Category;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired
	private CashService cashService;
	// 가계부수정 action
	@PostMapping("/modifyCash")
	public String modifyCash(HttpSession session, Cash cash, @RequestParam(value="date") String date) { 
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		System.out.println(cash);
		cashService.modifyCash(cash);
		LocalDate day = LocalDate.parse(date);
		return "redirect:/getCashListByDate?day="+day;
	}
	
	// 가계부수정 form
	@GetMapping("/modifyCash")
	public String modifyCash(HttpSession session, Model model, @RequestParam(value="cashNo") int cashNo, @RequestParam(value="day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		Cash cash = cashService.getCashOne(cashNo);
		System.out.println(cash);
		List<Category> categoryList = cashService.getCategoryList();
		model.addAttribute("cash", cash);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("year", day.getYear());
		model.addAttribute("day", day.toString());
		return "modifyCash";
	}
	
	// 가계부입력 action
	@PostMapping("/addCash")
	public String addCash(HttpSession session, Cash cash) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		String memberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		cash.setMemberId(memberId);
		System.out.println(cash + " <--CashController.addCash cash");
		cashService.addCash(cash);
		LocalDate day = LocalDate.now();
		return "redirect:/getCashListByDate?day="+day;
	}
	
	// 가계부입력 form
	@GetMapping("/addCash")
	public String addCash(HttpSession session, Model model, Cash cash) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		LocalDate day = LocalDate.now();
		model.addAttribute("day", day);
		List<Category> categoryList = cashService.getCategoryList();
		System.out.println(categoryList + " <---categoryList");
		model.addAttribute("categoryList", categoryList);
		return "addCash";
	}
	
	// 가계부삭제
	@GetMapping("/removeCash")
	public String removeCash(HttpSession session, @RequestParam(value="cashNo") int cashNo, @RequestParam(value="day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		cashService.removeCash(cashNo);
		return "redirect:/getCashListByDate?day="+day;
	}
	
	// 가계부리스트
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model, @RequestParam(value="day") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		// 로그인한 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		System.out.println(day + " <--date");
		model.addAttribute("day", day);
		// 요일
		DayOfWeek dayOfWeek = day.getDayOfWeek();
		model.addAttribute("dayOfWeek", dayOfWeek);
		
		Cash cash = new Cash(); // id, date('yyyy-MM-dd')
		cash.setMemberId(loginMemberId);
		cash.setCashDate(day.toString());
		System.out.println(cash);
		// 가계부 리스트, 수입/지출 총합
		Map<String, Object> map = cashService.getCashListByDate(cash);
		model.addAttribute("cashList", (List<Cash>)map.get("cashList"));
		model.addAttribute("total", map.get("cashKindSum"));
		return "getCashListByDate";
	}
}
