package com.example.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.GoodsItem;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/goods")
@RequiredArgsConstructor
public class AdminGoodsController {

	private final GoodsService goodsService;
	
	// 管理者用のグッズ一覧画面
		@GetMapping("/index")
		public String adminIndex(
						@RequestParam(name = "status", defaultValue = "active") String status, 
						@RequestParam(name = "page", defaultValue = "0") int page, 
						Model model) {
			
			// 1ページの表示件数は「5件」
			int size = 5;
			
			// 1. 文字列の status（active / suspended）を boolean（false / true）に翻訳する
			// status が "suspended"（停止中）なら true（削除済み）、それ以外なら false（販売中）
			boolean isDeleted = status.equals("suspended");
			
			// 条件に合うグッズを5件分だけ取得する
			List<GoodsItem> goodsList = goodsService.findByPage(isDeleted, page, size);
			
			// 状態（販売中 or 停止中）に合わせた総件数を取得する
			long totalCount = goodsService.count(isDeleted);
			
			// 全体のページ数を計算（端数切り上げ。例：6件なら2ページ）
			int totalPages = (int) Math.ceil((double) totalCount / size);
			
			// 画面（HTML）へ送るデータをセット
			model.addAttribute("goodsList", goodsList);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", totalPages);
			model.addAttribute("currentStatus", status);
			
			return "admin/goods/index";
		}
}
