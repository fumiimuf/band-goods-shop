package com.example.model;

import lombok.Data;

@Data
public class CartItem {

	private Integer goodsId;    // 商品ID（Cartエンティティと共通）
	
    private String name;        // 商品名（画面で見せたい！）
    
    private Integer price;      // 価格（画面で見せたい！）
    
    private String image;       // 画像パス（画面で見せたい！）
    
    private Integer quantity;   // 数量（Cartエンティティと共通）
    
    private Boolean isDeleted;
    
    // この商品の小計（単価 × 数量）を計算して返します。
    public int getSubtotal() {
    	return this.price * this.quantity;
    }

}
