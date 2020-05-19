package com.gdu.cashbook.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gdu.cashbook.service.CashService;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class CashController {
	@Autowired
	private CashService cashService;
	
	@GetMapping("/getCashListByDate")
	public String getCashListByDate(HttpSession session, Model model) {
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		// 로그인한 아이디
		String loginMemberId = ((LoginMember)session.getAttribute("loginMember")).getMemberId();
		// 오늘 날짜
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar today =  Calendar.getInstance();
		String strToday = sdf.format(today.getTime());
		System.out.println(today + " <--cashController.getCashListByDate today");
		System.out.println(strToday + " <--cashController.getCashListByDate strToday");
		System.out.println(loginMemberId + " <---cashController.getCashListByDate strToday");
		
		Cash cash = new Cash(); // id, date('yyyy-MM-dd')
		cash.setMemberId(loginMemberId);
		cash.setCashDate(strToday);
		
		List<Cash> cashList = cashService.getCashListByDate(cash);
		model.addAttribute("cashList", cashList);
		model.addAttribute("today", strToday);
		for(Cash c : cashList) {
			System.out.println(c);
		}
		
		return "getCashListByDate";
	}
}
