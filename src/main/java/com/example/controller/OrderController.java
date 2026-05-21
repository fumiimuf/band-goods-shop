package com.example.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.config.LoginUser;
import com.example.entity.User;
import com.example.model.CartItem;
import com.example.service.CartService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
	
	private final UserService userService;
	
	private final CartService cartService;

	@GetMapping("/confirm")
	public String getMethodName(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		// ログインユーザー情報を取得
		User user = userService.findById(userId);
		
		// ログインユーザーのカート内商品をすべて取得
		List<CartItem> cartList = cartService.findByUserId(userId);
		
		// 合計金額を取得
		int totalAmount = cartService.calculateTotalAmount(cartList);
		
		// modelに登録
		model.addAttribute("user", user);
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);
		
		return "order/confirm";
	}
	
}
