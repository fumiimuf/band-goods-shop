package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

	
	/**
	 * ログイン画面を表示
	 * @return ログイン画面のHTMLファイル名("login")
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
}
