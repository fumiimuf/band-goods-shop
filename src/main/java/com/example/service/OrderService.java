package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;

public interface OrderService {

	void createOrder(User user, List<CartItem> cartList, int totalAmount);
	
	// 購入履歴と明細を取得
	List<OrderViewItem> getOrderHistory(Integer userId);
}
