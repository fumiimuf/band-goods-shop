package com.github.fumiimuf.bandgoods.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.fumiimuf.bandgoods.entity.User;
import com.github.fumiimuf.bandgoods.model.LoginUser;
import com.github.fumiimuf.bandgoods.repository.UserMapper;

@Service
public class LoginUserDetailsImpl implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userMapper.selectByEmail(email);
		
		if (user == null) {
			
			throw new UsernameNotFoundException("メールアドレスが見つかりません。: " + email);
		}
		
		String role = (user.getIsAdmin() != null && user.getIsAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
		
		return new LoginUser(user, AuthorityUtils.createAuthorityList(role));
	}
}
