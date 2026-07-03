package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.User;

@Mapper
public interface UserMapper {

	// メールアドレスでユーザー情報を1件取得
	User selectByEmail(String email);
	
	// ユーザー情報1件を登録する
	public void insertOne(User user);
	
	// IDでユーザー情報を1件取得
	User selectById(Integer userId);
	
	// ユーザー情報を更新
	void updateUser(User user);
	
	// 一般ユーザー全員を取得する
	List<User> selectAllUsers(
			@Param("keyword") String keyword,
			@Param("limit") int limit, 
			@Param("offset") int offset);
	
	int selectCountAllUsers(String keyword);
}
