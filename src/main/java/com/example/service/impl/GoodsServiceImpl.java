package com.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.model.GoodsItem;
import com.example.repository.GoodsMapper;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoodsServiceImpl implements GoodsService {

	private final GoodsMapper goodsMapper;

	@Override
	public List<GoodsItem> findByPage(int page, int size) {
		int offset = page * size;
		return goodsMapper.findByPage(size, offset);
	}

	@Override
	public long count() {
		return goodsMapper.count();
	}

	@Override
	public GoodsItem findById(int id) {
		// Mapperを呼び出して1件取得
		return goodsMapper.findById(id);
	}

}
