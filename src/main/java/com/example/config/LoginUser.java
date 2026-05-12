package com.example.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

//Spring標準の User クラスを継承（extends）します
public class LoginUser extends User {

	// 自分だけの追加項目：ユーザーID
	private final Integer userId;
	
	// 自分だけの追加項目：名前
	private final String name;

	// コンストラクタ：親クラスのコンストラクタを呼びつつ、IDをセットします
	public LoginUser(com.example.entity.User user, Collection<? extends GrantedAuthority> authorities) {
		// super(...) は親クラス（User）のコンストラクタを呼び出す合図です
		super(user.getEmail(), user.getPassword(), authorities);
		this.userId = user.getId();
		this.name = user.getName();
	}

	// IDを取り出すためのメソッド
	public Integer getUserId() {
		return userId;
	}
	
	// 名前を取り出すためのメソッド
	public String getName() {
		return name;
	}
}
