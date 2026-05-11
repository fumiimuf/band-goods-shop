package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.config.LoginUser;
import com.example.entity.User;
import com.example.repository.UserMapper;

@Service
public class LoginUserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userMapper.findByEmail(email);
		
		if (user == null) {
			
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		String role = (user.getIsAdmin() != null && user.getIsAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
		
		// 自作したLoginUser を return するようにします
		return new LoginUser(user, AuthorityUtils.createAuthorityList(role));
	}
}
