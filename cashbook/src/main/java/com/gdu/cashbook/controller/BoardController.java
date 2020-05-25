package com.gdu.cashbook.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.BoardService;

@Controller
public class BoardController {
	@Autowired private BoardService boardService;
	
	// 전체 게시글
	@GetMapping("/boardListByPage")
	public String getBoardListByPage(HttpSession session, Model model, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
		System.out.println(currentPage + " <--currentPage");
		Map<String, Object> map = boardService.getBoardListByPage(currentPage);
		System.out.println(map.get("boardList"));
		model.addAttribute("boardList", map.get("boardList"));
		model.addAttribute("page", map.get("page"));
		return "BoardListByPage";
	}
}
