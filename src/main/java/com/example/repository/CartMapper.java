package com.example.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Cart;

@Mapper
public interface CartMapper {

	// 現在のカートに同じ商品があるか確認する
	Cart findByGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
	
	// 新しく商品を登録する(個数1)
	void insert(Cart cart);
	
	// すでにある商品の個数を増やす
	void updateQuantity(Cart cart);
}
