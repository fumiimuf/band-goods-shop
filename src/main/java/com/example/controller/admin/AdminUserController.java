package com.example.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.User;
import com.example.model.PageResult;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

	private final UserService userService;
	
	// 一般ユーザー一覧画面を表示する
	@GetMapping("/list")
	public String showUserList(
					@RequestParam(defaultValue = "") String keyword,
					@RequestParam(defaultValue = "0") int page, 
					Model model) {
		
		PageResult<User> pageResult = userService.getAdminUserPage(keyword, page);
		
		model.addAttribute("userList", pageResult.getList());
		model.addAttribute("currentPage", pageResult.getPage());
		model.addAttribute("totalPages", pageResult.getTotalPages());
		model.addAttribute("startPage", pageResult.getStartPage());
		model.addAttribute("endPage", pageResult.getEndPage());
		model.addAttribute("keyword", keyword);
		
		return "admin/user/list";
	}
	
	// 一般ユーザー詳細画面を表示
	@GetMapping("/detail/{id}")
	public String showUserDetail(@PathVariable Integer id, Model model) {
		
		User user = userService.findById(id);
		
		model.addAttribute("user", user);
		
		return "admin/user/detail";
		
	}
	
}
