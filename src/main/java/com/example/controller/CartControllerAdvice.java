package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.config.LoginUser;
import com.example.service.CartService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class CartControllerAdvice {

	private final CartService cartService;
	
	@ModelAttribute   // 全画面共通のデータを見つけて実行する
	public void addCartCountToModel(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		int cartCount = 0;
		
		if (loginUser!= null) {
			cartCount = cartService.getTotalQuantity(loginUser.getUserId());
		}
		
		model.addAttribute("cartCount", cartCount);
	}
}
