package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gdu.cashbook.vo.Comment;
import com.gdu.cashbook.vo.Page;

@Mapper
public interface CommentMapper {
	// 댓글 수정
	public int updateComment(Comment comment);
	// 게시글 삭제시 모든 댓글 삭제
	public int deleteCommentByBoard(int boardNo);
	// 회원탈퇴시 모든 댓글 삭제
	public int deleteCommentByMember(String memberId);
	// 댓글 삭제
	public int deleteComment(int commentNo);
	// 댓글 입력
	public int insertComment(Comment comment);
	// 전체 댓글 행수
	public int selectCommentTotalRowByBoard(int boardNo);
	// 댓글 리스트
	public List<Comment> selectCommentListByPage(@Param("boardNo") int boardNo, @Param("page") Page page);
}
