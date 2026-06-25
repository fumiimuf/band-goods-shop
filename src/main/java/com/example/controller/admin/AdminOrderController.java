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
		
		long totalCount = orderService.countAllOrders(keyword);
		int totalPages = (int) Math.ceil((double) totalCount / size);
		
		int displayButtonCount = 3;
		
		int startPage = Math.max(0, page - (displayButtonCount / 2));
		
		int endPage = Math.min(totalPages - 1, startPage + displayButtonCount - 1);
		
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", page);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "admin/order/list";
	}
	
}
