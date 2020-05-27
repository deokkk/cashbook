package com.gdu.cashbook.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gdu.cashbook.service.CommentService;
import com.gdu.cashbook.vo.Comment;

@Controller
public class CommentController {
	@Autowired private CommentService commentService;
	// 댓글 수정
	@PostMapping("/modifyComment")
	public String modifyComment(HttpSession session, Comment comment) { // , @RequestParam(value="commentNo") String commentNo, @RequestParam(value="boardNo") String boardNo, @RequestParam(value="commentContent") String commentContent
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		System.out.println(comment + " <--modify comment");
		commentService.modifyComment(comment);
		return "redirect:/boardDetail?boardNo="+comment.getBoardNo();
	}
	
	// 댓글 삭제
	@GetMapping("/removeComment")
	public String removeComment(HttpSession session, @RequestParam(value="commentNo") int commentNo, @RequestParam(value="boardNo") int boardNo) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		commentService.removeComment(commentNo);
		return "redirect:/boardDetail?boardNo="+boardNo;
	}
	
	// 댓글 입력
	@PostMapping("/addComment")
	public String addComment(HttpSession session, Comment comment) {
		// 로그인 안되있을때
		if(session.getAttribute("loginMember")==null) {
			return "redirect:/";
		}
		System.out.println(comment + " <--addComment");
		commentService.addComment(comment);
		return "redirect:/boardDetail?boardNo="+comment.getBoardNo();
	}
}
