package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook.mapper.CommentMapper;
import com.gdu.cashbook.vo.Comment;
import com.gdu.cashbook.vo.Page;

@Service
public class CommentService {
	@Autowired CommentMapper commentMapper;
	
	// 댓글 수정
	public int modifyComment(Comment comment) {
		return commentMapper.updateComment(comment);
	}
	
	// 댓글 삭제
	public int removeComment(int commentNo) {
		return commentMapper.deleteComment(commentNo);
	}
	
	// 댓글 입력
	public int addComment(Comment comment) {
		return commentMapper.insertComment(comment);
	}
	
	// 댓글 리스트
	public Map<String, Object> getCommentListByPage(int boardNo, int currentPage) {
		Page page = new Page();
		int rowPerPage = 5; // 페이지당 행 수
		int beginRow = (currentPage-1)*rowPerPage; // 시작 행
		page.setBeginRow(beginRow);
		page.setCurrentPage(currentPage);
		page.setRowPerPage(rowPerPage);
		
		List<Comment> commentList = commentMapper.selectCommentListByPage(boardNo, page);
		int totalRow = commentMapper.selectCommentTotalRowByBoard(boardNo); // 전체  행 수
		int lastPage = totalRow%rowPerPage!=0 ? totalRow/rowPerPage+1 : totalRow/rowPerPage; // 마지막 페이지
		page.setLastPage(lastPage);
		
		int pagePerGroup = 5; // 한 그룹당 페이지 수
		page.setPagePerGroup(pagePerGroup);
		
		int currentPageGroup = (currentPage-1)%pagePerGroup==0 ? currentPage : (currentPage-1)/pagePerGroup*pagePerGroup+1; // 현재 페이지 그룹
		page.setCurrentPageGroup(currentPageGroup);
		
		int lastPageGroup = lastPage%pagePerGroup!=0 ? lastPage/pagePerGroup+1 : lastPage/pagePerGroup; // 마지막 페이지 그룹
		page.setLastPageGroup(lastPageGroup);
		
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("commentList", commentList);
		
		return map;
	}
}
