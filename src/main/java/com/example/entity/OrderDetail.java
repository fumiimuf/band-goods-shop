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
	
	public String getOrderedImageUrl() {
		if (this.orderedImage == null || this.orderedImage.isEmpty()) {
			return "/images/test/no_image.png";
		}
		return "/images/product/" + this.orderedImage;
	}
	
}
