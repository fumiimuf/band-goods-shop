package com.example.service;

import java.util.List;

import com.example.entity.Goods;
import com.example.model.GoodsItem;

public interface GoodsService {

	List<GoodsItem> findByPage(boolean isDeleted, String keyword, int page, int size);
	
	long count(boolean isDeleted, String keyword);
	
	GoodsItem findById(int id);
	
	void registerGoods(Goods goods);
	
	void updateGoods(Goods goods);
	
}
