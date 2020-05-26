package com.gdu.cashbook.vo;

public class Page {
	private int currentPage;
	private int pagePerGroup;
	private int rowPerPage;
	private int beginRow;
	private int totalRow;
	private int lastPage;
	private int currentPageGroup;
	private int lastPageGroup;
	private String searchWord;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPagePerGroup() {
		return pagePerGroup;
	}
	public void setPagePerGroup(int pagePerGroup) {
		this.pagePerGroup = pagePerGroup;
	}
	public int getRowPerPage() {
		return rowPerPage;
	}
	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	public int getBeginRow() {
		return beginRow;
	}
	public void setBeginRow(int beginRow) {
		this.beginRow = beginRow;
	}
	public int getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public int getLastPage() {
		return lastPage;
	}
	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}
	public int getCurrentPageGroup() {
		return currentPageGroup;
	}
	public void setCurrentPageGroup(int currentPageGroup) {
		this.currentPageGroup = currentPageGroup;
	}
	public int getLastPageGroup() {
		return lastPageGroup;
	}
	public void setLastPageGroup(int lastPageGroup) {
		this.lastPageGroup = lastPageGroup;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	@Override
	public String toString() {
		return "Page [currentPage=" + currentPage + ", pagePerGroup=" + pagePerGroup + ", rowPerPage=" + rowPerPage
				+ ", beginRow=" + beginRow + ", totalRow=" + totalRow + ", lastPage=" + lastPage + ", currentPageGroup="
				+ currentPageGroup + ", lastPageGroup=" + lastPageGroup + ", searchWord=" + searchWord + "]";
	}
}
