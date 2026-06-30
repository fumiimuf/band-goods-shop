package com.example.model;

import com.example.entity.Category;
import com.example.entity.Goods;

import lombok.Data;

@Data
public class GoodsItem {

	private Goods goods;
	
	private Category category;
	
	// 画像の正しいURLを自動で判断して返すメソッド
	public String getImageUrl() {
		// 1. 画像名が空、またはnullなら「画像なし(no_image.png)」のパスを返す
		if (this.goods == null || this.goods.getImage() == null || this.goods.getImage().isEmpty()) {
			return "/images/product/no_image.png";
		}
		
		return "/images/product/" + this.goods.getImage();
	}
}
