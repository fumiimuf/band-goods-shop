package com.example.service;

import java.util.List;

import com.example.entity.Cart;
import com.example.model.CartItem;

public interface CartService {

	public void addOrUpdateCart(Integer userId, Integer goodsId);
	
	void updateQuantity(Cart cart);
	
	List<CartItem> findByUserId(Integer userId);
	
	void deleteByGoodsId(Integer userId, Integer goodsId);
	
	int getTotalQuantity(Integer userId);
	
	void deleteAllByUserId(Integer userId);
	
	int getTotalAmount(Integer userId);
}
