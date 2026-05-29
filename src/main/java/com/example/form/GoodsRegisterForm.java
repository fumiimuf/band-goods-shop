package com.example.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data // 💡 これだけでゲッター・セッター、toStringなどが裏で自動生成されます！
public class GoodsRegisterForm {

	// 商品名（設計書より：必須、50文字以内）
	@NotBlank
	@Size(max = 50)
	private String name;

	// 価格（設計書より：必須、0以上の半角数字）
	@NotNull
	@Min(0)
	private Integer price;

	// 💡 画像ファイルそのものを受け取るための型（必須）
	@NotNull
	private MultipartFile imageFile;

	// 商品説明（設計書より：必須、500文字以内）
	@NotBlank
	@Size(max = 500)
	private String description;

	// カテゴリID（設計書より：必須）
	@NotNull
	private Integer categoryId;

}