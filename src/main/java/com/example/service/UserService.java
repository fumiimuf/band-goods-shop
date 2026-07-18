package com.example.service;

import java.util.List;

import com.example.entity.User;

public interface UserService {
	
	boolean existEmail(String email);

	void insertOne(User user);
	
	User findById(Integer userId);
	
	void updateUser(User user);
	
	List<User> getAllUsers(String keyword, int page, int size);
	
	int getCountUsers(String keyword);
}
