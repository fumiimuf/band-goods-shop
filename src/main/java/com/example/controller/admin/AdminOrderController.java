package com.example.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.OrderViewItem;
import com.example.model.Pagination;
import com.example.service.OrderService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

	private final OrderService orderService;
	
	// 販売履歴一覧を表示
	@GetMapping("/list")
	public String showOrderList(
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
		
		Pagination<OrderViewItem> pagination = orderService.getAdminOrderPage(keyword, page);
		
		model.addAttribute("pagination", pagination);
		model.addAttribute("keyword", keyword);
		
		return "admin/order/list";
	}
	
}
