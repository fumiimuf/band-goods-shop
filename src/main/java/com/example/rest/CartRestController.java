package com.example.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
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
	
	private final MessageSource messageSource;
	
	private final ModelMapper modelMapper;
	
	// カート追加処理
	@PostMapping("/add")
	public Map<String, Integer> addCart(
			@RequestParam Integer goodsId, 
			@AuthenticationPrincipal LoginUser loginUser, 
			Locale locale) {
		
		Map<String, Object> response = new HashMap<>();
		
		Integer userId = loginUser.getUserId();
		
		// 該当商品がカートにあるのか確認するためのカート情報を取得
		Cart existingCart = cartService.getCartByUserAndGoods(userId, goodsId);
		
		// 取得したカートがnullの場合、登録処理して、成功時、メッセージを入れてresponseで返す
		// 取得したカートがあれば、更新処理する。「<10」の条件処理も追加。成功時、メッセージを入れてresponseで返す
		// 10を超えていて失敗時、メッセージを入れてメッセージを入れてresponseで返す
		if (existingCart == null) {
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			newCart.setGoodsId(goodsId);
			newCart.setQuantity(1);
			
			cartService.registerCart(newCart);
			
			// ここにJSファイルに送るmessages.propertiesのキー名を詰めたい。
			String message = messageSource.getMessage("toast.goods.addCartSuccess", null, locale);
			// JSのdoneメソッドに送れるようにしたい。
			response.put("success", true);
			
			return ResponseEntity.of(response);
			
		} else {
			if(existingCart.getQuantity() >= 10) {
				
			}
			existingCart.setQuantity(existingCart.getQuantity() + 1);
			cartService.updateCart(existingCart);
		}
		
		
		
		
		
		cartService.addOrUpdateCart(userId, goodsId);
		
		int totalQuantity = cartService.getTotalQuantity(userId);
		
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
		
		if (bindingResult.hasErrors()) {
			
			response.put("success", false);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Cart cart = modelMapper.map(form, Cart.class);
		
		Integer userId = loginUser.getUserId();
		cart.setUserId(userId);
		
		cartService.updateQuantity(cart);
		
		List<CartItem> cartList = cartService.findByUserId(userId);
		
		int totalAmount = cartService.getTotalAmount(userId);
		
		response.put("success", true);
		response.put("totalAmount", totalAmount);
		response.put("cartList", cartList);
		
		int totalQuantity = cartService.getTotalQuantity(userId);
		response.put("totalQuantity", totalQuantity);
		
		return ResponseEntity.ok(response);
	}
}
