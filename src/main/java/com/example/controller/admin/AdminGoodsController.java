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
import com.example.model.PageResult;
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
	public String showGoodsIndex(
			@RequestParam(defaultValue = "active") String status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {
		
		PageResult<GoodsItem> result = goodsService.getAdminGoodsPage(status, keyword, page);

		// 画面（HTML）へ送るデータをセット
		model.addAttribute("goodsList", result.getList());
		model.addAttribute("currentPage", result.getPage());
		model.addAttribute("totalPages", result.getTotalPages());
		model.addAttribute("startPage", result.getStartPage());
		model.addAttribute("endPage", result.getEndPage());
		model.addAttribute("currentStatus", status);
		model.addAttribute("keyword", keyword);
		
		return "admin/goods/index";
	}

	// グッズ登録画面の表示
	@GetMapping("/register")
	public String showRegisterGoods(@ModelAttribute GoodsRegisterForm goodsRegisterForm, Model model) {
		// ドロップダウンに表示するカテゴリ一覧をDBから取得してModelに詰める
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);

		return "admin/goods/register";
	}

	// グッズ登録処理の実行
	@PostMapping("/register")
	public String registerGoods(@ModelAttribute @Valid GoodsRegisterForm goodsRegisterForm,
			BindingResult bindingResult,
			Model model) {
		
		// 画面から送られてきた画像ファイルを取り出す
		MultipartFile imageFile = goodsRegisterForm.getImageFile();
		
		// 画像ファイルが選択されているかチェック
		if (imageFile != null && !imageFile.isEmpty()) {
			
			validateImageSize(imageFile, bindingResult);
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
	public String showGoodsEdit(@PathVariable int id,
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
	@PostMapping("/update")
	public String updateGoods(@ModelAttribute @Valid GoodsEditForm goodsEditForm,
			BindingResult bindingResult,
			Model model) {

		// 画面から送られてきた画像ファイル（MultipartFile）の取り出しと判定
		MultipartFile imageFile = goodsEditForm.getImageFile();

		if (imageFile != null && !imageFile.isEmpty()) {
			validateImageSize(imageFile, bindingResult);
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
	
	// 画像サイズチェック用の共通プライベートメソッド
	private void validateImageSize(MultipartFile imageFile, BindingResult bindingResult) {
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

}
