package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.config.LoginUser;
import com.example.entity.User;
import com.example.service.CartService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class LayoutControllerAdvice {

	private final CartService cartService;
	private final UserService userService;
	
	@ModelAttribute   // 全画面共通のデータを見つけて実行する
	public void addCommonDataToModel(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		int cartCount = 0;
		
		if (loginUser!= null) {
			cartCount = cartService.getTotalQuantity(loginUser.getUserId());
			
			User latestUser = userService.findById(loginUser.getUserId());
			model.addAttribute("latestUser", latestUser);
		}
		
		model.addAttribute("cartCount", cartCount);
	}
}
