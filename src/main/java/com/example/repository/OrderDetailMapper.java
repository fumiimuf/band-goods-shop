package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.entity.OrderDetail;

@Mapper
public interface OrderDetailMapper {

	int insert(OrderDetail orderDetail);
}
