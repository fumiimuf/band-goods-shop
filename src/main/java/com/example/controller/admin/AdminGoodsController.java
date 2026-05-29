package com.example.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.entity.Category;
import com.example.entity.Goods;
import com.example.form.GoodsEditForm;
import com.example.form.GoodsRegisterForm;
import com.example.model.GoodsItem;
import com.example.service.CategoryService;
import com.example.service.GoodsService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/goods")
@RequiredArgsConstructor
public class AdminGoodsController {

	private final GoodsService goodsService;
	
	private final CategoryService categoryService;
	
	private final ModelMapper modelMapper;
	
	// 💡 application.properties から画像の保存先（C:/Users/...）を引っ張ってきます
	// @RequiredArgsConstructorがあっても、この@Valueフィールドは自動生成の対象外になるので安全です
	@Value("${upload-directory}")
	private String uploadDirectory;
	
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
	
	// グッズ登録画面の表示
	@GetMapping("register")
	public String showRegisterForm(@ModelAttribute("goodsForm") GoodsRegisterForm goodsRegisterForm, Model model) {
		// ドロップダウンに表示するカテゴリ一覧をDBから取得してModelに詰める
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		
		return "admin/goods/register";
	}
	
	// グッズ登録処理の実行
	@PostMapping("register")
	public String registerGoods(@ModelAttribute("goodsForm") @Valid GoodsRegisterForm goodsRegisterForm, 
					BindingResult bindingResult, 
					Model model) {
		
		// 入力チェックでエラーがあった場合
		if (bindingResult.hasErrors()) {
			// 画面に戻る際、ドロップダウン用のカテゴリ一覧を再取得してModelに詰める
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("categories", categories);
			
			// エラーメッセージを表示した状態で、登録画面のHTMLを再表示する
			return "admin/goods/register";
		}
		
		// formをGoodsクラスに変換
		Goods goods = modelMapper.map(goodsRegisterForm, Goods.class);
		
		// 画面から送られてきた画像ファイルを取り出す
		MultipartFile imageFile = goodsRegisterForm.getImageFile();
		
		// DBの「画像」項目に保存するためのファイル名を入れる箱
		String savedFileName = "";
		
		// 画像ファイルが選択されているかチェック
		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				// 元のファイル名を取得
				String originalFileName = imageFile.getOriginalFilename();
				
				// 同名ファイルの上書きを防ぐため、UUID（ランダムな一意の文字列）を先頭に結合
				savedFileName = UUID.randomUUID().toString() + "_" + originalFileName;
				
				// application.properties から取得した保存先ディレクトリとファイル名を結合
				String filePath = uploadDirectory + savedFileName;
				
				// パソコンのフォルダへバイナリデータとして物理書き出し
				byte[] bytes = imageFile.getBytes();
				Files.write(Paths.get(filePath), bytes);
				
			} catch (IOException e) {
				// ファイルの書き込みに失敗した場合はスタックトレースを出力
				e.printStackTrace();
			}
		}
		
		// 後から決まった「唯一無二のファイル名」と「初期状態の削除フラグ(false=0)」をセットする
		goods.setImage(savedFileName);
		goods.setIsDeleted(false);
		
		// 完成した「goods」を引数にセットしてサービスを呼び出す
		goodsService.registerGoods(goods);
		
		// すべての処理が正常に完了したら、管理者用のグッズ一覧画面へリダイレクト（転送）
		return "redirect:/admin/goods/index";
	}
	
	// グッズ更新画面を表示
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") int id,
								@ModelAttribute("goodsEditForm") GoodsEditForm goodsEditForm, 
								Model model) {
		
		// DBから最新のグッズ情報を取得
		GoodsItem goodsItem = goodsService.findById(id);
		
		// もし指定されたIDのグッズが存在しない場合は、一覧画面へ戻す安全策
		if (goodsItem == null) {
			return "redirect:/admin/goods/index";
		}
		
		// ModelMapperを使い、GoodsItemの中にある「goods」オブジェクトからFormへ丸ごとコピーする
		modelMapper.map(goodsItem.getGoods(), goodsEditForm);
		
		// ドロップダウンに表示するカテゴリ一覧をDBから取得してModelに詰める
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);
		
		// グッズ更新画面へ遷移
		return "admin/goods/edit";
	}
	
}
