package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.Category;

@Mapper
public interface CategoryMapper {

	List<Category> findAll();
}
