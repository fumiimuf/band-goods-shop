package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.User;

@Mapper
public interface UserMapper {

	/*
	 * ユーザー全権取得
	 * @return ユーザーリスト
	 */
	List<User> findAll();
	
	/*
	 * IDでユーザーを1件取得
	 * @param id ユーザーID
	 * @return ユーザー情報
	 */
	User findById(Integer id);
}
