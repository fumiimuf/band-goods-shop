package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.OrderDetail;

@Mapper
public interface OrderDetailMapper {

	// 注文詳細情報を登録
	int insert(OrderDetail orderDetail);
}
