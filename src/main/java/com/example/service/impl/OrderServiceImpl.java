package com.example.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.User;
import com.example.model.CartItem;
import com.example.model.OrderViewItem;
import com.example.model.Pagination;
import com.example.repository.OrderDetailMapper;
import com.example.repository.OrderMapper;
import com.example.service.CartService;
import com.example.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderMapper orderMapper;
	private final OrderDetailMapper orderDetailMapper;
	private final CartService cartService;
	
	@Override
	@Transactional
	public void createOrder(User user, List<CartItem> cartList, int totalAmount) {
		
		for (CartItem item : cartList) {
			if (item.getGoods() != null && item.getGoods().getIsDeleted() != null && item.getGoods().getIsDeleted()) {
				throw new IllegalStateException(item.getGoods().getName() + " は販売終了しているため、注文できません。");
			}
		}
		
		Order order = new Order();
		order.setUserId(user.getId());
		order.setOrderedName(user.getName());
		order.setOrderedZipCode(user.getZipCode());
		order.setOrderedAddress(user.getAddress());
		order.setTotalPrice(totalAmount);
		
        orderMapper.insert(order);
        
        for (CartItem item : cartList) {
        	OrderDetail detail = new OrderDetail();
        	
        	detail.setOrderId(order.getId());
        	detail.setOrderedImage(item.getGoods().getImage());
			detail.setOrderedName(item.getGoods().getName());
			detail.setOrderedPrice(item.getGoods().getPrice());
			detail.setQuantity(item.getQuantity());
			
			orderDetailMapper.insert(detail);
        }
		 cartService.deleteAllByUserId(user.getId());
	}
	
	@Override
	public List<OrderViewItem> getOrderHistoryByPage(Integer userId, int page, int size) {
		
		List<OrderViewItem> historyList = new ArrayList<>();
		
		int offset = page * size;
		
		List<Order> orders = orderMapper.selectOrdersWithDetailsByUserIdByPage(userId, size, offset);
		
		for (Order order : orders) {
			OrderViewItem viewItem = new OrderViewItem();
			
			viewItem.setOrder(order);
			
			viewItem.setDetails(order.getDetails());
			
			historyList.add(viewItem);
		}
		
		return historyList;
	}

	@Override
	public long countByUserId(Integer userId) {
		
		return orderMapper.selectCountByUserId(userId);
	}

	@Override
	public List<OrderViewItem> getAllOrderHistoryByPage(String keyword, int page, int size) {
		
		List<OrderViewItem> historyList = new ArrayList<>();
		
		int offset = page * size;
		
		List<Order> orders = orderMapper.selectAllOrdersWithDetailsByPage(keyword, size, offset);
		
		for (Order order : orders) {
			OrderViewItem viewItem = new OrderViewItem();
			
			viewItem.setOrder(order);
			
			viewItem.setDetails(order.getDetails());
			
			historyList.add(viewItem);
		}
		return historyList;
	}

	@Override
	public long countAllOrders(String keyword) {
		return orderMapper.selectCountAllOrders(keyword);
	}

	@Override
	public Pagination<OrderViewItem> getOrderPage(Integer userId, int page) {
		
		int size = 2;
		
		List<OrderViewItem> historyList = getOrderHistoryByPage(userId, page, size);
		
		long totalCount = countByUserId(userId);
		
		Pagination<OrderViewItem> result = new Pagination<OrderViewItem>(historyList, page, totalCount, size);
		
		return result;
	}

	@Override
	public Pagination<OrderViewItem> getAdminOrderPage(String keyword, int page) {
		
		int size = 5;
		
		List<OrderViewItem> orderList = getAllOrderHistoryByPage(keyword, page, size);
		
		long totalCount = countAllOrders(keyword);
		
		Pagination<OrderViewItem> result = new Pagination<OrderViewItem>(orderList, page, totalCount, size);
		
		return result;
	}
}
