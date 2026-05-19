package com.example.entity;

import lombok.Data;

@Data
public class Cart {

	private Integer id;
	
	private Integer userId;
	
	private Integer goodsId;
	
	private Integer quantity;
}
