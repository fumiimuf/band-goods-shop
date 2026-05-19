package com.example.service;

import java.util.List;

import com.example.entity.Goods;

public interface GoodsService {

	List<Goods> findByPage(int page, int size);
	
	long count();
	
	Goods findById(int id);
	
}
