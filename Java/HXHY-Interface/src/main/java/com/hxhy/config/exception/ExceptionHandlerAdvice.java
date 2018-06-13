package com.hxhy.config.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.baomidou.kaptcha.exception.KaptchaException;
import com.baomidou.kaptcha.exception.KaptchaIncorrectException;
import com.baomidou.kaptcha.exception.KaptchaNotFoundException;
import com.baomidou.kaptcha.exception.KaptchaTimeoutException;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.config.util.ConstantUtils;

import redis.clients.jedis.exceptions.JedisException;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

     private static final Logger LOGGER =
     LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    /**
     * template
     * @return 
     * @return 
     * 
     * @ResponseStatus(HttpStatus.XXX_XX) @ExceptionHandler(XxxException.
     *                                    class) @ResponseBody public ErrorInfo
     *                                    handleXxx(XxxException e) { return new
     *                                    ErrorInfo("Content ...", e); }
     */
	
	
	@ExceptionHandler(value=JedisException.class)
	public String ErrorInfohandle(JedisException e) { 
		LOGGER.error("Redis连不上啦!");
		return "Redis连不上啦!";
	}
	
	@ExceptionHandler(value = KaptchaException.class)
	  public ResultBean<String> kaptchaExceptionHandler(KaptchaException kaptchaException) {
		ResultBean<String> result = new ResultBean<String>();
		String respMsg = "";
	    if (kaptchaException instanceof KaptchaIncorrectException) {
	    	result.setCode(ConstantUtils.KAPTCHA_INCORRECT);
	    	respMsg = "验证码不正确";
	    } else if (kaptchaException instanceof KaptchaNotFoundException) {
	    	result.setCode(ConstantUtils.KAPTCHA_TIME_OUT);
	    	respMsg = "验证码已失效";
	    } else if (kaptchaException instanceof KaptchaTimeoutException) {
	    	result.setCode(ConstantUtils.KAPTCHA_TIME_OUT);
	    	respMsg = "验证码已失效";
	    } else {
	    	result.setCode(ConstantUtils.KAPTCHA_REFRESH_NEED);
	    	respMsg = "验证码渲染失败";
	    }
	    
	   
	    result.setMsg(respMsg);
	    return result;
	  }
}
