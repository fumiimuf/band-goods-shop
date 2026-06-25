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
					@RequestParam(defaultValue = "") String keyword,
					@RequestParam(defaultValue = "0") int page, 
					Model model) {
		
		int size = 5;
		
		List<User> userList = userService.findGeneralUsers(keyword, page, size);
		
		int totalUsers = userService.countGeneralUsers(keyword);
		
		int totalPages = (int) Math.ceil((double) totalUsers / size);
		
		int displayButtonCount = 3;
		
		int startPage = Math.max(0, page - (displayButtonCount / 2));
		
		int endPage = Math.min(totalPages - 1, startPage + displayButtonCount - 1);
		
		if (endPage - startPage + 1 < displayButtonCount) {
			startPage = Math.max(0, endPage - displayButtonCount + 1);
		}
		
		model.addAttribute("userList", userList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("keyword", keyword);
		
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
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
