package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.CartItem;

public interface OrderService {

	void createOrder(User user, List<CartItem> cartList, int totalAmount);
}
