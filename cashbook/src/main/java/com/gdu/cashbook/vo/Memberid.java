package com.gdu.cashbook.vo;

public class Memberid {
	private int memberidNo;
	private String memberid;
	public int getMemberidNo() {
		return memberidNo;
	}
	public void setMemberidNo(int memberidNo) {
		this.memberidNo = memberidNo;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	@Override
	public String toString() {
		return "Memberid [memberidNo=" + memberidNo + ", memberid=" + memberid + "]";
	}
}
