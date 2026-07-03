package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Order;

@Mapper
public interface OrderMapper {

	// 注文情報を登録(親テーブル)
	int insert(Order order);
	
	// ログインユーザーの注文履歴(親)を「全件」取得
	List<Order> selectByUserId(Integer userId);
	
	// ログインユーザーの注文履歴(親)を、ページ指定して一部だけ取得（1ページあたりの件数、スキップする件数）
	List<Order> selectByPage(@Param("userId") Integer userId, @Param("limit") int limit, @Param("offset") int offset);
	
	// このユーザーの注文履歴が全部で何件あるか教える
	long selectCountByUserId(Integer userId);
	
	// 一般ユーザー全員の注文履歴を取得する
	List<Order> selectAllOrdersByPage(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
	
	// 一般ユーザー全員の注文履歴が全部で何件あるか教える
	long selectCountAllOrders(String keyword);
}
