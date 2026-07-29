package com.example.aspect;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice 
@Slf4j 
public class GlobalExceptionHandler {
	
	// URL間違い・存在しないページへのアクセス関連の例外処理
	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNotFoundException(
			NoResourceFoundException e, 
			Authentication authentication, 
			RedirectAttributes redirectAttributes) {
		
		log.warn("【404エラー】存在しないページへアクセスされました", e);
		
		redirectAttributes.addFlashAttribute("toastError", "messages.propertiesで後で定義します。");
		
		return "redirect:" + getTopUrlByRole(authentication);
	}
	
	// ログイン中の権限(Role)に応じて適切なトップページURLを返却するプライベートメソッド
	private String getTopUrlByRole(Authentication authentication) {
		// 未ログインの場合
		if (authentication == null || !authentication.isAuthenticated()) {
			return "/login";
		}
		
		// 管理者(ADMIN)の場合
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "/admin/goods/index"; // 管理者用トップ
		}
		
		// 一般ユーザー(USER)の場合
		return "/goods/index"; // 一般ユーザー用トップ
	}
}