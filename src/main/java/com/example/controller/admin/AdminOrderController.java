package com.example.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.OrderViewItem;
import com.example.model.PageResult;
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
		
		PageResult<OrderViewItem> pageResult = orderService.getAdminOrderPage(keyword, page);
		
		model.addAttribute("orderList", pageResult.getContent());
		model.addAttribute("totalPages", pageResult.getTotalPages());
		model.addAttribute("currentPage", pageResult.getCurrentPage());
		model.addAttribute("startPage", pageResult.getStartPage());
		model.addAttribute("endPage", pageResult.getEndPage());
		model.addAttribute("keyword", keyword);
		
		return "admin/order/list";
	}
	
}
