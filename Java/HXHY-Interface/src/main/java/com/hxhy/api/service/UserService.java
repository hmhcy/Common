package com.hxhy.api.service;

import javax.servlet.http.HttpSession;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.model.User;
import com.hxhy.api.responseEntity.ResultBean;


public interface UserService{
	
	/**
	 * 获取当前用户
	 */
	JSONObject getCurrentUser(JSONObject user);
	/**
	 * 使用账号查询用户
	 */
	User getByAccount(String userName);
	
	/**
	 * 使用手机号码查询用户
	 */
	User getByPhone(String userPhone);
	/**
	 * 用户登录
	 */
	ResultBean<String> login(String userName, String pwd, Integer rememberMe,HttpSession session);
	
	/**
	 * 用户手机快捷登录 
	 * @param userPhone
	 * @param captcha
	 * @param session
	 * @return
	 */
	
	ResultBean<String> smsLogin(String userPhone, String captcha, HttpSession session);
	/**
	 * 用户注册
	 */
	ResultBean<String> register(User record);
	
	/**
	 * 修改用户信息
	 */
	ResultBean<String> updateUserInfo(User record);
	
	/**
	 * 修改用户密码
	 * 
	 * @param oldPassWord
	 *            旧密码
	 * @param newPassWord
	 *            新密码
	 * @return
	 */
	ResultBean<String> updatePwd(String newPassWord,String... oldPassWord);
	
	/**
	 * 用户头像上传
	 */
	ResultBean<JSONObject> uploadAvatar(String file);
	
	Integer logout();
	
	ResultBean<String> updateUserPhone(String userPhone, String oldPhone, String captcha,HttpSession session);

}
