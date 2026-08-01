package com.github.fumiimuf.bandgoods.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.fumiimuf.bandgoods.entity.Category;

@Mapper
public interface CategoryMapper {

	List<Category> selectAll();
}
