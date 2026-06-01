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
		// 1. 画像名が空、またはnullなら「画像なし(no-image.png)」のパスを返す
		if (this.goods.getImage() == null || this.goods.getImage().isEmpty()) {
			return "/images/no-image.png";
		}
		
		// 2. 文字数が30文字より長ければ「アップロード画像」、短ければ「初期画像」のパスを返す
		if (this.goods.getImage().length() > 30) {
			return "/uploaded-images/" + this.goods.getImage();
		} else {
			return "/images/" + this.goods.getImage();
		}
	}
}
