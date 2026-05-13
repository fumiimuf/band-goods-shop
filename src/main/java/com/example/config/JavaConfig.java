package com.example.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@Configuration
public class JavaConfig {

	// マッピングの機能
	@Bean
	public ModelMapper modelMapper() {
		
		return new ModelMapper();
	}
	
	// レイアウトの機能
	@Bean
	public LayoutDialect layoutDialect() {
		
		return new LayoutDialect();
	}
}
