package com.github.fumiimuf.bandgoods.repository;

import org.apache.ibatis.annotations.Mapper;

import com.github.fumiimuf.bandgoods.entity.OrderDetail;

@Mapper
public interface OrderDetailMapper {

	int insert(OrderDetail orderDetail);
}
