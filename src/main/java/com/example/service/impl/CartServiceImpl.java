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
	
	
}
