package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterForm {

	@NotBlank
	private String name;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]{8,}$")
	private String password;
	
	@NotBlank
	@Pattern(regexp = "^[0-9]{3}-?[0-9]{4}$")
	private String zipCode;
	
	@NotBlank
	@Size(max = 100)
	private String address;
	
}
