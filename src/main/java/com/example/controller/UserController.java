package com.example.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.entity.User;
import com.example.form.RegisterForm;
import com.example.service.UserService;

import lombok.extern.slf4j.Slf4j;



@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	/**
	 * ユーザー登録画面を表示します。
	 * 
	 * @param registerForm 画面の入力値を保持するフォームオブジェクト
	 * @return ユーザー登録画面のテンプレートパス
	 */
	@GetMapping("/register")
	public String getRegister(@ModelAttribute RegisterForm registerForm) {
		
		return "user/register";
	}
	
	/**
	 * ユーザー登録処理を実行します。
	 * バリデーションがある場合は登録画面に戻り、正常の場合はログイン画面へ遷移します。
	 * 
	 * @param registerForm 画面から送信された入力内容
	 * @param bindingResult バリデーションの結果。エラー有無を確認するために使用
	 * @return 登録成功時はログイン画面へのリダイレクト、失敗時は登録画面のパス
	 */
	@PostMapping("/register")
	public String postRegister(@ModelAttribute @Validated RegisterForm registerForm, 
			BindingResult bindingResult) {
		
		// 入力チェック
		if (bindingResult.hasErrors()) {
			log.warn("登録内容に不備があります：{}", bindingResult.getAllErrors());
			
			return "user/register";
		}
		
		// formをUserクラスに変換
		User user = modelMapper.map(registerForm, User.class);
		
		log.info("登録処理を開始します：Email={}, Name={}", registerForm.getEmail(), registerForm.getName());
		
		
		if (!userService.insertOne(user)) {
			log.warn("登録失敗：メールアドレスが既に存在します：{}", user.getEmail());
			
			// 第1引数に"email"を指定することで、画面のメールアドレス入力欄にエラーを表示します
			bindingResult.rejectValue("email", "error.duplicate");
			
			return "user/register";
		}
		
		log.info("ユーザー登録が正常に完了しました：{}", user.getEmail());
		
		return "redirect:/login?registerSuccess";
	}
	
}
