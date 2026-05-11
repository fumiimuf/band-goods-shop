package com.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Goods;
import com.example.repository.GoodsMapper;
import com.example.service.GoodsService;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public List<Goods> findByPage(int page, int size) {
		int offset = page * size;
		return goodsMapper.findByPage(size, offset);
	}

	@Override
	public long count() {
		return goodsMapper.count();
	}

	@Override
	public Goods findById(int id) {
		// Mapperを呼び出して1件取得
		return goodsMapper.findById(id);
	}
	
	
}
