package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.model.LoginUser;



@Controller
public class HomeController {

	@GetMapping("/")
	public String index(@AuthenticationPrincipal LoginUser loginUser) {
		
		if (loginUser == null) {
			return "redirect:/login";
		}
		
		boolean isAdmin = AuthorityUtils.authorityListToSet(loginUser.getAuthorities()).contains("ROLE_ADMIN");
		
		
		if (isAdmin) {
			return "redirect:/admin/goods/index";
		}
		
		return "redirect:/goods/index";
	}
	
	@GetMapping("/access-denied")
	public String accessDenied(@AuthenticationPrincipal LoginUser loginUser,
					RedirectAttributes redirectAttributes) {
		
		redirectAttributes.addFlashAttribute("toastError", "toast.error.notFound");
		
		if (loginUser == null) {
            return "redirect:/login";
        }

        boolean isAdmin = AuthorityUtils.authorityListToSet(loginUser.getAuthorities()).contains("ROLE_ADMIN");
        if (isAdmin) {
            return "redirect:/admin/goods/index";
        }
        
        return "redirect:/goods/index";
	}
	
}
