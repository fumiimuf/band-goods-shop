package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.GoodsItem;
import com.example.model.Pagination;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

	private final GoodsService goodsService;
	
	// 一般ユーザー用のグッズ一覧画面
	@GetMapping("/index")
	public String showGoodsIndex(
		// URLの ?page=数字 を受け取ります。指定がない場合は 0 にします。
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "") String keyword,
		Model model) {
		
		Pagination<GoodsItem> pagination = goodsService.getGoodsPage(keyword, page);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		
		return "goods/index";
	}
	
}
