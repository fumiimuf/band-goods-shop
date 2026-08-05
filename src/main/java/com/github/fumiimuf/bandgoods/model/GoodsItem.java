package com.github.fumiimuf.bandgoods.model;

import com.github.fumiimuf.bandgoods.entity.Category;
import com.github.fumiimuf.bandgoods.entity.Goods;

import lombok.Data;

@Data
public class GoodsItem {

	private Goods goods;
	
	private Category category;
	
	public String getImageUrl() {
		if (this.goods == null) {
			return "/images/product/no_image.png";
		}
		return this.goods.getImageUrl();
	}
}
