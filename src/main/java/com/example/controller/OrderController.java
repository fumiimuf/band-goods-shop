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
import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;
import com.example.model.Pagination;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

	private final UserService userService;

	private final CartService cartService;

	private final OrderService orderService;

	private final int PAGE_SIZE = 2;

	// 購入確認画面を表示
	@GetMapping("/confirm")
	public String showConfirm(@AuthenticationPrincipal LoginUser loginUser, Model model) {

		Integer userId = loginUser.getUserId();

		List<CartItem> cartList = cartService.findByUserId(userId);

		if (isInvalidCart(cartList)) {
			return "redirect:/cart/index";
		}

		User user = userService.findById(userId);

		int totalAmount = cartService.getTotalAmount(userId);

		model.addAttribute("user", user);
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);

		return "order/confirm";
	}

	// 注文を確定ボタン押下時の購入処理
	@PostMapping("/complete")
	public String completeOrder(@AuthenticationPrincipal LoginUser loginUser, Model model) {

		Integer userId = loginUser.getUserId();

		List<CartItem> cartList = cartService.findByUserId(userId);

		if (isInvalidCart(cartList)) {
			return "redirect:/cart/index";
		}

		User user = userService.findById(userId);
		int totalAmount = cartService.getTotalAmount(userId);

		orderService.createOrder(user, cartList, totalAmount);

		return "redirect:/order/success";

	}

	// 注文完了画面を表示
	@GetMapping("/success")
	public String showSuccess() {
		return "order/success";
	}

	// 購入履歴の画面を表示
	@GetMapping("/history")
	public String showOrderHistory(
			@RequestParam(defaultValue = "0") int page,
			@AuthenticationPrincipal LoginUser loginUser,
			Model model) {

		Integer userId = loginUser.getUserId();

		User user = userService.findById(userId);
		model.addAttribute("user", user);

		long totalCount = orderService.getOrderCountByUserId(userId);

		Pagination<OrderViewItem> pagination = new Pagination<>(page, totalCount, PAGE_SIZE);

		List<OrderViewItem> historyList = orderService.getOrderHistoryByPage(userId, pagination.getCurrentPage(),
				PAGE_SIZE);

		pagination.setContent(historyList);

		model.addAttribute("pagination", pagination);

		return "order/history";
	}

	// カートチェック処理をするプライベートメソッド
	private boolean isInvalidCart(List<CartItem> cartList) {
		if (cartList == null || cartList.isEmpty()) {
			return true;
		}
		for (CartItem item : cartList) {
			if (item.getGoods() != null && item.getGoods().getIsDeleted()) {
				return true;
			}
		}
		return false;
	}
}
