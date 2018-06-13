package com.hxhy.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import com.hxhy.config.cache.UserCache;

/**
 * 过滤器
 * 
 * @author Insertor
 *
 */
@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(AuthorizeInterceptor.class);

	/** * 在请求处理之前进行调用（Controller方法调用之前）调用, * 返回true 则放行， false 则将直接跳出方法 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		try {
			if(UserCache.get() == null){
				 response.setStatus(401);
			     return false;
			}else {
				if(UserCache.get("status").equals("0")) { //还未实名认证过;
					response.sendError(401,"未实名认证");
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