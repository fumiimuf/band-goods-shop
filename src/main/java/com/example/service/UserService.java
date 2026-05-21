package com.example.service;

import com.example.entity.User;

public interface UserService {

	public boolean insertOne(User user);
	
	User findById(Integer userId);
}
