package com.example.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserMapper;
import com.example.service.UserService;

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
	public void insertOne(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insertOne(user);
	}

	@Override
	public User findById(Integer userId) {
		return userMapper.selectById(userId);
	}

	@Override
	public void updateUser(User user) {
		
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
		} else {
			User currentUser = userMapper.selectById(user.getId());
			user.setPassword(currentUser.getPassword());
		}
		
		userMapper.updateUser(user);
	}

	@Override
	public List<User> getAllUsers(String keyword, int page, int size) {
		
		int offset = page * size;
		
		return userMapper.selectAllUsers(keyword, size, offset);
	}

	@Override
	public int getCountUsers(String keyword) {
		return userMapper.selectCountAllUsers(keyword);
	}
}
