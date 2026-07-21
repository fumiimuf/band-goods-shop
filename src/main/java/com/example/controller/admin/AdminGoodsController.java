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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.entity.Category;
import com.example.entity.Goods;
import com.example.form.GoodsEditForm;
import com.example.form.GoodsRegisterForm;
import com.example.model.GoodsItem;
import com.example.model.Pagination;
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
	
	public static final int SIZE = 5;

	// 管理者用のグッズ一覧画面
	@GetMapping({"/", "/index"})
	public String showGoodsIndex(
			@RequestParam(defaultValue = "active") String status,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {
		
		boolean isDeleted = status.equals("suspended");
		
		long totalCount = goodsService.getGoodsCount(isDeleted, keyword);
		
		Pagination<GoodsItem> pagination = new Pagination<>(page, totalCount, SIZE);
		
		List<GoodsItem> goodsList = goodsService.getGoodsByPage(isDeleted, keyword, pagination.getCurrentPage(), SIZE);
		
		pagination.setContent(goodsList);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("currentStatus", status);
		model.addAttribute("keyword", keyword);
		
		return "admin/goods/index";
	}

	// グッズ登録画面の表示
	@GetMapping("/register")
	public String showRegisterGoods(@ModelAttribute GoodsRegisterForm goodsRegisterForm, Model model) {
		
		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);

		return "admin/goods/register";
	}

	// グッズ登録処理の実行
	@PostMapping("/register")
	public String registerGoods(@ModelAttribute @Valid GoodsRegisterForm goodsRegisterForm,
			BindingResult bindingResult,
			Model model) {
		
		MultipartFile imageFile = goodsRegisterForm.getImageFile();
		
		if (imageFile != null && !imageFile.isEmpty()) {
			
			validateImageSize(imageFile, bindingResult);
		} else {
			bindingResult.rejectValue("imageFile", "NotNull.goodsRegisterForm.imageFile");
		}
		
		if (bindingResult.hasErrors()) {
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("categories", categories);

			return "admin/goods/register";
		}
		
		Goods goods = modelMapper.map(goodsRegisterForm, Goods.class);

		goodsService.registerGoods(goods, imageFile);

		return "redirect:/admin/goods/index";
	}

	// グッズ更新画面を表示
	@GetMapping({"/edit/{id}", "/edit", "/edit/"})
	public String showGoodsEdit(
			@PathVariable(required = false) Integer id,
			@ModelAttribute GoodsEditForm goodsEditForm,
			Model model, 
			RedirectAttributes redirectAttributes) {

		if (id == null || goodsService.getOneGoodsItemById(id) == null) {
			redirectAttributes.addFlashAttribute("showErrorToast", true);
			
			return "redirect:/admin/goods/index";
		}
		
		GoodsItem goodsItem = goodsService.getOneGoodsItemById(id);

		modelMapper.map(goodsItem.getGoods(), goodsEditForm);

		List<Category> categories = categoryService.getAllCategories();
		model.addAttribute("categories", categories);

		return "admin/goods/edit";
	}

	// グッズ更新処理を実行
	@PostMapping("/update")
	public String updateGoods(@ModelAttribute @Valid GoodsEditForm goodsEditForm,
			BindingResult bindingResult,
			Model model) {

		MultipartFile imageFile = goodsEditForm.getImageFile();

		if (imageFile != null && !imageFile.isEmpty()) {
			validateImageSize(imageFile, bindingResult);
		}
		
		if (bindingResult.hasErrors()) {
			List<Category> categories = categoryService.getAllCategories();
			model.addAttribute("categories", categories);

			return "admin/goods/edit";
		}
		
		Goods goods = modelMapper.map(goodsEditForm, Goods.class);
		
		goodsService.updateGoods(goods, imageFile);

		return "redirect:/admin/goods/index";
	}
	
	// 画像サイズチェック用の共通プライベートメソッド
	private void validateImageSize(MultipartFile imageFile, BindingResult bindingResult) {
		try {
			BufferedImage bufferedImage = ImageIO.read(imageFile.getInputStream());
			
			if (bufferedImage != null) {
				int width = bufferedImage.getWidth();
				int height = bufferedImage.getHeight();
				
				if (width > 600 || height > 600) {
					bindingResult.rejectValue("imageFile", "error.imageFile.size");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
