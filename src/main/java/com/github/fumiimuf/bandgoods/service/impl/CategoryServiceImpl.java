package com.github.fumiimuf.bandgoods.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.fumiimuf.bandgoods.entity.Category;
import com.github.fumiimuf.bandgoods.repository.CategoryMapper;
import com.github.fumiimuf.bandgoods.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	@Override
	public List<Category> getAllCategories() {
		return categoryMapper.selectAll();
	}
}
