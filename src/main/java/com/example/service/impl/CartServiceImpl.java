package com.example.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.entity.Cart;
import com.example.model.CartItem;
import com.example.repository.CartMapper;
import com.example.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

	private final CartMapper cartMapper;

	@Override
	public void addOrUpdateCart(Integer userId, Integer goodsId) {
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
	}

	@Override
	public void insert(Cart cart) {
		cartMapper.insert(cart);
	}

	@Override
	public void updateQuantity(Cart cart) {
		cartMapper.updateQuantity(cart);
	}

	@Override
	public List<CartItem> findByUserId(Integer userId) {
		return cartMapper.findByUserId(userId);
	}

	@Override
	public void deleteByGoodsId(Integer userId, Integer goodsId) {
		cartMapper.deleteByGoodsId(userId, goodsId);
	}
	
	// 小計金額の計算
	@Override
	public int calculateSubtotal(List<CartItem> cartList, Integer goodsId) {
		for (CartItem item : cartList) {
			if (item.getGoodsId().equals(goodsId)) {
				return item.getSubtotal();
			}
		}
		return 0;
	}
	
	// 合計金額の計算
	@Override
	public int calculateTotalAmount(List<CartItem> cartlist) {
		int totalAmount = 0;
		for (CartItem item : cartlist) {
			// 販売終了（isDeletedがtrue）の商品は計算をスキップする
			if (item.getIsDeleted() != null && item.getIsDeleted()) {
				continue;
			}
			totalAmount += item.getSubtotal();
		}
		return totalAmount;
	}

	@Override
	public int getTotalQuantity(Integer userId) {
		// ログインユーザーのカート情報をすべて取得する
		List<CartItem> cartList = cartMapper.findByUserId(userId);
		
		// 合計金額を入れる箱を用意
		int totalCount = 0;
		// カートの中身を一つずつ取り出して、個数を足していく
		if (cartList != null) {
			for (CartItem item : cartList) {
				// 販売終了（isDeletedがtrue）の商品は合計個数に含めない
				if (item.getIsDeleted() != null && item.getIsDeleted()) {
					continue;
				}
				totalCount += item.getQuantity();
			}
		}
		return totalCount;
	}

	@Override
	public void deleteAllByUserId(Integer userId) {
		cartMapper.deleteAllByUserId(userId);
	}

	
	
	
	
}
