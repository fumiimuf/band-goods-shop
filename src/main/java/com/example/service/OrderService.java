package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;

public interface OrderService {

	void createOrder(User user, List<CartItem> cartList, int totalAmount);
	
	List<OrderViewItem> getOrderHistoryByPage(Integer userId, int page, int size);
	
	long getOrderCountByUserId(Integer userId);
	
	List<OrderViewItem> getAllOrderHistoryByPage(String keyword, int page, int size);
	
	long countAllOrders(String keyword);
}
