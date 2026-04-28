package com.example.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserMapper userMapper;
	
	public UserDetailsServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userMapper.findByEmail(email);
		
		if (user == null) {
			
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		
		String role = (user.getIsAdmin() != null && user.getIsAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
		
		return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword()) 
                .authorities(AuthorityUtils.createAuthorityList(role))
                .build();
	}
}
