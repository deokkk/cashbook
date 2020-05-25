package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Page;

@Service
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	
	// 전체 행 수
	public int getBoardTotlaRow() {
		return boardMapper.selectBoardTotalRow();
	}
	
	// 게시글 전체 불러오기
	public Map<String, Object> getBoardListByPage(int currentPage) {
		Page page = new Page();
		int rowPerPage = 1;	// 페이지당 행수
		int beginRow = (currentPage-1)*rowPerPage;
		page.setBeginRow(beginRow);
		page.setRowPerPage(rowPerPage);
		List<Board> boardList = boardMapper.selectBoardListByPage(page);
		int totalRow = boardMapper.selectBoardTotalRow();
		page.setCurrentPage(currentPage);
		page.setTotalRow(totalRow);
		int lastPage = totalRow%rowPerPage!=0 ? totalRow/rowPerPage+1 : totalRow/rowPerPage;
		page.setLastPage(lastPage);
		int pagePerGroup = 1; // 몇페이지씩 그룹
		page.setPagePerGroup(pagePerGroup);
		int currentPageGroup = (currentPage-1)%pagePerGroup==0 ? currentPage : (currentPage-1/pagePerGroup)/pagePerGroup+1;
		page.setCurrentPageGroup(currentPageGroup);
		int lastPageGroup = lastPage%pagePerGroup!=0 ? lastPage/pagePerGroup+1 : lastPage/pagePerGroup;
		page.setLastPageGroup(lastPageGroup);
		
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("boardList", boardList);
		
		return map;
	}
}
