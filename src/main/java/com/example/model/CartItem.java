package com.example.model;

import com.example.entity.Goods;
import com.example.entity.User;

import lombok.Data;

@Data
public class CartItem {

	private User user;
	
	private Goods goods;
	
    private Integer quantity;
    
    // この商品の小計（単価 × 数量）を計算して返します。
    public int getSubtotal() {
    	if (this.goods == null || this.goods.getPrice() == null) {
    		return 0;
    	}
    	return this.goods.getPrice() * this.quantity;
    }
    
    // カート画面用の画像URL判別メソッド
    public String getImageUrl() {
    	// 1. 画像名が空、またはnullなら「画像なし(no_image.png)」のパスを返す
		if (this.goods == null || this.goods.getImage() == null || this.goods.getImage().isEmpty()) {
			return "/images/product/no_image.png";
		}
		
		return "/images/product/" + this.goods.getImage();
    }
}
