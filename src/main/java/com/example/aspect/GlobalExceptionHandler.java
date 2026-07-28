package com.example.aspect;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authorization.AuthorizationDeniedException;
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
	
	// 権限エラー関連の例外処理
	@ExceptionHandler(AuthorizationDeniedException.class)
	public String handleAccessDeniedException(
			Exception e, 
			Authentication authentication, 
			RedirectAttributes redirectAttributes) {
		
		log.warn("【403エラー】アクセス権限がないページへアクセスされました。", e);
		
		// トースト通知用のメッセージキーをセット
		redirectAttributes.addFlashAttribute("toastError", "messages.propertiesで後で定義します。");
		
		// 権限に応じたトップページへリダイレクト
		return "redirect:" + getTopUrlByRole(authentication);
	}

	// データベース関連の例外処理
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e) {
		
		log.error("【DBエラー】データベース処理中に例外が発生しました。", e);
		
		// 共通エラー画面を表示
		return "error";
	}
	
	// その他のすべての例外処理
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e) {
		
		log.error("【システムエラー】予期せぬ例外が発生しました。", e);
		
		// 共通エラー画面を表示
		return "error";
	}
	
	// ログイン中の権限(Role)に応じて適切なトップページURLを返却するプライベートメソッド
	private String getTopUrlByRole(Authentication authentication) {
		// 未ログインの場合
		if (authentication == null || !authentication.isAuthenticated()) {
			return "/login";
		}
		
		// 管理者(ADMIN)の場合
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return "/admin/goods/"; // 管理者用トップ
		}
		
		// 一般ユーザー(USER)の場合
		return "/goods/"; // 一般ユーザー用トップ
	}
}