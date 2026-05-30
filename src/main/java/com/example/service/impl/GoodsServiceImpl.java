package com.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Goods;
import com.example.model.GoodsItem;
import com.example.repository.GoodsMapper;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

	private final GoodsMapper goodsMapper;

	@Override
	public List<GoodsItem> findByPage(boolean isDeleted, int page, int size) {
		int offset = page * size;
		return goodsMapper.findByPage(isDeleted, size, offset);
	}

	@Override
	public long count(boolean isDeleted) {
		return goodsMapper.count(isDeleted);
	}

	@Override
	public GoodsItem findById(int id) {
		// Mapperを呼び出して1件取得
		return goodsMapper.findById(id);
	}

	@Override
	public void registerGoods(Goods goods) {
		goodsMapper.insert(goods);
	}

	@Override
	public void updateGoods(Goods goods) {
		goodsMapper.update(goods);
		
	}
	
	

}
