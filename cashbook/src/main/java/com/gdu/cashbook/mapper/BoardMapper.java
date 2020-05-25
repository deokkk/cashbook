package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Board;
import com.gdu.cashbook.vo.Page;

@Mapper
public interface BoardMapper {
	// 전체 행 수
	public int selectBoardTotalRow();
	// 게시글 전체 불러오기
	public List<Board> selectBoardListByPage(Page page);
}
