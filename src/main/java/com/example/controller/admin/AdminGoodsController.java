package com.example.controller.admin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
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


	// 管理者用のグッズ一覧画面
	@GetMapping("/index")
	public String adminIndex(
			@RequestParam(defaultValue = "active") String status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {

		// 1ページの表示件数は「5件」
		int size = 5;

		// 1. 文字列の status（active / suspended）を boolean（false / true）に翻訳する
		// status が "suspended"（停止中）なら true（削除済み）、それ以外なら false（販売中）
		boolean isDeleted = status.equals("suspended");

		// 条件に合うグッズを5件分だけ取得する
		List<GoodsItem> goodsList = goodsService.findByPage(isDeleted, keyword, page, size);

		// 状態（販売中 or 停止中）に合わせた総件数を取得する
		long totalCount = goodsService.count(isDeleted, keyword);

		// 全体のページ数を計算（端数切り上げ。例：6件なら2ページ）
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		// 表示するページボタンの範囲を最大3つに設定
		int displayButtonCount = 3;
		
		// 開始ページ
		int startPage = Math.max(0, page - (displayButtonCount / 2));
		
		// 終了ページ
		int endPage = Math.min(totalPages - 1, startPage + displayButtonCount - 1);
		
		// ページの終わりでボタンが3つ未満になってしまう場合の調整
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}

		// 画面（HTML）へ送るデータをセット
		model.addAttribute("goodsList", goodsList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentStatus", status);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "admin/goods/index";
	}

	// グッズ登録画面の表示
	@GetMapping("register")
	public String showRegisterForm(@ModelAttribute GoodsRegisterForm goodsRegisterForm, Model model) {
		// ドロップダウンに表示するカテゴリ一覧をDBから取得してModelに詰める
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);

		return "admin/goods/register";
	}

	// グッズ登録処理の実行
	@PostMapping("register")
	public String registerGoods(@ModelAttribute @Valid GoodsRegisterForm goodsRegisterForm,
			BindingResult bindingResult,
			Model model) {
		
		// 画面から送られてきた画像ファイルを取り出す
		MultipartFile imageFile = goodsRegisterForm.getImageFile();
		
		// 画像ファイルが選択されているかチェック
		if (imageFile != null && !imageFile.isEmpty()) {
			try {
				// 画像の縦横サイズチェック
				// マルチパートファイルをJava標準のBufferedImageに変換
				BufferedImage bufferedImage = ImageIO.read(imageFile.getInputStream());
				
				if (bufferedImage != null) {
					// 画像の横幅を取得
					int width = bufferedImage.getWidth();
					// 画像の縦幅を取得
					int height = bufferedImage.getHeight();
					
					// 600x600ピクセル以外の場合はエラーにする
					if (width > 600 || height > 600) {
						bindingResult.rejectValue("imageFile", "error.imageFile.size");
					}
				}
			} catch (IOException e) {
				// ファイルの書き込みに失敗した場合はスタックトレースを出力
				e.printStackTrace();
			}
		} else {
			// ファイルがnull or 空(未選択)の場合
			bindingResult.rejectValue("imageFile", "NotNull.goodsRegisterForm.imageFile");
		}
		
		// 入力チェックor画像サイズでエラーがあった場合
		if (bindingResult.hasErrors()) {
			// 画面に戻る際、ドロップダウン用のカテゴリ一覧を再取得してModelに詰める
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("categories", categories);

			// エラーメッセージを表示した状態で、登録画面のHTMLを再表示する
			return "admin/goods/register";
		}
		
		// formをGoodsクラスに変換
		Goods goods = modelMapper.map(goodsRegisterForm, Goods.class);

		// チェックに通ったグッズ情報と画像をセットしてグッズ登録処理を呼び出す
		
		goodsService.registerGoods(goods, imageFile);

		// すべての処理が正常に完了したら、管理者用のグッズ一覧画面へリダイレクト（転送）
		return "redirect:/admin/goods/index";
	}

	// グッズ更新画面を表示
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable int id,
			@ModelAttribute GoodsEditForm goodsEditForm,
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

	// グッズ更新処理を実行
	@PostMapping("update")
	public String updateGoods(@ModelAttribute @Valid GoodsEditForm goodsEditForm,
			BindingResult bindingResult,
			Model model) {

		// 画面から送られてきた画像ファイル（MultipartFile）の取り出しと判定
		MultipartFile imageFile = goodsEditForm.getImageFile();

		if (imageFile != null && !imageFile.isEmpty()) {
			// パターンA：新しく画像が選択されている場合→新規保存処理を行う（登録時と同じロジック）
			try {
				// 画像の縦横サイズチェック
				// マルチパートファイルをJava標準のBufferedImageに変換
				BufferedImage bufferedImage = ImageIO.read(imageFile.getInputStream());
				
				if (bufferedImage != null) {
					// 画像の横幅を取得
					int width = bufferedImage.getWidth();
					// 画像の縦幅を取得
					int height = bufferedImage.getHeight();
					
					// 600x600ピクセル以外の場合はエラーにする
					if (width > 600 || height > 600) {
						bindingResult.rejectValue("imageFile", "error.imageFile.size");
					}
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		
		// 入力チェック・画像サイズチェックをまとめて判定する
		if (bindingResult.hasErrors()) {
			// 入力エラーがあった場合、更新画面用のドロップダウン用のカテゴリ一覧を再取得してModelに詰める
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("categories", categories);

			// エラーメッセエージを保持したまま、更新画面を再表示する
			return "admin/goods/edit";
		}
		
		// Formの入力値を、DB保存用の「Goodsエンティティ」へ変換（コピー）
		Goods goods = modelMapper.map(goodsEditForm, Goods.class);
		
		// サービスを呼び出して、DBのデータを上書き更新する
		goodsService.updateGoods(goods, imageFile);

		// すべて正常に完了したら、管理者用のグッズ一覧画面へリダイレクト
		return "redirect:/admin/goods/index";
	}

}
