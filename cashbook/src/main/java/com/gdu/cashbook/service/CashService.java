package com.gdu.cashbook.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdu.cashbook.mapper.CashMapper;
import com.gdu.cashbook.mapper.CategoryMapper;
import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.Category;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.MonthAndPrice;

@Service
@Transactional
public class CashService {
	@Autowired private CashMapper cashMapper;
	@Autowired private CategoryMapper categoryMapper;
	// 연 수입/지출 총합
	public int getYearTotalPrice(String memberId, int year) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		return cashMapper.selectYearTotalPrice(map);
	}
	
	// 월별 수입/지출 총합 리스트
	public List<MonthAndPrice> getMonthAndPriceList(String memberId, int year) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		return cashMapper.selectMonthAndPriceList(map);
	}
	
	// 월 수입/지출 총합
	public int getMonthTotalPrice(String memberId, int year, int month) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		map.put("month", month);
		return cashMapper.selectMonthTotalPrice(map);
	}
	
	// 일별 수입/지출 총합 리스트
	public List<DayAndPrice> getDayAndPriceList(String memberId, int year, int month) {
		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("year", year);
		map.put("month", month);
		return cashMapper.selectDayAndPriceList(map);
	}
	
	// 가계부 수정
	public int modifyCash(Cash cash) {
		return cashMapper.updateCash(cash);
	}
	
	// 가계부 1개 선택
	public Cash getCashOne(int cashNo) {
		return cashMapper.selectCashOne(cashNo);
	}
	
	// 카테고리 리스트
	public List<Category> getCategoryList() {
		return categoryMapper.selectCategoryList();
	}
	
	// 가계부 입력
	public int addCash(Cash cash) {
		return cashMapper.insertCash(cash);
	}
	
	// 가계부 삭제
	public int removeCash(int cashNo) {
		return cashMapper.deleteCash(cashNo);
	}
	
	// 가계부 리스트
	public Map<String, Object> getCashListByDate(Cash cash) {
		List<Cash> cashList =  cashMapper.selectCashListByDate(cash);
		int cashKindSum = cashMapper.selectCashKindSum(cash);
		System.out.println(cashKindSum + " <---CashService.getCashListByDate cashKindSum");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("cashList", cashList);
		map.put("cashKindSum", cashKindSum);
		return map;
	}
}
