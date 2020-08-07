package com.gdu.cashbook.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdu.cashbook.mapper.CategoryMapper;
import com.gdu.cashbook.vo.Category;

@Service
public class CategoryService {
	@Autowired private CategoryMapper categoryMapper;
	public int addCategory(String categoryName) {
		List<Category> list = categoryMapper.selectCategoryList();
		boolean flag = true;
		for(Category c : list) {
			if(c.getCategoryName().equals(categoryName)) { 
				flag = false;
			}
		}
		int row = 0;
		if(flag) { // 입력받은 카테고리가 리스트에 없을때
			row = categoryMapper.insertCategory(categoryName);
		}
		return row;
	}
}
