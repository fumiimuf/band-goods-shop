package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.entity.Cart;
import com.example.model.CartItem;

public interface CartService {

	// 現在のカートに同じ商品があるか確認する
		Cart findByGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
		
		// 新しく商品を登録する(個数1)
		void insert(Cart cart);
		
		// すでにある商品の個数を増やす
		void updateQuantity(Cart cart);
		
		// 【新しく追加】ユーザーIDを指定して、その人のカート情報をすべて取得する
	    // 戻り値は「商品名や価格」も含んだ CartItem のリストにします
		List<CartItem> findByUserId(@Param("userId") Integer userId);
		
		//ログインしているユーザーの特定のグッズを削除する
		void deleteByGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}
