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
    	if (this.goods == null || this.goods.getImage() == null || this.goods.getImage().isEmpty()) {
    		return "/images/no_image.png";
    	}
    	
    	if (this.goods.getImage().length() > 30) {
    		return "/uploaded-images/" + this.goods.getImage();
    		
    	} else {
			return "/images/" + this.goods.getImage();
		}
    }
}
