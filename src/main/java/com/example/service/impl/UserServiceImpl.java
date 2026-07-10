package com.example.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.model.PageResult;
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
		// パスワードの暗号化
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userMapper.insertOne(user);
	}

	@Override
	public User findById(Integer userId) {
		return userMapper.selectById(userId);
	}

	// ユーザー情報を更新する
	@Override
	public void updateUser(User user) {
		
		// パスワードの変更有無による処理分岐
		// 画面で新しいパスワードが入力されているかチェック
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			
			// 入力されている場合：パスワードを暗号化してセットする
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
		} else {
			// 空の場合：パスワード変更しないので、DBの元のパスワードをそのままセット
			User currentUser = userMapper.selectById(user.getId());
			user.setPassword(currentUser.getPassword());
		}
		
		// DBのデータを更新する
		userMapper.updateUser(user);
	}

	@Override
	public List<User> findGeneralUsers(String keyword, int page, int size) {
		
		int offset = page * size;
		
		return userMapper.selectAllUsers(keyword, size, offset);
	}

	@Override
	public int countGeneralUsers(String keyword) {
		return userMapper.selectCountAllUsers(keyword);
	}

	@Override
	public PageResult<User> getAdminUserPage(String keyword, int page) {
		
		int size = 5;
		
		List<User> userList = findGeneralUsers(keyword, page, size);
		
		int totalUsers = countGeneralUsers(keyword);
		
		PageResult<User> result = new PageResult<User>(userList, page, totalUsers, size);
		
		return result;
	}

	
	
	
}
