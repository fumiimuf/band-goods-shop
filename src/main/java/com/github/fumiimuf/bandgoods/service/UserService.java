package com.github.fumiimuf.bandgoods.service;

import java.util.List;

import com.github.fumiimuf.bandgoods.entity.User;

public interface UserService {
	
	boolean existEmail(String email);

	void insert(User user);
	
	User findById(Integer userId);
	
	void update(User user);
	
	List<User> getAllUsers(String keyword, int page, int size);
	
	int getCountUsers(String keyword);
}
