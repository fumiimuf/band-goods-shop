package com.example.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.OrderViewItem;
import com.example.service.OrderService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

	private final OrderService orderService;
	
	@GetMapping("/list")
	public String showOrderList(
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		
		int size = 5;
		
		List<OrderViewItem> orderList = orderService.getAllOrderHistoryByPage(keyword, page, size);
		
		long totalOrders = orderService.countAllOrders(keyword);
		int totalPages = (int) Math.ceil((double) totalOrders / size);
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", keyword);
		
		return "admin/order/list";
	}
	
}
