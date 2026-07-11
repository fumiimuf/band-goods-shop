package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;
import com.example.model.Pagination;

public interface OrderService {

	void createOrder(User user, List<CartItem> cartList, int totalAmount);
	
	// ログインユーザーの注文履歴と明細をページ指定して取得する
	List<OrderViewItem> getOrderHistoryByPage(Integer userId, int page, int size);
	
	// ログインユーザーの注文履歴が全部で何件あるか数える
	long countByUserId(Integer userId);
	
	// 販売履歴を全件取得
	List<OrderViewItem> getAllOrderHistoryByPage(String keyword, int page, int size);
	
	long countAllOrders(String keyword);
	
	Pagination<OrderViewItem> getOrderPage(Integer userId, int page);
	
	Pagination<OrderViewItem> getAdminOrderPage(String keyword, int page);
}
