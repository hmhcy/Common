package com.hxhy.config.aspect;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.config.exception.PlatformRestException;
import com.hxhy.config.util.ConstantUtils;


/**
 * 操作日志切面
 * 
 * @author Abner
 */
@Aspect
@Component
public class ControllerAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);
//	final static String CONTROLLER_EXECUTION = "execution(public com.hxhy.api.responseEntity.ResultBean *(..))";
	final static String CONTROLLER_ANNOTATION = "within(@org.springframework.web.bind.annotation.RestController *) && " +
			"@annotation(org.springframework.web.bind.annotation.RequestMapping) && "+
			"execution(public com.hxhy.api.responseEntity.ResultBean *(..))";
	@Autowired
	HttpServletRequest request;
	
	@SuppressWarnings("unused")
	private Map<String, String> ACTION_ID_MAP = new HashMap<String, String>();

	@Pointcut(CONTROLLER_ANNOTATION)
	public void controller() {
	}

	@Around("controller()")
	public Object handleController(ProceedingJoinPoint joinPoint) throws Throwable {
		ResultBean<?> result;
		try {
			
			Object[] intA = joinPoint.getArgs();
			String args = "";
			for(int i=0;i<intA.length;i++){
				args += intA[i];
			}
/*			LOGGER.info(
					joinPoint.getSignature() + " request: " + args);*/
			LOGGER.info("Request URL::" + request.getServletPath().toString() + "	data::" +  args);
			result = (ResultBean<?>) joinPoint.proceed();
			LOGGER.info(joinPoint.getSignature() + " response: " + JSON.toJSONString(result));
			return result;
		} catch (Throwable e) {
			return handleException(joinPoint, e);
		}
	}

	@SuppressWarnings("rawtypes")
	public Object handleException(ProceedingJoinPoint jp, Throwable e) throws Throwable {
		ResultBean<?> result = new ResultBean();

		// 已知异常
		if (e instanceof PlatformRestException) {
			result.setMsg(e.getLocalizedMessage());
			result.setCode(ConstantUtils.RESULT_FAILED);
		} else {
			LOGGER.error(jp.getSignature() + "error", e);

			result.setMsg(e.getMessage());
			result.setCode(ConstantUtils.RESULT_FAILED);

			// TODO 未知异常应该重点关注， 这里可以做一些其他操作， 如通知邮件， 单独写到某个文件， 发送企业微信消息等等
		}
		return result;
	}

}
