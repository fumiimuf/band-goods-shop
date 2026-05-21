package com.example.service;

import java.util.List;

import com.example.model.GoodsItem;

public interface GoodsService {

	List<GoodsItem> findByPage(int page, int size);
	
	long count();
	
	GoodsItem findById(int id);
	
}
