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
		if (this.goods == null) {
			return "/images/product/no_image.png";
		}
		return this.goods.getImageUrl();
	}
}
