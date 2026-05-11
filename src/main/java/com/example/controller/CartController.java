package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.config.LoginUser;
import com.example.entity.Cart;
import com.example.entity.Goods;
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
		
		// 2. ログインユーザーから本物のIDを取得
		Integer userId = loginUser.getUserId();
		
		// A. DBに同じグッズがあるか確認
		Cart existingCart = cartMapper.findByGoodsId(userId, goodsId);
		
		if (existingCart != null) {
			// B. あれば個数を +1 して更新（UPDATE）
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
		
		// 6. 【完了】商品一覧画面へ戻る
		return "redirect:/goods";
	}
	
}
