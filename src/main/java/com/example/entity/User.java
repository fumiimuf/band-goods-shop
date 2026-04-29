package com.example.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class User {

	private Integer id;
	
	private String name;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 8)
	@Pattern(regexp = "^[a-zA-Z0-9]+$")
	private String password;
	
	private String zipCode;
	
	private String address;
	
	private Boolean isAdmin;
}
