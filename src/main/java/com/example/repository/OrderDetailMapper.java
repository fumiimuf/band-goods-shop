package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.OrderDetail;

@Mapper
public interface OrderDetailMapper {

	// 注文詳細情報を登録
	int insert(OrderDetail orderDetail);
	
	// 注文IDからその注文の明細(子)を「すべて」取得する
	List<OrderDetail> findByOrderId(Integer userId);
	
}
