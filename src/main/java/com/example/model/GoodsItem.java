package com.example.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GoodsItem {

	private Integer id;

	private String name;

	private Integer price;

	private String image;

	private String description;

	private Integer categoryId;

	private String categoryName;

	private LocalDateTime createDateTime;

	private LocalDateTime deleteDateTime;

	private Boolean isDeleted;
}
