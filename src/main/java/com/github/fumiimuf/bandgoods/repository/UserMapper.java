package com.github.fumiimuf.bandgoods.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.fumiimuf.bandgoods.entity.User;

@Mapper
public interface UserMapper {

	int selectCountByEmail(String email);
	
	User selectByEmail(String email);
	
	public void insert(User user);
	
	User selectById(Integer userId);
	
	void update(User user);
	
	List<User> selectUsers(
			@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);
	
	int selectCountUsers(String keyword);
}
