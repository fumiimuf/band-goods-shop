package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.Order;

@Mapper
public interface OrderMapper {

	int insert(Order order);
	
	List<Order> selectOrdersWithDetailsByUserIdByPage(@Param("userId") Integer userId, @Param("limit") int limit, @Param("offset") int offset);
	
	long selectCountByUserId(Integer userId);
	
	List<Order> selectAllOrdersWithDetailsByPage(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
	
	long selectCountAllOrders(String keyword);
}
