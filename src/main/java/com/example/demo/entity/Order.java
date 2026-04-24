package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Order {

	private Integer id;
	
	private Integer userId;
	
	private String orderedName;
	
	private String orderedZipCode;
	
	private String orderedAddress;
	
	private Integer totalPrice;
	
	private LocalDateTime orderedAt;
	
}
