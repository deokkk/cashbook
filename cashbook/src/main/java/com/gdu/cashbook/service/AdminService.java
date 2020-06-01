package com.gdu.cashbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook.mapper.AdminMapper;

@Service
public class AdminService {
	@Autowired private AdminMapper adminMapper;
	
	// 관리자로 변경
	public int addAdmin(String memberId) {
		return adminMapper.insertAdmin(memberId);
	}
	
	// 관리자 계정인지 확인
	public String getAdmin(String memberId) {
		return adminMapper.selectAdmin(memberId);
	}
}
