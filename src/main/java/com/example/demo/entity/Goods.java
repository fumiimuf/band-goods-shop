package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Goods {

	private Integer id;
	
	private String name;
	
	private Integer price;
	
	private String image;
	
	private String description;
	
	private Integer categoryId;
	
	private LocalDateTime createDateTime;
	
	private LocalDateTime deleteDateTime;
	
	private Boolean isDeleted;
}
