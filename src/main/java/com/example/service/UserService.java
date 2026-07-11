package com.example.service;

import java.util.List;

import com.example.entity.User;
import com.example.model.Pagination;

public interface UserService {
	
	boolean existEmail(String email);

	void insertOne(User user);
	
	User findById(Integer userId);
	
	void updateUser(User user);
	
	List<User> findGeneralUsers(String keyword, int page, int size);
	
	int countGeneralUsers(String keyword);
	
	Pagination<User> getAdminUserPage(String keyword, int page);
}
