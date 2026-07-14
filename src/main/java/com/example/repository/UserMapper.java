package com.example.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.entity.User;

@Mapper
public interface UserMapper {

	int selectCountByEmail(String email);
	
	User selectByEmail(String email);
	
	public void insertOne(User user);
	
	User selectById(Integer userId);
	
	void updateUser(User user);
	
	List<User> selectAllUsers(
			@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
	
	int selectCountAllUsers(String keyword);
}
