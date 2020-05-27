package com.gdu.cashbook.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gdu.cashbook.mapper.BoardMapper;
import com.gdu.cashbook.mapper.CommentMapper;
import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.BoardForm;
import com.gdu.cashbook.vo.Page;

@Service
public class BoardService {
	@Autowired private BoardMapper boardMapper;
	@Autowired private CommentMapper commentMapper;
	
	@Value("D:\\sts-deok\\maven.1590373000896\\cashbook\\src\\main\\resources\\static\\upload\\board\\")
	private String path;
	// 게시글 수정
	public int modifyBoard(BoardForm boardForm) {
		String originBoardPic = boardMapper.selectBoardPic(boardForm.getBoardNo());
		MultipartFile mf = boardForm.getBoardPic();
		String originName = mf.getOriginalFilename();
		System.out.println(originName + " <-originName");
		String boardPic = null;
		if(!originName.equals("")) {
			File originFile = new File(path+originBoardPic);
			if(originFile.exists()) {
				originFile.delete();
			}
			int lastDot = originName.lastIndexOf(".");
			String extension = originName.substring(lastDot);
			System.out.println(extension);
			// 사진파일 랜덤문자열로 추가
			UUID uuid = UUID.randomUUID();
			System.out.println(uuid);
			boardPic = boardForm.getMemberId()+uuid+extension;
			System.out.println(boardPic);
		}
		// BoardForm -> Board
		Board board = new Board();
		//title content pic no
		board.setBoardContent(boardForm.getBoardContent());
		board.setBoardNo(boardForm.getBoardNo());
		board.setBoardPic(boardPic);
		board.setBoardTitle(boardForm.getBoardTitle());
		System.out.println(board);
		int row = boardMapper.updateBoard(board);
		
		if(!originName.equals("")) {
			// 물리적으로 저장
			File file = new File(path+boardPic);
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		return row;
	}
	
	// 수정할 게시글
	public Board getModifyBoard(int boardNo) {
		return boardMapper.selectBoardOne(boardNo);
	}
	
	// 게시글 삭제
	public int removeBoard(int boardNo) {
		commentMapper.deleteCommentByBoard(boardNo);
		int result = boardMapper.deleteBoard(boardNo);
		String boardPic = boardMapper.selectBoardPic(boardNo);
		File file = new File(path+boardPic);
		// 테이블에서 게시글이 삭제됬고 파일이 물리 저장소에 존재하면
		if(result==1 && file.exists()) {
			file.delete();
		}
		return result;
	}
	
	// 게시글 상세보기
	public Map<String, Object> getBoardOne(int boardNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("board", boardMapper.selectBoardOne(boardNo));
		// 처음 게시물, 마지막 게시물 번호
		Map<String, Object> boardNoMap = boardMapper.selectFistAndLastBoard();
		map.put("firstBoardNo", boardNoMap.get("minBoardNo"));
		map.put("lastBoardNo", boardNoMap.get("maxBoardNo"));
		map.put("nextBoardNo", boardMapper.selectNextBoardNo(boardNo));
		map.put("prevBoardNo", boardMapper.selectPrevBoardNo(boardNo));
		return map;
	}
	
	// 게시글 입력
	public int addBoard(BoardForm boardForm) {
		MultipartFile mf = boardForm.getBoardPic();
		String originName = mf.getOriginalFilename();
		System.out.println(originName);
		String boardPic = null;
		if(!originName.equals("")) {
			int lastDot = originName.lastIndexOf(".");
			String extension = originName.substring(lastDot);
			System.out.println(extension);
			// 사진파일 랜덤문자열로 추가
			UUID uuid = UUID.randomUUID();
			System.out.println(uuid);
			boardPic = boardForm.getMemberId()+uuid+extension;
			System.out.println(boardPic);
		}
		
		// boardForm -> board
		Board board = new Board();
		board.setBoardTitle(boardForm.getBoardTitle());
		board.setBoardContent(boardForm.getBoardContent());
		board.setMemberId(boardForm.getMemberId());
		board.setBoardPic(boardPic);
		System.out.println(board + " <--boardService.addBoard board");
		
		int row = boardMapper.insertBoard(board);
		if(!originName.equals("")) {
			// file 저장
			System.out.println(path+boardPic + " <-- 저장경로");
			File file = new File(path+boardPic);
			try {
				mf.transferTo(file);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		return row;
	}
	// 게시글 전체 불러오기
	public Map<String, Object> getBoardListByPage(int currentPage, String searchWord) {
		Page page = new Page();
		int rowPerPage = 10;	// 페이지당 행수
		int beginRow = (currentPage-1)*rowPerPage;
		page.setBeginRow(beginRow);
		page.setRowPerPage(rowPerPage);
		page.setSearchWord(searchWord);
		page.setCurrentPage(currentPage);
		
		List<Board> boardList = boardMapper.selectBoardListByPage(page);
		int totalRow = boardMapper.selectBoardTotalRow(searchWord);
		int lastPage = totalRow%rowPerPage!=0 ? totalRow/rowPerPage+1 : totalRow/rowPerPage;
		page.setLastPage(lastPage);
		
		int pagePerGroup = 5; // 몇페이지씩 그룹
		page.setPagePerGroup(pagePerGroup);
		
		int currentPageGroup = (currentPage-1)%pagePerGroup==0 ? currentPage : (currentPage-1)/pagePerGroup*pagePerGroup+1;
		page.setCurrentPageGroup(currentPageGroup);
		
		int lastPageGroup = lastPage%pagePerGroup!=0 ? lastPage/pagePerGroup+1 : lastPage/pagePerGroup;
		page.setLastPageGroup(lastPageGroup);
		
		Map<String, Object> map = new HashMap<>();
		map.put("page", page);
		map.put("boardList", boardList);
		
		return map;
	}
}
