package com.example.entity;

import lombok.Data;

@Data
public class OrderDetail {

	private Integer id;
	
	private Integer orderId;
	
	private String orderedImage;
	
	private String orderedName;
	
	private Integer orderedPrice;
	
	private Integer quantity;
	
	// 販売履歴用の画像URL判別メソッド
	public String getOrderedImageUrl() {
		// 画像名が空っぽ、またはnullの場合は「画像なし」の共通画像を返す
		if (this.orderedImage == null || this.orderedImage.isEmpty()) {
			return "/images/product/no_image.png";
		}
		return "/images/product/" + this.orderedImage;
	}
	
}
