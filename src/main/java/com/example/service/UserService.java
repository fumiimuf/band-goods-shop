package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.PageResult;

public interface UserService {
	
	boolean existEmail(String email);

	public void insertOne(User user);
	
	User findById(Integer userId);
	
	boolean updateUser(User user);
	
	List<User> findGeneralUsers(String keyword, int page, int size);
	
	int countGeneralUsers(String keyword);
	
	PageResult<User> getAdminUserPage(String keyword, int page);
}
