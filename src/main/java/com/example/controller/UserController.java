package com.example.controller;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.config.LoginUser;
import com.example.entity.User;
import com.example.form.UserEditForm;
import com.example.form.UserRegisterForm;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	private final ModelMapper modelMapper;
	
	
	
	// ユーザー登録情報を表示
	@GetMapping("/register")
	public String showRegister(@ModelAttribute UserRegisterForm userRegisterForm) {
		
		return "user/register";
	}
	
	// ユーザー登録処理
	@PostMapping("/register")
	public String registerUser(@ModelAttribute @Validated UserRegisterForm userRegisterForm, 
			BindingResult bindingResult) {
		
		// メールアドレス重複チェック
		if (userService.existEmail(userRegisterForm.getEmail())) {
			bindingResult.rejectValue("email", "error.duplicate.email");
		}
		
		// 入力チェック
		if (bindingResult.hasErrors()) {
			return "user/register";
		}
		
		// formをUserクラスに変換
		User user = modelMapper.map(userRegisterForm, User.class);
		
		userService.insertOne(user);
		
		return "redirect:/login";
	}
	
	@GetMapping("/profile")
	public String showUserProfile(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// DBから最新のユーザー情報を取得
		User user = userService.findById(userId);
		
		// modelに登録
		model.addAttribute("user", user);
		
		// ユーザーページ画面へ遷移
		return "user/profile";
	}
	
	@GetMapping("/edit")
	public String showUserEdit(@AuthenticationPrincipal LoginUser loginUser, 
					@ModelAttribute UserEditForm userEditForm) {
		
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// DBから最新のユーザー情報を取得
		User user = userService.findById(userId);
		
		// ModelMapperを使い、DBから取得した情報を画面用の器（Form）に丸ごとコピーします
		modelMapper.map(user, userEditForm);
		
		// 4. パスワードだけは画面設計書の仕様（空欄で表示）に基づき、意図的に空っぽ（空文字）にクリアします
		userEditForm.setPassword("");
		
		// ユーザー更新画面へ遷移
		return "user/edit";
	}
	
	@PostMapping("/update")
	public String updateUser(@ModelAttribute @Validated UserEditForm userEditForm, 
					BindingResult bindingResult,
					@AuthenticationPrincipal LoginUser loginUser) {
		
		User currentUser = userService.findById(loginUser.getUserId());
		
		if (!userEditForm.getEmail().equals(currentUser.getEmail())) {
			// メールアドレス重複チェック
			if (userService.existEmail(userEditForm.getEmail())) {
				bindingResult.rejectValue("email", "error.duplicate.email");
			}
		}
		
		// 入力チェック
		if(bindingResult.hasErrors()) {
			return "user/edit";
		}
		
		// フォームオブジェクトをUserクラスに変換
		User user = modelMapper.map(userEditForm, User.class);
		
		user.setId(loginUser.getUserId());
		
		userService.updateUser(user);
		
		if (loginUser != null) {
			loginUser.setName(user.getName());
		}
		
		// ユーザーページへリダイレクト
		return "redirect:/user/profile";
	}
}
