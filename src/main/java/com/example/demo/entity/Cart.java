package com.example.demo.entity;

import lombok.Data;

@Data
public class Cart {

	private Integer userId;
	
	private Integer goodsId;
	
	private Integer quantity;
}
