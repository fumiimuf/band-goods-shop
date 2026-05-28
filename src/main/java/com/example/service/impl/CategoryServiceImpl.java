package com.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Category;
import com.example.repository.CategoryMapper;
import com.example.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryMapper categoryMapper;

	@Override
	public List<Category> getAllCategories() {
		return categoryMapper.findAll();
	}
	
	
}
