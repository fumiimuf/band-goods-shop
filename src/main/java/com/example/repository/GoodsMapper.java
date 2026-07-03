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
	
	// 指定したIDのグッズを1件だけ取得
	GoodsItem selectById(int id);
	
	// グッズを新しく登録
	void insert(Goods goods);
	
	// 指定したIDのグッズ情報を上書き更新
	void update(Goods goods);
	
}
