package com.hxhy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.api.service.SmsService;
import com.hxhy.config.util.ConstantUtils;
import com.hxhy.config.util.RandomUtils;
import com.hxhy.config.util.StringUtils;
import com.hxhy.redis.RedisUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "sms")
@Api(value = "短信接口", tags = { "短信接口" })
public class SMSController {
	@Autowired
	private SmsService smsService;	
	@Autowired
	private RedisUtil redisUtil;
	
	@ApiOperation(value = "获取验证码短信")
	@RequestMapping(value = "/getCaptchaCode", method = RequestMethod.POST)
	public ResultBean<String> getCaptchaCode(@RequestParam(value="用户手机号") String userPhone,
			@RequestParam(name="验证码用途(1:身份验证；2:登录确认；3:用户注册；)") Integer type) {
		ResultBean<String> result = new ResultBean<String>();
		String code = RandomUtils.randomNum(6);
		if (StringUtils.isEmpty(userPhone)) {
			result.setCode(-1);
			result.setMsg("手机号不能为空");
		}else {
			try {
				String smsCode = ConstantUtils.SMS_AUTH;
				switch(type) {
					case 1: smsCode = ConstantUtils.SMS_AUTH;break;
					case 2: smsCode = ConstantUtils.SMS_LOGIN_COMFIRM;break;
					case 3: smsCode = ConstantUtils.SMS_REGISTER_CODE;break;
				}
				
				if(smsService.sendSms(userPhone, code, smsCode)) {
					result.setCode(0);
					result.setMsg("发送成功");
			        redisUtil.set(userPhone,code,60);
				}else {
					result.setCode(-100);
					result.setMsg("发送失败");
				}
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setCode(-100);
				result.setMsg("发送失败");
			}
		}
		return result;
	}
}
