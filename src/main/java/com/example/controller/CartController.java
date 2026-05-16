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
import com.example.entity.Cart;
import com.example.model.CartItem;
import com.example.service.CartService;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final GoodsService goodsService;
	private final CartService cartService;
	
	// カート追加処理
//	@PostMapping("/add")
//	public String postAdd(
//			@RequestParam("goodsId") Integer goodsId, 
//			@AuthenticationPrincipal LoginUser loginUser) {
//		
//		// データベースから商品情報を取得
//		Goods goods = goodsService.findById(goodsId);
//		
//		// 商品が見つからない場合は一覧へ戻る(安全策)
//		if (goods == null) {
//			return "redirect:/goods";
//		}
//		
//		// ログインユーザーから本物のIDを取得
//		Integer userId = loginUser.getUserId();
//		
//		// 現在のカードに同じ商品があるか確認。あり→個数を更新 なし→商品追加
//		cartService.addOrUpdateCart(userId, goodsId);
//		
//		//【完了】商品一覧画面へ戻る
//		return "redirect:/goods";
//	}
	
	// カート内容表示
	@GetMapping("/index")
	public String index(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		//ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// DBからそのユーザーのカート内商品をすべて取得
		List<CartItem> cartList = cartService.findByUserId(userId);
		
		// 3. 合計金額を計算するメソッドを呼び出す
		int totalAmount = cartService.calculateTotalAmount(cartList);
		
		// HTMLに渡すデータを登録
		model.addAttribute("loginUserName", loginUser.getName());
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalAmount", totalAmount);
		
		return "cart/index";
	}
	
	// カート内グッズの数量の変更
	@PostMapping("/update")
	public String updateQuantity(
			@RequestParam("goodsId") Integer goodsId, 
			@RequestParam("quantity") Integer quantity, 
			@AuthenticationPrincipal LoginUser loginUser) {
		
		// ログインユーザーIDを取得
		Integer userId = loginUser.getUserId();
		
		// カート情報を新しく作り、IDと新しい数量をセットする
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setGoodsId(goodsId);
		cart.setQuantity(quantity);
		
		// DBを更新する
		cartService.updateQuantity(cart);
		
		// カート内容画面へリダイレクトして再表示
		return "redirect:/cart/index";
	}
	
	// カート内の特定のグッズを削除
	@PostMapping("/delete")
	public String deleteByGoodsId(
			@RequestParam("goodsId") Integer goodsId, 
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
