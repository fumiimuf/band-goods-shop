package com.example.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.LoginUser;
import com.example.service.CartService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {

	private final CartService cartService;
	
	@PostMapping("/add")
	public Map<String, Integer> addCart(@RequestParam("goodsId") Integer goodsId, 
			@AuthenticationPrincipal LoginUser loginUser) {
		
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// 現在のカードに同じ商品があるか確認。あり→個数を更新 なし→商品追加
		cartService.addOrUpdateCart(userId, goodsId);
		
		// 最新のカート内「合計個数」を取得
		int totalQuantity = cartService.getTotalQuantity(userId);
		
		// JavaScript側に「newCartCount」という名前で個数を返却
		Map<String, Integer> response = new HashMap<>();
		response.put("newCartCount", totalQuantity);
				
		return response;
	}
	
}
