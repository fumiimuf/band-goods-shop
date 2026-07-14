package com.example.aspect;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice 
@Slf4j 
public class GlobalExceptionHandler {

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
}