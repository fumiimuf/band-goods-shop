package com.github.fumiimuf.bandgoods.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.fumiimuf.bandgoods.entity.Cart;
import com.github.fumiimuf.bandgoods.model.CartItem;
import com.github.fumiimuf.bandgoods.model.LoginUser;
import com.github.fumiimuf.bandgoods.service.CartService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	
	// カート内容表示
	@GetMapping("/index")
	public String showCartIndex(@AuthenticationPrincipal LoginUser loginUser, Model model) {

		Integer userId = loginUser.getUserId();

		List<CartItem> cartList = cartService.findByUserId(userId);

		int totalAmount = cartService.getTotalAmount(userId);

		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);

		return "cart/index";
	}

	// カート内の特定のグッズを削除
	@PostMapping("/delete/{goodsId}")
	public String deleteByGoodsId(
			@PathVariable Integer goodsId,
			@AuthenticationPrincipal LoginUser loginUser, 
			RedirectAttributes redirectAttributes) {
		
		
		Integer userId = loginUser.getUserId();
		
		Cart targetCart = cartService.getTargetCart(userId, goodsId);
		
		if (targetCart == null) {
			redirectAttributes.addFlashAttribute("showErrorToast", true);
			
			return "redirect:/cart/index";
		}
		
		cartService.deleteByGoodsId(loginUser.getUserId(), goodsId);

		return "redirect:/cart/index";
	}

	// カート内のすべてのグッズを削除
	@PostMapping("/clear")
	public String clearCart(@AuthenticationPrincipal LoginUser loginUser) {

		cartService.deleteAllByUserId(loginUser.getUserId());

		return "redirect:/cart/index";
	}

}
