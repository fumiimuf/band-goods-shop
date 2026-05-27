package com.example.model;

import com.example.entity.Category;
import com.example.entity.Goods;

import lombok.Data;

@Data
public class GoodsItem {

	private Goods goods;
	
	private Category category;
}
