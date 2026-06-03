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

	// ユーザー情報を更新する
	@Override
	public boolean updateUser(User user) {
		
		// メールアドレスの重複チェック
		User existingUser = userMapper.findByEmail(user.getEmail());
		
		// 同じメールアドレスが存在し、かつ、そのIDが自分自身のIDと異なる場合はと重複エラー
		if (existingUser != null && !existingUser.getId().equals(user.getId())) {
			return false;
		}
		
		// パスワードの変更有無による処理分岐
		// 画面で新しいパスワードが入力されているかチェック
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			
			// 入力されている場合：パスワードを暗号化してセットする
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
		} else {
			// 空の場合：パスワード変更しないので、DBの元のパスワードをそのままセット
			User currentUser = userMapper.findById(user.getId());
			user.setPassword(currentUser.getPassword());
		}
		
		// DBのデータを更新する
		userMapper.updateUser(user);
		
		return true;
	}

	@Override
	public List<User> findGeneralUsers(int page, int size) {
		
		int offset = page * size;
		
		return userMapper.findGeneralUsers(size, offset);
	}

	@Override
	public int countGeneralUsers() {
		return userMapper.countGeneralUsers();
	}
	
	
}
