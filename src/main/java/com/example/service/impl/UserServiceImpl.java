package com.example.service.impl;

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
	public boolean insertOne(User user) {
		
		// メールアドレスの重複チェック
		User existingUser = userMapper.findByEmail(user.getEmail());
		if (existingUser != null) {
			// すでに登録されている場合は、falseを返す
			return false;
		}
		
		// パスワードの暗号化
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		// 登録処理の実行
		userMapper.insertOne(user);
		
		// 成功したらtrueを返す
		return true;
		
	}

	@Override
	public User findById(Integer userId) {
		return userMapper.findById(userId);
	}
	
	
}
