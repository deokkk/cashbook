package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;

@Mapper
public interface CashMapper {
	// 로그인한 사용자의 오늘날짜 가계부 리스트
	public List<Cash> selectCashListByDate(Cash cash);
}
