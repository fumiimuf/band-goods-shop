package com.example.model;

import com.example.entity.Goods;
import com.example.entity.User;

import lombok.Data;

@Data
public class CartItem {

	private User user;
	
	private Goods goods;
	
    private Integer quantity;
    
    public int getSubtotal() {
    	if (this.goods == null || this.goods.getPrice() == null) {
    		return 0;
    	}
    	return this.goods.getPrice() * this.quantity;
    }
    
    public String getImageUrl() {
		if (this.goods == null) {
			return "/images/product/no_image.png";
		}
		return this.goods.getImageUrl();
    }
}
