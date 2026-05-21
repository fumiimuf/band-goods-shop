package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.model.GoodsItem;

@Mapper
public interface GoodsMapper {

	List<GoodsItem> findByPage(@Param("limit") int limit, @Param("offset") int offset);
	
	long count();
	
	// 指定したIDのグッズを1件だけ取得
	GoodsItem findById(int id);
	
}
