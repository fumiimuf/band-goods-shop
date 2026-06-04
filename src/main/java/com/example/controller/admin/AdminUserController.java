package com.example.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.entity.User;
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
					@RequestParam(name = "keyword", defaultValue = "") String keyword,
					@RequestParam(name = "page", defaultValue = "0") int page, 
					Model model) {
		
		int size = 5;
		
		List<User> userList = userService.findGeneralUsers(keyword, page, size);
		
		int totalUsers = userService.countGeneralUsers(keyword);
		
		int totalPages = (int) Math.ceil((double) totalUsers / size);
		
		model.addAttribute("userList", userList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		
		return "admin/user/list";
	}
	
	// 一般ユーザー詳細画面を表示
	@GetMapping("/detail/{id}")
	public String showUserDetail(@PathVariable("id") Integer id, Model model) {
		
		User user = userService.findById(id);
		
		model.addAttribute("user", user);
		
		return "admin/user/detail";
		
	}
	
}
