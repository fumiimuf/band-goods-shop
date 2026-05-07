package com.example.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.Goods;
import com.example.repository.GoodsMapper;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class GoodsController {

	private final GoodsMapper goodsMapper;
	
	
	// 一般ユーザー用のグッズ一覧画面
	@GetMapping("/goods")
	public String index(
		// URLの ?page=数字 を受け取ります。指定がない場合は 0 にします。
		@RequestParam(name = "page", defaultValue = "0") int page, 
		Model model) {
		
		// 1ページあたりの表示件数は「8件」と決めます
		int limit = 8;
		
		// 「最初の◯件を飛ばす」ための計算です（例：1ページ目なら 1*8=8件飛ばす）
		int offset = page * limit;
		
		// Mapperを使って、データベースから「現在のページに必要な8件」を取得します
		List<Goods> goodsList = goodsMapper.findByPage(limit, offset);
		
		// 全体の件数を数えます（全何ページあるか計算するため）
		long totalCount = goodsMapper.count();
		
		// 全体のページ数を計算します（端数は切り上げ。例：9件なら2ページ）
		int totalPages = (int) Math.ceil((double) totalCount / limit);
		
		// 画面（HTML）で使うためのデータをセットします
		model.addAttribute("goodsList", goodsList);   // 商品リスト
		model.addAttribute("currentPage", page);      // 現在のページ番号
		model.addAttribute("totalPages", totalPages); // 全ページ数
		
		// グッズ一覧(一般ユーザー)を表示
		return "goods/index";
	}
	
	
	
	
	
}
