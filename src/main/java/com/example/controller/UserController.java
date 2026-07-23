package com.example.controller;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.config.LoginUser;
import com.example.entity.User;
import com.example.form.UserEditForm;
import com.example.form.UserRegisterForm;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	private final ModelMapper modelMapper;
	
	// ユーザー登録情報を表示
	@GetMapping("/register")
	public String showRegister(@ModelAttribute UserRegisterForm userRegisterForm) {
		
		return "user/register";
	}
	
	// ユーザー登録処理
	@PostMapping("/register")
	public String registerUser(@ModelAttribute @Validated UserRegisterForm userRegisterForm, 
			BindingResult bindingResult, 
			RedirectAttributes redirectAttributes) {
		
		if (userService.existEmail(userRegisterForm.getEmail())) {
			bindingResult.rejectValue("email", "error.duplicate.email");
		}
		
		if (bindingResult.hasErrors()) {
			return "user/register";
		}
		
		User user = modelMapper.map(userRegisterForm, User.class);
		
		userService.insertOne(user);
		
		redirectAttributes.addFlashAttribute("registerSuccess", "msg.user.register.completeUserRegister");
		
		return "redirect:/login";
	}
	
	// ユーザーページ画面を表示
	@GetMapping("/profile")
	public String showUserProfile(@AuthenticationPrincipal LoginUser loginUser, Model model) {
		
		Integer userId = loginUser.getUserId();
		
		User user = userService.findById(userId);
		
		model.addAttribute("user", user);
		
		return "user/profile";
	}
	
	// ユーザー編集画面の表示
	@GetMapping("/edit")
	public String showUserEdit(@AuthenticationPrincipal LoginUser loginUser, 
					@ModelAttribute UserEditForm userEditForm) {
		
		Integer userId = loginUser.getUserId();
		
		User user = userService.findById(userId);
		
		modelMapper.map(user, userEditForm);
		
		userEditForm.setPassword("");
		
		return "user/edit";
	}
	
	// ユーザー更新処理
	@PostMapping("/update")
	public String updateUser(@ModelAttribute @Validated UserEditForm userEditForm, 
					BindingResult bindingResult,
					@AuthenticationPrincipal LoginUser loginUser, 
					RedirectAttributes redirectAttributes) {
		
		if (!userEditForm.getEmail().equals(loginUser.getEmail())) {
			if (userService.existEmail(userEditForm.getEmail())) {
				bindingResult.rejectValue("email", "error.duplicate.email");
			}
		}
		
		if(bindingResult.hasErrors()) {
			return "user/edit";
		}
		
		User user = modelMapper.map(userEditForm, User.class);
		
		user.setId(loginUser.getUserId());
		
		userService.updateUser(user);
		
		if (loginUser != null) {
			loginUser.setName(user.getName());
		}
		
		redirectAttributes.addFlashAttribute("editSuccess", "msg.user.profile.completeUserEdit");
		
		return "redirect:/user/profile";
	}
}
