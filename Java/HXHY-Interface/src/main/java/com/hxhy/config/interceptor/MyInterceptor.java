package com.hxhy.config.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.service.UserService;
import com.hxhy.config.cache.UserCache;
import com.hxhy.config.util.StringUtils;
import com.hxhy.config.util.UserAuth;

/**
 * 过滤器
 * 
 * @author Insertor
 *
 */
@Component
public class MyInterceptor implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(MyInterceptor.class);
	
	@Autowired
	private UserService userService;

	/**
	 * 把我们的拦截器注入为bean
	 * 
	 * @return
	 */
/*	@Bean
	public HandlerInterceptor getMyInterceptor() {
		return new MyInterceptor();
	}*/

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		logger.info("*****************afterCompletion***************");
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		logger.info("*****************postHandle***************");
	}

	/** * 在请求处理之前进行调用（Controller方法调用之前）调用, * 返回true 则放行， false 则将直接跳出方法 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		JSONObject user = null;
		try {
			if(request.getSession().getAttribute("UserInfo") != null) {
				user = (JSONObject) request.getSession().getAttribute("UserInfo");
				UserCache.set(user);
			}else{
				Cookie[] cookies = request.getCookies();
				String token = "";
				  if(cookies != null) {
				      for (int i = 0; i < cookies.length; i++) {
				          if(cookies[i].getName().equals("auth")) {
				        	  token =  cookies[i].getValue();
				        	  break;
				          }
				       }
				   }
//				String token = request.getHeader("AuthKey");
				if (StringUtils.isNotEmpty(token)) {
					user = UserAuth.verifyUser(token);
					if(user != null){
						user = userService.getCurrentUser(user);
						if(user != null){
							UserCache.set(user);
							request.getSession().setAttribute("User", user);
						}
					}
				}
				
				UserCache.set(user);
				if(user == null) {
					response.sendError(401,"用户还未登录");
					return false;
				}
			}
		
		} catch (Exception e) {
			System.out.println(e);
			logger.error(e.getMessage());
			return false;
		}
		
		
		return true;
	}
}