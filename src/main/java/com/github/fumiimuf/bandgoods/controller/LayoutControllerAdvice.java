package com.github.fumiimuf.bandgoods.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.github.fumiimuf.bandgoods.model.LoginUser;
import com.github.fumiimuf.bandgoods.service.CartService;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class LayoutControllerAdvice {

	private final CartService cartService;

	@ModelAttribute
	public void addCommonDataToModel(@AuthenticationPrincipal LoginUser loginUser, Model model) {

		int cartCount = 0;

		if (loginUser != null) {
			cartCount = cartService.getTotalQuantity(loginUser.getUserId());
			
		}
		model.addAttribute("cartCount", cartCount);
	}
}
