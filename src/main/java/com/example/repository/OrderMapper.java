package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.Order;

@Mapper
public interface OrderMapper {

	// 注文情報を登録(親テーブル)
	int insert(Order order);
}
