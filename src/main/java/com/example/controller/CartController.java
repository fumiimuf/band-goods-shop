package com.example.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.config.LoginUser;
import com.example.model.CartItem;
import com.example.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	// カート内容表示
	@GetMapping("/index")
	public String showCartIndex(@AuthenticationPrincipal LoginUser loginUser, Model model) {

		//ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();

		// DBからそのユーザーのカート内商品をすべて取得
		List<CartItem> cartList = cartService.findByUserId(userId);

		// 合計金額を取得する
		int totalAmount = cartService.getTotalAmount(userId);

		// HTMLに渡すデータを登録
		model.addAttribute("loginUserName", loginUser.getName());
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);

		return "cart/index";
	}

	// カート内の特定のグッズを削除
	@PostMapping("/delete")
	public String deleteByGoodsId(
			@RequestParam Integer goodsId,
			@AuthenticationPrincipal LoginUser loginUser) {

		// ログインユーザーのIDを使って、特定のグッズを削除
		cartService.deleteByGoodsId(loginUser.getUserId(), goodsId);

		// 削除後はカート内容画面へリダイレクト
		return "redirect:/cart/index";
	}

	@PostMapping("/clear")
	public String clearCart(@AuthenticationPrincipal LoginUser loginUser) {

		// ログインユーザーのIDを使って、カートを一括削除
		cartService.deleteAllByUserId(loginUser.getUserId());

		return "redirect:/cart/index";
	}

}
