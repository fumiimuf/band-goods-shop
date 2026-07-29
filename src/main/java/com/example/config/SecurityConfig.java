package com.example.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    
		http.formLogin(login -> login
				.loginPage("/login")
				.usernameParameter("email")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login?error")
				.permitAll()
			)
			.logout(logout -> logout
				.logoutSuccessUrl("/login?logout")
				.permitAll()
			)
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
					.requestMatchers("/login", "/user/register").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.requestMatchers("/goods/**", "/cart/**", "/order/**", "/user/**").hasRole("USER")
					.anyRequest().authenticated()
			)
			.exceptionHandling(exception -> exception.accessDeniedPage("/access-denied")
					
			);
			        
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
