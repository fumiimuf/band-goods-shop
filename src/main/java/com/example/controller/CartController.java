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
import com.example.entity.Goods;
import com.example.model.CartItem;
import com.example.repository.CartMapper;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;





@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final GoodsService goodsService;
	private final CartMapper cartMapper;
	
	// カート追加処理
	@PostMapping("/add")
	public String postAdd(
			@RequestParam("goodsId") Integer goodsId, 
			@AuthenticationPrincipal LoginUser loginUser) {
		
		// データベースから商品情報を取得
		Goods goods = goodsService.findById(goodsId);
		
		// 商品が見つからない場合は一覧へ戻る(安全策)
		if (goods == null) {
			return "redirect:/goods";
		}
		
		// ログインユーザーから本物のIDを取得
		Integer userId = loginUser.getUserId();
		
		// DBに同じグッズがあるか確認
		Cart existingCart = cartMapper.findByGoodsId(userId, goodsId);
		
		if (existingCart != null) {
			// あれば個数を +1 して更新（UPDATE）
			existingCart.setQuantity(existingCart.getQuantity() + 1);
			cartMapper.updateQuantity(existingCart);
		} else {
			// C. なければ新規登録（INSERT）
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			newCart.setGoodsId(goodsId);
			newCart.setQuantity(1);
			cartMapper.insert(newCart);
		}
		
		//【完了】商品一覧画面へ戻る
		return "redirect:/goods";
	}
	
	// カート内容表示
	@GetMapping("/index")
	public String index(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		//ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// DBからそのユーザーのカート内商品をすべて取得
		List<CartItem> cartList = cartMapper.findByUserId(userId);
		
		// 3. 合計金額を計算（設計書の「合計金額（税込）」用）
		int totalAmount = 0;
		for (CartItem item : cartList) {
			totalAmount += item.getPrice() * item.getQuantity();
		}
		
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
		cartMapper.updateQuantity(cart);
		
		// カート内容画面へリダイレクトして再表示
		return "redirect:/cart/index";
	}
	
	// カート内の特定のグッズを削除
	@PostMapping("/delete")
	public String deleteByGoodsId(
			@RequestParam("goodsId") Integer goodsId, 
			@AuthenticationPrincipal LoginUser loginUser) {
	
		// ログインユーザーのIDを使って、特定のグッズを削除
		cartMapper.deleteByGoodsId(loginUser.getUserId(), goodsId);
		
		// 削除後はカート内容画面へリダイレクト
		return "redirect:/cart/index";
	}
	
}
