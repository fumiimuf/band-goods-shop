package com.github.fumiimuf.bandgoods.model;

import java.util.List;

import com.github.fumiimuf.bandgoods.entity.Order;
import com.github.fumiimuf.bandgoods.entity.OrderDetail;

import lombok.Data;

@Data
public class OrderViewItem {

	private Order order;
	
	private List<OrderDetail> details;
}
