package com.github.fumiimuf.bandgoods.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.fumiimuf.bandgoods.entity.Cart;
import com.github.fumiimuf.bandgoods.model.CartItem;

@Mapper
public interface CartMapper {

	Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
	
	void insert(Cart cart);
	
	void updateQuantity(Cart cart);
	
	List<CartItem> findByUserId(Integer userId);
	
	void deleteByGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
	
	void deleteAllByUserId(Integer userId);
	
	int selectTotalAmountByUserId(Integer userId);
	
}
