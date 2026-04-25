package com.example.entity;

import lombok.Data;

@Data
public class Cart {

	private Integer userId;
	
	private Integer goodsId;
	
	private Integer quantity;
}
