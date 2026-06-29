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
import com.example.model.PageResult;
import com.example.service.CartService;
import com.example.service.OrderService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/order")
@Slf4j
@RequiredArgsConstructor
public class OrderController {
	
	private final UserService userService;
	
	private final CartService cartService;
	
	private final OrderService orderService;

	// 購入確認画面を表示
	@GetMapping("/confirm")
	public String showConfirm(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		// ログインユーザー情報を取得
		User user = userService.findById(userId);
		
		// ログインユーザーのカート内商品をすべて取得
		List<CartItem> cartList = cartService.findByUserId(userId);
		
		// 合計金額を取得
		int totalAmount = cartService.calculateTotalAmount(cartList);
		
		// modelに登録
		model.addAttribute("user", user);
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);
		
		return "order/confirm";
	}
	
	// 注文を確定ボタン押下時の購入処理
	@PostMapping("/complete")
	public String completeOrder(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		try {
			// ログインユーザーのIDを取得
			Integer userId = loginUser.getUserId();
			
			// 登録に必要な情報をすべて集める
			User user = userService.findById(userId);
			List<CartItem> cartlist = cartService.findByUserId(userId);
			int totalAmount = cartService.calculateTotalAmount(cartlist);
			
			// 購入処理を呼び出す
			orderService.createOrder(user, cartlist, totalAmount);
			
			// 成功したら購入完了画面へリダイレクト
			return "redirect:/order/success";
			
		} catch (IllegalStateException e) {
			// 【例外処理】もし販売終了の商品があってエラーが出た場合
			log.error("購入処理エラー: {}", e.getMessage());
			
			// カート画面へリダイレクト
			return "redirect:/cart/index";
		}
	}
	
	// 注文完了画面を表示
	@GetMapping("/success")
	public String showSuccess() {
		return "order/success";
	}
	
	// 購入履歴の画面を表示
	@GetMapping("/history")
	public String getOrderHistory(
					@RequestParam(defaultValue = "0") int page,
					@AuthenticationPrincipal LoginUser loginUser, 
					Model model) {
		
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// ユーザー情報を取得してModelに登録
		User user = userService.findById(userId);
		model.addAttribute("user",user);
		
		PageResult<OrderViewItem> pageResult = orderService.getOrderPage(userId, page);
		
		// 画面(HTML)で使うためのデータをセットする
		model.addAttribute("historyList", pageResult.getContent());
		model.addAttribute("currentPage", pageResult.getCurrentPage());
		model.addAttribute("totalPages", pageResult.getTotalPages());
		model.addAttribute("startPage", pageResult.getStartPage());
		model.addAttribute("endPage", pageResult.getEndPage());
		
		// 画面遷移
		return "order/history";
	}
	
	
}
