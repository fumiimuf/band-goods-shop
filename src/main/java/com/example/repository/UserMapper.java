package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.User;

@Mapper
public interface UserMapper {

	// メールアドレスでユーザー情報を1件取得
	User findByEmail(String email);
}
