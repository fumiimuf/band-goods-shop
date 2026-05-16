package com.example.form;

import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

@Data
public class CartForm {

	@NotNull
	private Integer goodsId;
	
	@NotNull
	@Range(min = 1, max = 10)
	private Integer quantity;
}
