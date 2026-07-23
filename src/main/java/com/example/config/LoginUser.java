package com.example.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginUser extends User {

	private final Integer userId;
	
	private String name;
	
	private String email;

	// コンストラクタ：親クラス(User)に認証情報を渡しつつ、各フィールド値をセット
	public LoginUser(com.example.entity.User user, Collection<? extends GrantedAuthority> authorities) {
		super(user.getEmail(), user.getPassword(), authorities);
		this.userId = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
	}

	// IDを取り出すためのメソッド
	public Integer getUserId() {
		return userId;
	}
	
	// 名前を取り出すためのメソッド
	public String getName() {
		return name;
	}
	
	// メールアドレスを取り出すためのメソッド
	public String getEmail() {
		return email;
	}
	
	// 外部から最新の名前を受け取って上書きするメソッド
	public void setName(String name) {
		this.name = name;
	}
}
