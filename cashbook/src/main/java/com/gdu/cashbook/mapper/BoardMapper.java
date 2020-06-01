package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Page;

@Mapper
public interface BoardMapper {
	// 답글에 답글일때 순서 update	
	public int updateGroupOrderRe(Board board);
	// origin_no, group_order, group_layer 수정
	public int updateGroupOrder(Board board);
	// 부모글 groupLayer
	public int selectGroupLayer(int boardNo);
	// 부모글 groupOrder
	public int selectGroupOrder(int boardNo);
	// get max(groupOrder)
	public int selectMaxGroupOrder(int boardNo);
	// 원래글의 originNo
	public int selectOriginNo(int boardNo);
	// 게시글 수정
	public int updateBoard(Board board);
	// 게시글 이미지파일 이름
	public String selectBoardPic(int boardNo);
	// 게시글 삭제
	public int deleteBoard(int boardNo);
	// 회원탈퇴시 게시글 삭제
	public int deleteBoardByMember(String memberId);
	// 이전글 boardNo
	public int selectPrevBoardNo(int boardNo);
	// 다음글 boardNo
	public int selectNextBoardNo(int boardNo);
	// 처음, 마지막 게시글
	public Map<String, Object> selectFistAndLastBoard();
	// 게시글 상세보기
	public Board selectBoardOne(int boardNo);
	// 게시글 원글일때 origin_no, group_order, group_layer 초기화
	public int updateBoardInit();	
	// 게시글 입력
	public int insertBoard(Board board);
	// 전체 행 수
	public int selectBoardTotalRow(String searchWord);
	// 게시글 전체 불러오기
	public List<Board> selectBoardListByPage(Page page);
}
