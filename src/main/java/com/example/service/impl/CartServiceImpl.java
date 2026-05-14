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
	public Cart findByGoodsId(Integer userId, Integer goodsId) {
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
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void updateQuantity(Cart cart) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public List<CartItem> findByUserId(Integer userId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void deleteByGoodsId(Integer userId, Integer goodsId) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
	
	
}
