package com.example.entity;

import lombok.Data;

@Data
public class User {

	private Integer id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String zipCode;
	
	private String address;
	
	private Boolean isAdmin;
}
