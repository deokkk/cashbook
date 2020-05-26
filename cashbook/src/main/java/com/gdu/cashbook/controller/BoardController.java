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

import com.gdu.cashbook.service.BoardService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.BoardForm;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class BoardController {
	@Autowired private BoardService boardService;
	// 게시글 삭제
	@GetMapping("/removeBoard")
	public String removeBoard(HttpSession session, @RequestParam(value="boardNo") int boardNo, @RequestParam(value="memberId") String memberId) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		boardService.removeBoard(boardNo);
		return "redirect:/getBoardListByPage";
	}
	
	// 게시글 상세보기
	@GetMapping("/boardDetail")
	public String boardDetail(HttpSession session, Model model, @RequestParam(value = "boardNo") int boardNo) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		Map<String, Object> map = boardService.getBoardOne(boardNo);
		model.addAttribute("board", map.get("board"));
		System.out.println(map.get("board") + " <---getBoardOne");
		model.addAttribute("firstBoardNo", map.get("firstBoardNo"));
		model.addAttribute("lastBoardNo", map.get("lastBoardNo"));
		model.addAttribute("nextBoardNo", map.get("nextBoardNo"));
		model.addAttribute("prevBoardNo", map.get("prevBoardNo"));
		return "boardDetail";
	}
	
	// 게시글 추가 form
	@GetMapping("/addBoard")
	public String addBoard(HttpSession session) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		return "addBoard";
	}
	
	// 게시글 추가 action
	@PostMapping("/addBoard")
	public String addBoard(HttpSession session, BoardForm boardForm) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		// 파일이 입력됐을때
		MultipartFile mf = boardForm.getBoardPic();
		if(boardForm.getBoardPic() != null && !mf.getOriginalFilename().equals("")) {
			if(!boardForm.getBoardPic().getContentType().equals("image/png") && !boardForm.getBoardPic().getContentType().equals("image/jpeg") && !boardForm.getBoardPic().getContentType().equals("image/gif")) {
				return "redirect:/addBoard?imgMsg=n";
			}
		}
		boardForm.setMemberId(((LoginMember)session.getAttribute("loginMember")).getMemberId());
		boardService.addBoard(boardForm);
		return "redirect:/getBoardListByPage";
	}
	
	// 전체 게시글
	@GetMapping("/getBoardListByPage")
	public String getBoardListByPage(HttpSession session, Model model, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage, @RequestParam(required = false, value = "searchWord") String searchWord) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		if(searchWord==null) {
			searchWord="";
		}
		System.out.println(currentPage + " <--currentPage");
		Map<String, Object> map = boardService.getBoardListByPage(currentPage, searchWord);
		System.out.println(map.get("boardList"));
		model.addAttribute("boardList", map.get("boardList"));
		model.addAttribute("page", map.get("page"));
		return "getBoardListByPage";
	}
}
