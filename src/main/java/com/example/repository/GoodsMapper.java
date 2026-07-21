package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Goods;
import com.example.model.GoodsItem;

@Mapper
public interface GoodsMapper {

	List<GoodsItem> selectByPage(
					@Param("isDeleted") boolean isDeleted,
					@Param("keyword") String keyword,
					@Param("limit") int limit, 
					@Param("offset") int offset);
	
	long selectCount(@Param("isDeleted") boolean isDeleted, @Param("keyword") String keyword);
	
	GoodsItem selectById(int id);
	
	void insert(Goods goods);
	
	void update(Goods goods);
	
	Goods findActiveGoodsById(Integer goodsId);
}
