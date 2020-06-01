package com.gdu.cashbook.vo;

public class Board {
	private int boardNo;
	private int originNo;
	private int groupOrder;
	private int groupLayer;
	private String boardTitle;
	private String memberId;
	private String boardDate;
	private String boardContent;
	private String boardPic;
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public int getOriginNo() {
		return originNo;
	}
	public void setOriginNo(int originNo) {
		this.originNo = originNo;
	}
	public int getGroupOrder() {
		return groupOrder;
	}
	public void setGroupOrder(int groupOrder) {
		this.groupOrder = groupOrder;
	}
	public int getGroupLayer() {
		return groupLayer;
	}
	public void setGroupLayer(int groupLayer) {
		this.groupLayer = groupLayer;
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
	public String getBoardPic() {
		return boardPic;
	}
	public void setBoardPic(String boardPic) {
		this.boardPic = boardPic;
	}
	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", originNo=" + originNo + ", groupOrder=" + groupOrder + ", groupLayer="
				+ groupLayer + ", boardTitle=" + boardTitle + ", memberId=" + memberId + ", boardDate=" + boardDate
				+ ", boardContent=" + boardContent + ", boardPic=" + boardPic + "]";
	}
}
