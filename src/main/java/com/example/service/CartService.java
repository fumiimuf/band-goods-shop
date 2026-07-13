package com.example.service;

import java.util.List;

import com.example.entity.Cart;
import com.example.model.CartItem;

public interface CartService {

	// 現在のカートに同じ商品があるか確認する あり→個数追加 なし→商品追加
	public void addOrUpdateCart(Integer userId, Integer goodsId);
	
	// すでにある商品の個数を増やす
	void updateQuantity(Cart cart);
	
	// ログインユーザーのカート内のグッズをすべて取得
	List<CartItem> findByUserId(Integer userId);
	
	//ログインしているユーザーの特定のグッズを削除する
	void deleteByGoodsId(Integer userId, Integer goodsId);
	
	// ログインユーザーのカート内商品の合計個数を取得
	int getTotalQuantity(Integer userId);
	
	// ログインユーザーのカート内グッズをすべて削除する
	void deleteAllByUserId(Integer userId);
	
	// ユーザーIDに紐づくカート内の合計金額を取得する
	int getTotalAmount(Integer userId);
}
