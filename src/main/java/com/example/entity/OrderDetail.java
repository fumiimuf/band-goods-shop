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
			return "/images/no_image.png";
		}
		
		// カート時と同じ判別ルール（文字数が30文字より多ければ更新された画像、それ以外は初期画像）
		if (this.orderedImage.length() > 30) {
			return "/uploaded-images/" + this.orderedImage;
		} else {
			return "/images/" + this.orderedImage;
		}
	}
	
}
