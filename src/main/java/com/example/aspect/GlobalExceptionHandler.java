package com.example.aspect;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice // アプリケーション全体のControllerを一括で見張る設定
@Slf4j // Eclipseのコンソールにログ（log.error）を出力するためのLombok
public class GlobalExceptionHandler {

	/**
	 * ① データベース関連の例外処理
	 * SQLエラーやMySQL接続切れなど、データアクセスに関するバグをここでキャッチします。
	 */
	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e) {
		
		// 画面にはエラーの詳細を見せない代わり、コンソールに分かりやすく「【DBエラー】」と出力
		log.error("【DBエラー】データベース処理中に例外が発生しました。", e);
		
		// 共通エラー画面（src/main/resources/templates/error.html）を表示
		return "error";
	}
	
	/**
	 * ② その他のすべての例外処理
	 * ぬるぽ（NullPointerException）など、プログラム上の予期せぬJavaのバグをすべてここでキャッチします。
	 */
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e) {
		
		// コンソールに分かりやすく「【システムエラー】」と出力
		log.error("【システムエラー】予期せぬ例外が発生しました。", e);
		
		// 共通エラー画面（src/main/resources/templates/error.html）を表示
		return "error";
	}
}