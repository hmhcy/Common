package com.hxhy.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer{
	
	@Autowired
	private MyInterceptor myInterceptor;
	
	@Autowired
	private AuthorizeInterceptor authorInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
		// excludePathPatterns 用户排除拦截
		
		//登录拦截
		registry.addInterceptor(myInterceptor).addPathPatterns("/user/**","/order/**")
		.excludePathPatterns("/user/login/**","/user/register/**");
		
		//账号实名认证拦截
		registry.addInterceptor(authorInterceptor).addPathPatterns("/order/**");
	}
}
