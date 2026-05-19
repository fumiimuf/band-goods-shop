package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Goods;

@Mapper
public interface GoodsMapper {

	List<Goods> findByPage(@Param("limit") int limit, @Param("offset") int offset);
	
	long count();
	
	// 指定したIDのグッズを1件だけ取得
	Goods findById(int id);
	
}
