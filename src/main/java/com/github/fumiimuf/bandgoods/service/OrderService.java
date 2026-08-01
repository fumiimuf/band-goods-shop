package com.github.fumiimuf.bandgoods.service;

import java.util.List;

import com.github.fumiimuf.bandgoods.entity.User;
import com.github.fumiimuf.bandgoods.model.CartItem;
import com.github.fumiimuf.bandgoods.model.OrderViewItem;

public interface OrderService {

	void createOrder(User user, List<CartItem> cartList, int totalAmount);
	
	List<OrderViewItem> getOrderHistoryByPage(Integer userId, int page, int size);
	
	long getOrderCountByUserId(Integer userId);
	
	List<OrderViewItem> getAllOrderHistoryByPage(String keyword, int page, int size);
	
	long getOrderCount(String keyword);
}
