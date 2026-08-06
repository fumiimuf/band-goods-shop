package com.github.fumiimuf.bandgoods.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.fumiimuf.bandgoods.entity.User;
import com.github.fumiimuf.bandgoods.repository.UserMapper;
import com.github.fumiimuf.bandgoods.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public boolean existEmail(String email) {
		return userMapper.selectCountByEmail(email) > 0;
	}
	
	@Override
	public void insert(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insert(user);
	}

	@Override
	public User findById(Integer userId) {
		return userMapper.selectById(userId);
	}

	@Override
	public void update(User user) {
		
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
		} else {
			User currentUser = userMapper.selectById(user.getId());
			user.setPassword(currentUser.getPassword());
		}
		
		userMapper.update(user);
	}

	@Override
	public List<User> getAllUsers(String keyword, int page, int size) {
		
		int offset = page * size;
		
		return userMapper.selectUsers(keyword, size, offset);
	}

	@Override
	public int getCountUsers(String keyword) {
		return userMapper.selectCountUsers(keyword);
	}
}
