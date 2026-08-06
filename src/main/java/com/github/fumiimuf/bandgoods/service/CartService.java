package com.github.fumiimuf.bandgoods.service;

import java.util.List;

import com.github.fumiimuf.bandgoods.entity.Cart;
import com.github.fumiimuf.bandgoods.model.CartItem;

public interface CartService {

	public void registerCart(Cart cart);
	
	void updateQuantity(Cart cart);
	
	List<CartItem> findByUserId(Integer userId);
	
	void deleteByGoodsId(Integer userId, Integer goodsId);
	
	int getTotalQuantity(Integer userId);
	
	void deleteAllByUserId(Integer userId);
	
	int getTotalAmount(Integer userId);
	
	Cart getCart(Integer userId, Integer goodsId);
	
}
