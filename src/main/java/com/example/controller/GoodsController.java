package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.GoodsItem;
import com.example.model.PageResult;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

	private final GoodsService goodsService;
	
	
	// 一般ユーザー用のグッズ一覧画面
	@GetMapping("/index")
	public String index(
		// URLの ?page=数字 を受け取ります。指定がない場合は 0 にします。
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "") String keyword,
		Model model) {
		
		PageResult<GoodsItem> pageResult = goodsService.getGoodsPage(keyword, page);
		
		// 画面（HTML）で使うためのデータをセットします
		model.addAttribute("goodsList", pageResult.getContent());
		model.addAttribute("currentPage", pageResult.getCurrentPage());
		model.addAttribute("totalPages", pageResult.getTotalPages());
		model.addAttribute("startPage", pageResult.getStartPage());
		model.addAttribute("endPage", pageResult.getEndPage());
		model.addAttribute("keyword", keyword);
		
		// グッズ一覧(一般ユーザー)を表示
		return "goods/index";
	}
	
}
