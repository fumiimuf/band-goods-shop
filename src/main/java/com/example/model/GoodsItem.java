package com.example.model;

import com.example.entity.Category;
import com.example.entity.Goods;

import lombok.Data;

@Data
public class GoodsItem {

	private Goods goods;
	
	private Category category;
	
	public String getImageUrl() {
		if (this.goods == null) {
			return "/images/test/no_image.png";
		}
		return this.goods.getImageUrl();
	}
}
