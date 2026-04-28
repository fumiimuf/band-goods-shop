package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class GoodsController {

	// 一般ユーザー用のグッズ一覧画面
	@GetMapping("/goods")
	public String index() {
		
		return "goods/index";
	}
	
	
	// 管理者用のグッズ一覧画面
	@GetMapping("/admin/goods")
	public String adminIndex() {
		
		return "admin/goods/index";
	}
	
	
}
