package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	// application.properties に書いた独自プロパティの値をここに注入します
	@Value("${upload-directory}")
	private String uploadDirectory;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// ブラウザから /uploaded-images/** でアクセスされたら、
		// パソコンのホームフォルダ内の「band-goods-images」フォルダを見に行くように窓口を作ります
		registry.addResourceHandler("/uploaded-images/**")
						.addResourceLocations("file:" + uploadDirectory);
	}
	
	
}
