package com.example.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
					@RequestParam(name = "page", defaultValue = "0") int page, 
					Model model) {
		
		int size = 5;
		
		List<User> userList = userService.findGeneralUsers(page, size);
		
		int totalUsers = userService.countGeneralUsers();
		
		int totalPages = (int) Math.ceil((double) totalUsers / size);
		
		model.addAttribute("userList", userList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		
		return "admin/user/list";
	}
	
}
