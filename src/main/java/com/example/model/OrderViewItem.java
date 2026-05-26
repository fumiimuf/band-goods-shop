package com.example.model;

import java.util.List;

import com.example.entity.Order;
import com.example.entity.OrderDetail;

import lombok.Data;

@Data
public class OrderViewItem {

	private Order order;
	
	private List<OrderDetail> details;
}
