package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Cart;
import com.example.model.CartItem;

@Mapper
public interface CartMapper {

	Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
	
	// selectAll(userId);追加
	
	void insert(Cart cart);
	
	// update(cart);追加
	
	void updateQuantity(Cart cart);
	
	List<CartItem> findByUserId(Integer userId);
	
	void deleteByGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
	
	void deleteAllByUserId(Integer userId);
	
	int selectTotalAmountByUserId(Integer userId);
}
