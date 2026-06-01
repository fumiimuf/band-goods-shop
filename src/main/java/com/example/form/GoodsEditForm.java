package com.example.form;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class GoodsEditForm {

	@NotNull
	private Integer id;
	
	@NotBlank
	@Size(max = 50)
	private String name;

	@NotNull
	@Min(0)
	private Integer price;

	private MultipartFile imageFile;

	@NotBlank
	@Size(max = 500)
	private String description;

	@NotNull
	private Integer categoryId;
	
	private Boolean isDeleted;
	
	private LocalDateTime deleteDateTime;
}
