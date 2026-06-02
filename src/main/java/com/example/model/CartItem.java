package com.example.model;

import com.example.entity.Goods;

import lombok.Data;

@Data
public class CartItem {

	private Integer userId;
	
	private Integer goodsId;
	
	private Goods goods;
	
    private Integer quantity;
    
    // この商品の小計（単価 × 数量）を計算して返します。
    public int getSubtotal() {
    	if (this.goods == null || this.goods.getPrice() == null) {
    		return 0;
    	}
    	return this.goods.getPrice() * this.quantity;
    }
}
