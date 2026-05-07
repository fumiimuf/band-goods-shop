package com.example.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminGoodsController {

	// 管理者用のグッズ一覧画面
		@GetMapping("/goods")
		public String adminIndex() {
			
			return "admin/goods/index";
		}
}
