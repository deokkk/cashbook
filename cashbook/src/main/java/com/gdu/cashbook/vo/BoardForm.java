package com.gdu.cashbook.vo;

import org.springframework.web.multipart.MultipartFile;

public class BoardForm {
	private int boardNo;
	private String boardTitle;
	private String memberId;
	private String boardDate;
	private String boardContent;
	private MultipartFile boardPic;
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getBoardTitle() {
		return boardTitle;
	}
	public void setBoardTitle(String boardTitle) {
		this.boardTitle = boardTitle;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getBoardDate() {
		return boardDate;
	}
	public void setBoardDate(String boardDate) {
		this.boardDate = boardDate;
	}
	public String getBoardContent() {
		return boardContent;
	}
	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}
	public MultipartFile getBoardPic() {
		return boardPic;
	}
	public void setBoardPic(MultipartFile boardPic) {
		this.boardPic = boardPic;
	}
	@Override
	public String toString() {
		return "BoardForm [boardNo=" + boardNo + ", boardTitle=" + boardTitle + ", memberId=" + memberId
				+ ", boardDate=" + boardDate + ", boardContent=" + boardContent + ", boardPic=" + boardPic + "]";
	}
}
