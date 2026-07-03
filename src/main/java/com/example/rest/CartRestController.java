package com.example.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.config.LoginUser;
import com.example.entity.Cart;
import com.example.form.CartForm;
import com.example.model.CartItem;
import com.example.service.CartService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {

	private final CartService cartService;
	private final ModelMapper modelMapper;
	
	// カート追加
	@PostMapping("/add")
	public Map<String, Integer> addCart(@RequestParam Integer goodsId, 
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
	
	// カート内グッズの数量を更新
	@PostMapping("/update-quantity")
	public ResponseEntity<Map<String, Object>> updateQuantity(
			@Validated CartForm form,
			BindingResult bindingResult,
			@AuthenticationPrincipal LoginUser loginUser, 
			Locale locale) {
		
		Map<String, Object> response = new HashMap<>();
		
		// バリデーションエラーのチェック
		if (bindingResult.hasErrors()) {
			
			response.put("success", false);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		// formをCartクラスに変換
		Cart cart = modelMapper.map(form, Cart.class);
		
		// データベース更新
		Integer userId = loginUser.getUserId();
		cart.setUserId(userId);
		
		// Serviceを呼び出してDBの個数をUPDATE
		cartService.updateQuantity(cart);
		
		List<CartItem> cartList = cartService.findByUserId(userId);
		
		// ユーザーIDに紐づくカート内の合計金額を取得する
		int totalAmount = cartService.getTotalAmount(userId);
		
		// レスポンスデータを詰める
		response.put("success", true);
		response.put("totalAmount", totalAmount);
		response.put("cartList", cartList);
		
		// 最新カート内「グッズ合計個数」を取得する
		int totalQuantity = cartService.getTotalQuantity(userId);
		response.put("totalQuantity", totalQuantity);
		
		return ResponseEntity.ok(response);
	}
	
}
