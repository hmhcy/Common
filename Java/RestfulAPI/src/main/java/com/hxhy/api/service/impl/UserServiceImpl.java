package com.hxhy.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.dao.UserMapper;
import com.hxhy.api.model.User;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.api.service.LoginAttemptService;
import com.hxhy.api.service.UserService;
import com.hxhy.config.cache.UserCache;
import com.hxhy.config.util.ConstantUtils;
import com.hxhy.config.util.FTPUtils;
import com.hxhy.config.util.IDUtils;
import com.hxhy.config.util.ImgUtil;
import com.hxhy.config.util.MD5Utils;
import com.hxhy.config.util.RandomUtils;
import com.hxhy.config.util.StringUtils;
import com.hxhy.config.util.UserAuth;
import com.hxhy.redis.RedisUtil;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private FTPUtils ftp;
	
	@Autowired
	ServletContext servletContext;
	
	private String tokenGenerator(User user) {
		JSONObject uObj = new JSONObject();
		uObj.put("userToken", user.getUserToken());
		uObj.put("userName", user.getUsername());
		String auth =  UserAuth.generateAuth(uObj);
		return auth;
	}

	@Override
	@Transactional
	public ResultBean<String> login(String userName, String pwd, Integer rememberMe,HttpSession session) {
		ResultBean<String> result = new ResultBean<String>();
		User user = getByAccount(userName);
//		JSONObject user = userMapper.login(userName,pwd);
		if (user == null) {
			result.setCode(ConstantUtils.USER_NOT_EXIST);
			result.setMsg("用户还未注册");
		} else {
			String salt = user.getPasswordSalt();
			pwd = MD5Utils.encode(pwd+salt);
			
			if (!pwd.equals(user.getPassword())) {
				result.setCode(ConstantUtils.INCORRECT_PWD);
				result.setMsg("密码输入错误");
			} else {
				result.setMsg("登陆成功");
				String oToken = user.getUserToken();
				String userToken = IDUtils.genID();
				user.setUserToken(userToken);
				int i = userMapper.updateByPrimaryKeySelective(user);
				if(i < 1){
					userToken = oToken;	
				}
				if(rememberMe == 1){
					if(redisUtil.hasKey(oToken)){
						redisUtil.del(oToken);
					}
					redisUtil.set(userToken,user.toString(),604800);
				}else{
					redisUtil.set(userToken,user.toString(),10800);
				}
				session.setAttribute("UserInfo", JSONObject.parseObject(user.toString()));
				result.setData(tokenGenerator(user));
			}
		}
		
		return result;
	}
	
	@Override
	@Transactional
	public ResultBean<String> smsLogin(String userPhone, String captcha, HttpSession session) {
		ResultBean<String> result = new ResultBean<String>();
		String code = StringUtils.EMPTY;
		
		code = (String) redisUtil.get(userPhone);
		if(code != null && code.equals(captcha)) { //如果验证码通过
			User user = getByPhone(userPhone); //根据手机号码查询用户
			if (user != null) {//如果用户存在，更新userToken
				String oToken = user.getUserToken();
				String userToken = IDUtils.genID();
				user.setUserToken(userToken);
				int i = userMapper.updateByPrimaryKeySelective(user);
				if(i < 1){
					userToken = oToken;	
				}
				if(redisUtil.hasKey(oToken)){
					redisUtil.del(oToken);
				}
				redisUtil.set(userToken,user.toString(),604800);
				session.setAttribute("UserInfo", JSONObject.parseObject(user.toString()));
				result.setMsg("登陆成功");
				result.setData(tokenGenerator(user));
				 
			} else { //如果用户不存在，新建一个账号
				user = new User();
				String userId = IDUtils.getPostfix(3);
				String userToken = IDUtils.genID();
				user.setId(userId);
				user.setUsername(userPhone);
				user.setPhone(userPhone);
				user.setUserToken(userToken);
				user.setStatus(0);
				int success = userMapper.insert(user);
				if (success <= 0) {
					result.setCode(ConstantUtils.RESULT_FAILED);
					result.setMsg("注册失败");
				} else {
					redisUtil.set(userToken,user.toString(),604800);
					session.setAttribute("UserInfo", JSONObject.parseObject(user.toString()));
					result.setMsg("登陆成功");
					result.setData(tokenGenerator(user));
				}
			}
		}else {
			if(code == null) {
				result.setCode(ConstantUtils.INCORRECT_SMS_CODE);
				result.setMsg("验证码已失效");
			}else {
				result.setCode(ConstantUtils.INCORRECT_SMS_CODE);
				result.setMsg("验证码错误");
			}
		}

		return result;
	}
	
	@Override
	public User getByAccount(String userName) {
		User user = userMapper.getByAccount(userName);
		if(user == null){
			return null;
		}
		return user;
	}
	
	@Override
	public User getByPhone(String userPhone) {
		User user = userMapper.getByPhone(userPhone);
		if(user == null){
			return null;
		}
		return user;
	}
	@Override
	@Transactional
	public ResultBean<String> register(User record) {
		ResultBean<String> result = new ResultBean<String>();
		String salt = RandomUtils.randomAllChar(7);
		String userId = IDUtils.getPostfix(3);
//		String userId = String.valueOf(new SnowFlake().nextId());
		String userToken = IDUtils.genID();
		String pwd = MD5Utils.encode(record.getPassword() + salt);
		record.setPasswordSalt(salt);
		record.setPassword(pwd);
		record.setUserToken(userToken);
		record.setId(userId);
		record.setStatus(0);
		int duplicate = userMapper.userExist(record.getUsername());
		if(duplicate > 0){
			result.setCode(ConstantUtils.USER_EXIST);
			result.setMsg("用户名已被注册");
			return result;
		}
		int user = userMapper.insert(record);
		if (user <= 0) {
			result.setCode(ConstantUtils.RESULT_FAILED);
			result.setMsg("注册失败");
		} else {
			result.setMsg("注册成功");
//				result.setData(aesEncode(user));
		}
		return result;
	}

	@Override
	@Transactional
	public ResultBean<String> updateUserInfo(User record) {
		// TODO Auto-generated method stub
//		System.out.println(String.valueOf(new SnowFlake().nextId()));
		ResultBean<String> result = new ResultBean<String>();
		String userId = UserCache.getUserId();
		String userToken = UserCache.get("userToken");
		int exist = userMapper.userExist(userId);
		if (exist < 1) {
			result.setCode(ConstantUtils.PERMISSION_DENIED);
			result.setMsg("用户还未登录");
		} else {
			record.setId(userId);
			int i = userMapper.updateByPrimaryKeySelective(record);
			if (i > 0) {
				User updated = userMapper.selectByPrimaryKey(userId);
				result.setMsg("修改成功");
				if(redisUtil.hasKey(userToken)){
				  redisUtil.set(userToken,updated.toString());
				}
			} else {
				result.setCode(ConstantUtils.RESULT_FAILED);
				result.setMsg("修改失败");
			}
		}

		return result;
	}

	@Override
	@Transactional
	public ResultBean<String> updatePwd(String newPassWord,String... oldPassWord) {
		ResultBean<String> result = new ResultBean<String>();
		String userName = UserCache.get("userName");
		if(userName == null){
			result.setCode(ConstantUtils.PERMISSION_DENIED);
			result.setMsg("您没有修改权限，请重新登录");
		}else{
			User user = getByAccount(userName);
			if (user == null) {
				result.setCode(ConstantUtils.USER_NOT_EXIST);
				result.setMsg("查无此用户");
			} else {
				if(oldPassWord.length>0) {
					String oldpwd = oldPassWord[0];
					String salt = user.getPasswordSalt();
					oldpwd = MD5Utils.encode(oldpwd+salt);
				
					if (!oldpwd.equals(user.getPassword())) {
						result.setCode(ConstantUtils.INCORRECT_PWD);
						result.setMsg("旧密码输入错误");
						return result;
					}
				}
				
				String newSalt = RandomUtils.randomAllChar(7);
				String pwd = MD5Utils.encode(newPassWord + newSalt);
				user.setPasswordSalt(newSalt);
				user.setPassword(pwd);
				int i = userMapper.updateByPrimaryKeySelective(user);
				if (i > 0) {
					result.setMsg("修改成功");
					if(redisUtil.hasKey(user.getUserToken())){
						redisUtil.del(user.getUserToken());
					}
				} else {
					result.setCode(ConstantUtils.RESULT_FAILED); //数据库更新失败
					result.setMsg("修改失败");
				}
			}
		}
	
		return result;
	}

	@Override
	public ResultBean<JSONObject> uploadAvatar(String file){
		
		ResultBean<JSONObject> result = new ResultBean<JSONObject>();
		if(UserCache.get() == null){
			result.setCode(ConstantUtils.PERMISSION_DENIED);
			result.setMsg("用户还未登录");
			return result;
		}
		
		String prefix = servletContext.getRealPath("/");
		String filePath = "";
		try {
			if (!file.replace(",", "").isEmpty()) {
				filePath = ImgUtil.generateImage(file, prefix);
				FileSystemResource fileSystemResource = new FileSystemResource(filePath);
				result = uploadAvatar(fileSystemResource);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ImgUtil.deleteFile(filePath);
		}
		return result;
	}
	
	@Transactional
	public ResultBean<JSONObject> uploadAvatar(FileSystemResource file) {
		ResultBean<JSONObject> result = new ResultBean<JSONObject>();
//		System.out.println(SFTPUtils.fileUpload(file,base + "userAvatar"));
		
		String userId = UserCache.getUserId();
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			result.setCode(ConstantUtils.PERMISSION_DENIED);
			result.setMsg("用户还未注册");
		} else {
			String path = "userAvatar";
//			String filename = ftp.genFileName(file.getOriginalFilename());
//			Boolean fileName = ftp.fileUpload(path,filename, file);
			List<FileSystemResource> filelist = new ArrayList<FileSystemResource>();
			filelist.add(file);
			String storedFilename = ftp.fileUpload(path, filelist).get(0);
			if (storedFilename.isEmpty()) {
				result.setCode(ConstantUtils.RESULT_FAILED);
				result.setMsg("上传失败");
			} else {
				User tuser = new User();
				tuser.setId(userId);
				tuser.setAvatar(storedFilename);
				int i = userMapper.updateByPrimaryKeySelective(tuser);
				if (i > 0) {
					User updated = userMapper.selectByPrimaryKey(userId);
					String userToken = updated.getUserToken();
					result.setMsg("修改成功");
					if(redisUtil.hasKey(userToken)){
					  redisUtil.set(userToken,updated.toString(),604800);
					}
				} else {
					result.setCode(ConstantUtils.RESULT_FAILED);
					result.setMsg("修改失败");
				}
				userMapper.updateByPrimaryKeySelective(tuser);
				JSONObject json = new JSONObject();
				json.put("avatar", storedFilename);
				result.setMsg("上传成功");
				result.setData(json);
				
			}
		}
		return result;
	}

	@Override
	public JSONObject getCurrentUser(JSONObject user) {
		// TODO Auto-generated method stub
		String userToken = user.getString("userToken");
		String userName = user.getString("userName");
				
	    try{
	    	if(redisUtil.hasKey(userToken)){
	    		String userObj = redisUtil.get(userToken).toString();
				user = JSONObject.parseObject(userObj);
			    redisUtil.expire(userToken, 604800);
			}else{
				User userfromdb = getByAccount(userName);
				user = JSONObject.parseObject(userfromdb.toString());
				redisUtil.set(userToken,userfromdb.toString(),604800);
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
		return user;
	}

	@Override
	public Integer logout() {
		String userToken = UserCache.get("userToken");
		if(redisUtil.hasKey(userToken)){
			redisUtil.del(userToken);
		}
		UserCache.remove();
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@Transactional
	public ResultBean<String> updateUserPhone(String userPhone, String oldPhone, String captcha,HttpSession session) {
		ResultBean<String> result = new ResultBean<String>();
		String code = StringUtils.EMPTY;
		User newPhone = new User();
		
		code = (String) redisUtil.get(userPhone);
		if(code != null && code.equals(captcha)) { //如果验证码通过
			User user = getByPhone(oldPhone); //根据旧手机号码查询用户
			if (user != null) {//如果用户存在，更新手机号码
				String userToken = user.getUserToken();
				user.setPhone(userPhone);
				newPhone.setId(user.getId());
				newPhone.setPhone(userPhone); //新建一个用户对象，只更新手机号码
				int i = userMapper.updateByPrimaryKeySelective(newPhone);
				if (i > 0) {
					result.setMsg("修改成功");
					//更新缓存的用户信息
					if(redisUtil.hasKey(userToken)){
					  redisUtil.set(userToken,user.toString());
					}
					session.setAttribute("UserInfo", JSONObject.parseObject(user.toString()));
				} else {
					result.setCode(ConstantUtils.RESULT_FAILED);
					result.setMsg("修改失败");
				}
			}else {
				result.setCode(ConstantUtils.USER_NOT_EXIST);
				result.setMsg("此用户不存在");
			}
		
		}else {
			if(code == null) {
				result.setCode(ConstantUtils.INCORRECT_SMS_CODE);
				result.setMsg("验证码已失效");
			}else {
				result.setCode(ConstantUtils.INCORRECT_SMS_CODE);
				result.setMsg("验证码错误");
			}
		}

		return result;
	}
}
