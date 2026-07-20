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

	@Override
	public int getTotalAmount(Integer userId) {
		return cartMapper.selectTotalAmountByUserId(userId);
	}

	@Override
	public int getTotalQuantity(Integer userId) {
		List<CartItem> cartList = cartMapper.findByUserId(userId);

		int totalCount = 0;
		if (cartList != null) {
			for (CartItem item : cartList) {
				if (item.getGoods() != null && item.getGoods().getIsDeleted() != null
						&& item.getGoods().getIsDeleted()) {
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

	@Override
	public Cart getCartByUserAndGoods(Integer userId, Integer goodsId) {
		return cartMapper.selectByUserIdAndGoodsId(userId, goodsId);
	}

	@Override
	public void registerCart(Cart cart) {
		cartMapper.insertOne(cart);
	}
}
