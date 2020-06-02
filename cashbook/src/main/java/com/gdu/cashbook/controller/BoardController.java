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
import com.gdu.cashbook.service.CommentService;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.BoardForm;
import com.gdu.cashbook.vo.LoginMember;

@Controller
public class BoardController {
	@Autowired private BoardService boardService;
	@Autowired private CommentService commentService;
	
	// 답글작성 action
	@PostMapping("/addBoardByAdmin")
	public String addBoardByAdmin(HttpSession session, Model model, BoardForm boardForm) {
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
		System.out.println(boardForm);
		boardService.addBoard(boardForm);
		return "redirect:/getBoardListByPage";
	}
	
	// 게시글 수정 form
	@GetMapping("/modifyBoard")
	public String modifyBoard(HttpSession session, Model model, @RequestParam(value="boardNo") int boardNo) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		System.out.println(boardService.getModifyBoard(boardNo) + " <--getBoardOne");
		model.addAttribute("board", boardService.getModifyBoard(boardNo));
		return "modifyBoard";
	}
	
	// 게시글 수정 action
	@PostMapping("/modifyBoard")
	public String modifyBoard(HttpSession session, BoardForm boardForm) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		// 파일 입력됬을때
		MultipartFile mf = boardForm.getBoardPic();
		if(boardForm.getBoardPic() != null && !mf.getOriginalFilename().equals("")) {
			if(!boardForm.getBoardPic().getContentType().equals("image/png") && !boardForm.getBoardPic().getContentType().equals("image/jpeg") && !boardForm.getBoardPic().getContentType().equals("image/gif")) {
				return "redirect:/modifyBoard?imgMsg=n";
			}
		}
		boardService.modifyBoard(boardForm);
		return "redirect:/boardDetail?boardNo="+boardForm.getBoardNo();
	}
	
	// 게시글 삭제
	@GetMapping("/removeBoard")
	public String removeBoard(HttpSession session, @RequestParam(value="boardNo") int boardNo) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		boardService.removeBoard(boardNo);
		return "redirect:/getBoardListByPage";
	}
	
	// 게시글 상세보기
	@GetMapping("/boardDetail")
	public String boardDetail(HttpSession session, Model model, @RequestParam(value = "boardNo") int boardNo, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
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
		
		System.out.println(currentPage + " <--comment currentPage");
		Map<String, Object> commentMap = commentService.getCommentListByPage(boardNo, currentPage);
		System.out.println(commentMap.get("commentList") + " <-- commentListByPage");
		model.addAttribute("commentList", commentMap.get("commentList"));
		model.addAttribute("page", commentMap.get("page"));
		System.out.println(commentMap.get("page") + " <-- comment page");
		return "boardDetail";
	}
	
	// 게시글 추가 form
	@GetMapping("/addBoard")
	public String addBoard(HttpSession session, Model model, @RequestParam(value="boardNo", required = false) String boardNo) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		System.out.println(boardNo + " <--boardNo");
		model.addAttribute("boardNo", boardNo);
		return "addBoard";
	}
	
	// 게시글 추가 action
	@PostMapping("/addBoard")
	public String addBoard(HttpSession session, BoardForm boardForm, @RequestParam(value="originBoardNo", required = false) String originBoardNo) {
		System.out.println(originBoardNo + " <--addBaord boardNo");
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
		if(originBoardNo == null) {
			boardService.addBoard(boardForm);
		} else {
			boardForm.setBoardNo(Integer.parseInt(originBoardNo));
			boardService.addBoardByAdmin(boardForm);
		}
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
