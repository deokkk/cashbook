package com.gdu.cashbook.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gdu.cashbook.vo.Category;

@Mapper
public interface CategoryMapper {
	// 카테고리 목록 가져오기
	public List<Category> selectCategoryList();
}
