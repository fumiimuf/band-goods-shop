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
	
	// カート追加処理
	@PostMapping("/add")
	public Map<String, Integer> addCart(@RequestParam Integer goodsId, 
			@AuthenticationPrincipal LoginUser loginUser) {
		
		Integer userId = loginUser.getUserId();
		
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
