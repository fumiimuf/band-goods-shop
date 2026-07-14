package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;
import com.example.model.Pagination;

public interface OrderService {

	void createOrder(User user, List<CartItem> cartList, int totalAmount);
	
	List<OrderViewItem> getOrderHistoryByPage(Integer userId, int page, int size);
	
	long countByUserId(Integer userId);
	
	List<OrderViewItem> getAllOrderHistoryByPage(String keyword, int page, int size);
	
	long countAllOrders(String keyword);
	
	Pagination<OrderViewItem> getOrderPage(Integer userId, int page);
	
	Pagination<OrderViewItem> getAdminOrderPage(String keyword, int page);
}
