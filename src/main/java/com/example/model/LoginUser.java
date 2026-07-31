package com.example.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginUser extends User {

	private final Integer userId;
	
	@Setter
	private String name;
	
	private String email;

	// コンストラクタ：親クラス(User)に認証情報を渡しつつ、各フィールド値をセット
	public LoginUser(com.example.entity.User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getEmail(), user.getPassword(), authorities);
		this.userId = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
	}
}
