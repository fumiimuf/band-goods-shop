package com.example.controller;

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
		
		// 1ページあたりの表示件数は「8件」と決めます
		int size = 8;
		
		// Serviceに「ページ番号」と「件数」を渡してリストを取得
		List<GoodsItem> goodsList = goodsService.findByPage(false, keyword, page, size);
		
		// 全体の件数を数えます（全何ページあるか計算するため）
		long totalCount = goodsService.count(false, keyword);
		
		// 全体のページ数を計算します（端数は切り上げ。例：9件なら2ページ）
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		// 表示するページボタンの範囲を最大3に設定
		int displayButtonCount =3;
		
		// 開始ページ
		int startPage = Math.max(0, page - (displayButtonCount / 2));
		
		// 終了ページ
		int endPage = Math.min(totalPages -1,  startPage + displayButtonCount -1);
		
		// ページの終わりで方でボタンが3つ未満になってしまう場合の調整
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}
		
		// 画面（HTML）で使うためのデータをセットします
		model.addAttribute("goodsList", goodsList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		// グッズ一覧(一般ユーザー)を表示
		return "goods/index";
	}
	
}
