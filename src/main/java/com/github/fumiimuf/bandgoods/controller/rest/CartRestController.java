package com.github.fumiimuf.bandgoods.controller.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.fumiimuf.bandgoods.entity.Cart;
import com.github.fumiimuf.bandgoods.form.CartForm;
import com.github.fumiimuf.bandgoods.model.CartItem;
import com.github.fumiimuf.bandgoods.model.LoginUser;
import com.github.fumiimuf.bandgoods.service.CartService;
import com.github.fumiimuf.bandgoods.service.GoodsService;

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
			String errorMessage = messageSource.getMessage("msg.error.goods.notfound", null, locale);
			
			response.put("success", false);
			response.put("errorMessage", errorMessage);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Integer userId = loginUser.getUserId();
		
		// 該当商品がカートにあるのか確認するためのカート情報を取得
		Cart targetCart = cartService.getCart(userId, goodsId);
		
		if (targetCart != null && targetCart.getQuantity() >= 10) {
			String errorMessage = messageSource.getMessage("msg.error.cart.max.quantity", null, locale);
			
			response.put("success", false);
			response.put("errorMessage", errorMessage);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		if (targetCart == null) {
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			newCart.setGoodsId(goodsId);
			newCart.setQuantity(1);
			
			cartService.registerCart(newCart);
		} else {
			targetCart.setQuantity(targetCart.getQuantity() + 1);
			cartService.updateQuantity(targetCart);
		}
		
		String successMessage = messageSource.getMessage("msg.success.cart.add", null, locale);
		
		int totalQuantity = cartService.getTotalQuantity(userId);
		
		response.put("success", true);
		response.put("successMessage", successMessage);
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
			
			String errorMessage = messageSource.getMessage("msg.error.cart.update.quantity", null, locale);
			
			FieldError fieldError = bindingResult.getFieldError();
		    String fieldErrorMessage = messageSource.getMessage(fieldError, locale);
			
			response.put("success", false);
			response.put("errorMessage", errorMessage);
			response.put("fieldError", fieldErrorMessage);
			
			return ResponseEntity.badRequest().body(response);
		}
		
		Integer userId = loginUser.getUserId();
		
		Cart targetCart = cartService.getCart(userId, form.getGoodsId());
		
		if (targetCart == null) {
			String errorMessage = messageSource.getMessage("msg.error.goods.notfound", null, locale);
			response.put("success", false);
			response.put("errorMessage", errorMessage);
			
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
