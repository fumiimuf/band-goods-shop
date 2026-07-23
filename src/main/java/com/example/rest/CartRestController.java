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
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {

	private final CartService cartService;
	
	private final GoodsService goodsService;
	
	private final MessageSource messageSource;
	
	private final ModelMapper modelMapper;
	
	// カート追加処理
	@PostMapping("/add")
	public ResponseEntity<Map<String, Object>> addCart(
			@RequestParam Integer goodsId, 
			@AuthenticationPrincipal LoginUser loginUser, 
			Locale locale) {
		
		Map<String, Object> response = new HashMap<>();
		
		if (!goodsService.isAvailableGoods(goodsId)) {
			String message = messageSource.getMessage("msg.goods.noGoods", null, locale);
			
			response.put("success", false);
			response.put("message", message);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Integer userId = loginUser.getUserId();
		
		// 該当商品がカートにあるのか確認するためのカート情報を取得
		Cart existingCart = cartService.getCartByUserAndGoods(userId, goodsId);
		
		if (existingCart != null && existingCart.getQuantity() >= 10) {
			String errorMessage = messageSource.getMessage("toast.goods.cartLimitError", null, locale);
			
			response.put("success", false);
			response.put("message", errorMessage);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		if (existingCart == null) {
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			newCart.setGoodsId(goodsId);
			newCart.setQuantity(1);
			
			cartService.registerCart(newCart);
		} else {
			existingCart.setQuantity(existingCart.getQuantity() + 1);
			cartService.updateQuantity(existingCart);
		}
		
		String successMessage = messageSource.getMessage("toast.goods.addCartSuccess", null, locale);
		
		int totalQuantity = cartService.getTotalQuantity(userId);
		
		response.put("success", true);
		response.put("message", successMessage);
		response.put("newCartCount", totalQuantity);
		
		return ResponseEntity.ok(response);
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
			String errorMessage = messageSource.getMessage("toast.cart.quantityError", null, locale);
			
			response.put("success", false);
			response.put("message", errorMessage);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Integer userId = loginUser.getUserId();
		
		Cart existingCart = cartService.getCartByUserAndGoods(userId, form.getGoodsId());
		if (existingCart == null) {
			String errorMessage = messageSource.getMessage("toast.cart.noGoodsId", null, locale);
			response.put("success", false);
			response.put("message", errorMessage);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Cart cart = modelMapper.map(form, Cart.class);
		
		
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
