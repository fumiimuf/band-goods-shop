package com.example.service;

import java.util.List;

import com.example.entity.User;

public interface UserService {

	public boolean insertOne(User user);
	
	User findById(Integer userId);
	
	boolean updateUser(User user);
	
	List<User> findGeneralUsers(int page, int size);
	
	int countGeneralUsers();
}
