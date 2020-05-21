package com.gdu.cashbook.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Cash;
import com.gdu.cashbook.vo.DayAndPrice;
import com.gdu.cashbook.vo.MonthAndPrice;

@Mapper
public interface CashMapper {
	// 월별 수입/지출 총합 리스트
	public List<MonthAndPrice> selectMonthAndPriceList(Map<String, Object> month);
	// 회원 탈퇴시 그 memberId로 작성된 가계부 전부 삭제
	public int deleteCashByMember(String memberId);
	// 월 수입/지출 총합
	public int selectMonthTotalPrice(Map<String, Object> month);
	// 일별 수입/지출 총합 리스트
	public List<DayAndPrice> selectDayAndPriceList(Map<String, Object> date); 
	// 가계부 수정
	public int updateCash(Cash cash);
	// 가계부 1개 선택
	public Cash selectCashOne(int cashNo);
	// 가계부 입력
	public int insertCash(Cash cash);
	// 가게부 삭제
	public int deleteCash(int cashNo);
	// 수입/지출 총합
	public int selectCashKindSum(Cash cash);
	// 로그인한 사용자의 오늘날짜 가계부 리스트
	public List<Cash> selectCashListByDate(Cash cash);
}
