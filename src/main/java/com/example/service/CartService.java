package com.example.service;

import java.util.List;

import com.example.entity.Cart;
import com.example.model.CartItem;

public interface CartService {

	public void registerCart(Cart cart);
	
	void updateQuantity(Cart cart);
	
	List<CartItem> findByUserId(Integer userId);
	
	void deleteByGoodsId(Integer userId, Integer goodsId);
	
	int getTotalQuantity(Integer userId);
	
	void deleteAllByUserId(Integer userId);
	
	int getTotalAmount(Integer userId);
	
	Cart getTargetCart(Integer userId, Integer goodsId);
	
	List<CartItem> getActiveItemsInCart(Integer userId);
}
