package com.example.service;

import java.util.List;

import com.example.entity.Goods;
import com.example.model.GoodsItem;

public interface GoodsService {

	List<GoodsItem> findByPage(boolean isDeleted, int page, int size);
	
	long count(boolean isDeleted);
	
	GoodsItem findById(int id);
	
	void registerGoods(Goods goods);
	
}
