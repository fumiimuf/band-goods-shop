package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.Order;

@Mapper
public interface OrderMapper {

	// 注文情報を登録(親テーブル)
	int insert(Order order);
	
	// ログインユーザーの注文履歴(親)を「全件」取得
	List<Order> findByUserId(Integer userId);
}
