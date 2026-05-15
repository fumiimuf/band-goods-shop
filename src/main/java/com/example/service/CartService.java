package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.entity.Cart;
import com.example.model.CartItem;

public interface CartService {

	// 現在のカートに同じ商品があるか確認する あり→個数追加 なし→商品追加
	public void addOrUpdateCart(Integer userId, Integer goodsId);
	
	// 新しく商品を登録する(個数1)
	void insert(Cart cart);
	
	// すでにある商品の個数を増やす
	void updateQuantity(Cart cart);
	
	// ログインユーザーのカート内のグッズをすべて取得
	List<CartItem> findByUserId(Integer userId);
	
	//ログインしているユーザーの特定のグッズを削除する
	void deleteByGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
	
	// カートの合計金額の計算
	int calculateTotalAmount(List<CartItem> cartlist);
	
	// ログインユーザーのカート内商品の合計個数を取得
	int getTotalQuantity(Integer userId);
}
