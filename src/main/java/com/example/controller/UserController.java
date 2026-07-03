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
	
	/**
	 * ユーザーページ（登録情報確認画面）を表示します。
	 * * @param loginUser 認証情報から取得したログイン中のユーザーオブジェクト
	 * @param model 画面へデータを渡すためのオブジェクト
	 * @return ユーザーページ画面のテンプレートパス
	 */
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
	
	/**
	 * ユーザー編集画面を表示します。
	 * * @param loginUser 認証情報から取得したログイン中のユーザーオブジェクト
	 * @param userEditForm 画面に初期値を渡すためのフォームオブジェクト
	 * @param model 画面へデータを渡すためのオブジェクト（必要に応じて使用）
	 * @return ユーザー編集画面のテンプレートパス
	 */
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
	
	/**
	 * ユーザー情報の更新処理を実行します。
	 * * @param userEditForm 画面から送信された入力内容
	 * @param bindingResult バリデーションの結果
	 * @param loginUser ログイン中のユーザーオブジェクト
	 * @return 更新成功時はプロフィール画面へのリダイレクト、失敗時は編集画面のパス
	 */
	@PostMapping("/update")
	public String updateUser(@ModelAttribute @Validated UserEditForm userEditForm, 
					BindingResult bindingResult,
					@AuthenticationPrincipal LoginUser loginUser) {
		
		// 入力チェック
		if(bindingResult.hasErrors()) {
			return "user/edit";
		}
		
		// フォームオブジェクトをUserクラスに変換
		User user = modelMapper.map(userEditForm, User.class);
			
		// ログインユーザーのIDを取得
		Integer userId = loginUser.getUserId();
		
		// 取得したIDをuserオブジェクトにセットする
		user.setId(userId);
		
		// Serviceの更新処理を呼び出す
		// メールアドレス重複などで失敗した場合は、編集画面に戻してエラーを表示します
		if (!userService.updateUser(user)) {
			bindingResult.rejectValue("email", "error.duplicate.email");
			
			return "user/edit";
		}
		
		// 更新が成功したら、ユーザーページへリダイレクト
		return "redirect:/user/profile";
	}
	
}
