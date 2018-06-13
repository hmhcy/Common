package com.hxhy.api.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.model.User;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.api.service.LoginAttemptService;
import com.hxhy.api.service.UserService;
import com.hxhy.api.service.VerifyService;
import com.hxhy.config.cache.UserCache;
import com.hxhy.config.exception.LoginAttempFailureException;
import com.hxhy.config.util.ConstantUtils;
import com.hxhy.config.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "user")
@Api(value = "用户资源", tags = { "用户资源" })
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private VerifyService verifyService;
	
	@Autowired
	private LoginAttemptService loginAttempt;

	
	@ApiOperation(value = "用户名密码注册")
	@RequestMapping(value = "/register", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResultBean<String> register(@ApiParam(name="用户",value="用户") User user) {
		ResultBean<String> result = new ResultBean<String>();
		if (StringUtils.isEmpty(user.getUsername())) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("用户名不能为空");
		} else if (StringUtils.isEmpty(user.getPassword())) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("密码不能为空");
		} else {
			result = userService.register(user);
		}
		return result;
	}

	@ApiOperation(value = "用户名密码登录")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	@ApiImplicitParam(name = "rememberMe", value = "", dataType = "Integer",paramType = "query")
	public ResultBean<String> login(@RequestParam String userName, @RequestParam String pwd, 
			 @RequestParam Integer rememberMe,@ApiIgnore HttpServletRequest request, 
			@ApiIgnore  HttpServletResponse response) {
		ResultBean<String> result = new ResultBean<String>();
		
		if (StringUtils.isEmpty(userName)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("用户名不能为空");
//			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		} else if (StringUtils.isEmpty(pwd)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("密码不能为空");
		} else {
			try {
				loginAttempt.loginAttempt(getClientIP(request));
			} catch (LoginAttempFailureException e) {
				result.setCode(ConstantUtils.RESULT_FAILED);
				result.setMsg(e.getMessage());
				return result;
			}
			result = userService.login(userName, pwd, rememberMe,request.getSession());
			if(result.getCode() == ConstantUtils.RESULT_SUCCESS) {
				loginAttempt.loginSucceeded(getClientIP(request));
				Cookie cookie = new Cookie("auth", result.getData()); // Not necessary, but saves bandwidth.
				cookie.setPath("/hxhyapi");
				System.out.println(getClientIP(request));
				cookie.setHttpOnly(true);
				cookie.setMaxAge(60 * 60 * 24 * 365);
				response.addCookie(cookie);
			}else if(result.getCode() == ConstantUtils.INCORRECT_PWD) {
				loginAttempt.loginFailed(getClientIP(request));
			}
		}
		
		return result;
	}
	

	@ApiOperation(value = "手机短信验证码登录")
	@RequestMapping(value = "/login/sms", method = RequestMethod.POST)
	public ResultBean<String> smsLogin(@RequestParam String userPhone, @RequestParam String captcha, @ApiIgnore HttpServletRequest request, 
			@ApiIgnore  HttpServletResponse response) {
		ResultBean<String> result = new ResultBean<String>();
		if (StringUtils.isEmpty(userPhone)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("手机号不能为空");
//			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		} else if (StringUtils.isEmpty(captcha)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("验证码不能为空");
		} else {
			result = userService.smsLogin(userPhone, captcha,request.getSession());
			if(result.getCode() == ConstantUtils.RESULT_SUCCESS) {
				Cookie cookie = new Cookie("auth", result.getData()); // Not necessary, but saves bandwidth.
				cookie.setPath("/hxhyapi");
				System.out.println(request.getServerName());
//				cookie.setDomain(request.getServerName());
				cookie.setHttpOnly(true);
				cookie.setMaxAge(24* 60 * 60 * 365);
				response.addCookie(cookie);
			}
		}
		
		return result;
//		return ResponseEntity.ok(result);
	}
	
	@ApiOperation(value = "修改密码")
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
//	@ApiImplicitParam(name = "AuthKey", value = "凭证", paramType = "header",  dataType = "String")
	public ResultBean<String> updatePwd(@RequestParam String oldPassWord, @RequestParam String newPassWord) {
		ResultBean<String> result = new ResultBean<String>();
		if (StringUtils.isEmpty(oldPassWord)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("旧密码不能为空");
		} else if (StringUtils.isEmpty(newPassWord)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("新密码不能为空");
		} else {
			result = userService.updatePwd(newPassWord,oldPassWord);
		}
		return result;
	}
	
	@ApiOperation(value = "设置密码")
	@RequestMapping(value = "/setPwd", method = RequestMethod.POST)
//	@ApiImplicitParam(name = "AuthKey", value = "凭证", paramType = "header",  dataType = "String")
	public ResultBean<String> setPwd(@RequestParam String passWord) {
		ResultBean<String> result = new ResultBean<String>();
		 if (StringUtils.isEmpty(passWord)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("密码不能为空");
		} else {
			result = userService.updatePwd(passWord);
		}
		return result;
	}
	
	@ApiOperation(value = "修改用户信息")
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//	@ApiImplicitParam(name = "AuthKey", value = "凭证", paramType = "header", dataType = "String")
	public ResultBean<String> updateUserInfo(@ApiParam(value="用户") User user) {
		ResultBean<String> result = new ResultBean<String>();
		result = userService.updateUserInfo(user);
		return result;
	}
	
	@ApiOperation(value = "修改用户手机号")
	@RequestMapping(value = "/updateUserPhone", method = RequestMethod.POST)
//	@ApiImplicitParam(name = "AuthKey", value = "凭证", paramType = "header", dataType = "String")
	public ResultBean<String> updateUserPhone(@RequestParam(value="新手机号") String userPhone, @RequestParam(value="旧手机号") String oldPhone, 
			@RequestParam(value="验证码") String captcha,@ApiIgnore HttpServletRequest request) {
		ResultBean<String> result = new ResultBean<String>();
		result = userService.updateUserPhone(userPhone,oldPhone,captcha, request.getSession());
		return result;
	}
	
	@ApiOperation(value = "实名认证")
	@RequestMapping(value = "/verifyIdentity", method = RequestMethod.POST)
	public ResultBean<String> verifyIdentity(@RequestParam String userName,@RequestParam String userIdentity) {
		ResultBean<String> result = new ResultBean<String>();
		if (StringUtils.isEmpty(userName)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("姓名不能为空");
		} else if (StringUtils.isEmpty(userIdentity)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("身份证不能为空");
		} else {
			result = verifyService.verifyIdentity(userName, userIdentity);
		}
		return result;
	}
	
	@ApiOperation(value = "三网手机号实名认证")
	@RequestMapping(value = "/verifyPhone", method = RequestMethod.POST)
	public ResultBean<String> verifyPhone(@RequestParam String userName,
			@RequestParam String userIdentity,
			@RequestParam String userPhone) {
		ResultBean<String> result = new ResultBean<String>();
		if (StringUtils.isEmpty(userName)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("姓名不能为空");
		} else if (StringUtils.isEmpty(userIdentity)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("身份证不能为空");
		} else if (StringUtils.isEmpty(userPhone)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("手机号不能为空");
		} else {
			result = verifyService.verifyPhone(userName, userIdentity, userPhone);
		}
		return result;
	}
	
	@ApiOperation(value = "失信人黑名单查询")
	@RequestMapping(value = "/selectCredit", method = RequestMethod.POST)
	public ResultBean<JSONObject> selectCredit(@RequestParam String userCardno,
			@RequestParam String userName,
			@RequestParam(required=false) String userPhone,
			@RequestParam String userType) {
		ResultBean<JSONObject> result = new ResultBean<JSONObject>();
		if (StringUtils.isEmpty(userCardno)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("个人公民身份号码或机构营业执照编号不能为空");
		} else if (StringUtils.isEmpty(userName)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("个人或企业名称不能为空");
		} else if (StringUtils.isEmpty(userType)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("查询主体不能为空");
		} else {
			result = verifyService.selectCredit(userCardno, userName, userPhone, userType);
		}
		return result;
	}
	
	@ApiOperation(value = "银行卡实名认证")
	@RequestMapping(value = "/selectBankCard", method = RequestMethod.POST)
	public ResultBean<String> selectBankCard(@RequestParam(required=false) String userName,
		@RequestParam String userCard, @RequestParam(required=false) String userIdentity, @RequestParam(required=false) String userPhone) {
		ResultBean<String> result = new ResultBean<String>();
		if (StringUtils.isEmpty(userCard)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("银行卡号不能为空");
		} else {
			result = verifyService.selectBankCard(userName, userCard, userIdentity, userPhone);
		}
		return result;
	}
	 /**
     * 上传图片
     */
//	@ApiOperation(value = "用户头像上传")
//    @RequestMapping(method = RequestMethod.POST, value = "/uploadAvatar",consumes = {"multipart/form-data"})
//    public ResultBean<JSONObject> uploadAvatarFile(@RequestPart(value = "user.avatar") MultipartFile picture,@ApiIgnore HttpSession session) {
//        ResultBean<JSONObject> result = new ResultBean<JSONObject>();
///*		if (UserCache.get() == null) {
//			result.setCode(-1000);
//			result.setMsg("用户不能为空");
//		} else {
//			String prefix = session.getServletContext().getRealPath("/");
//			result = userService.uploadAvatar(picture,prefix);
//		}*/
////		result = userService.uploadAvatar(picture);
//		return result;
//    }
	
	/**
	 * 上传图片
     */
	@ApiOperation(value = "用户头像上传")
    @RequestMapping(method = RequestMethod.POST, value = "/uploadAvatar")
    public ResultBean<JSONObject> uploadAvatar(@RequestBody String base64File) {
        ResultBean<JSONObject> result = new ResultBean<JSONObject>();
        if (StringUtils.isEmpty(base64File)) {
			result.setCode(ConstantUtils.FIELD_INVALID);
			result.setMsg("请上传您的头像");
		}else {
			 result = userService.uploadAvatar(base64File);
		}
     	
		return result;
    }
	
	/**
	 * 上传图片
     */
	@ApiOperation(value = "获取用户信息")
    @RequestMapping(method = RequestMethod.GET, value = "/getUser")
    public ResultBean<JSONObject> getUser() {
        ResultBean<JSONObject> result = new ResultBean<JSONObject>();
        if(UserCache.get() == null){
			result.setCode(ConstantUtils.PERMISSION_DENIED);
			result.setMsg("用户还未登录");
			return result;
		}else {
			result.setMsg("获取成功");
			result.setData(UserCache.get());
		}
     	
		return result;
    }
	
	@ApiOperation(value = "登出")
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResultBean<String> logout(@ApiIgnore HttpServletRequest request,
			@ApiIgnore HttpServletResponse response) {
		ResultBean<String> result = new ResultBean<String>();
		Cookie[] cookies = request.getCookies();
		  if(cookies != null) {
		      for (int i = 0; i < cookies.length; i++) {
		          String cookieName = cookies[i].getName();
		          if(cookieName == "auth")
		          {
		        	  cookies[i].setValue("");
		        	  cookies[i].setPath("/");
		        	  cookies[i].setMaxAge(-1);
		        	  response.addCookie( cookies[i]);
		        	  break;
		          }
		       }
		   }
		Integer resultCode = userService.logout();
		if(resultCode == 1){
			request.getSession().removeAttribute("UserInfo");
			result.setCode(ConstantUtils.RESULT_SUCCESS);
			result.setMsg("登出成功");
		}
		return result;
	}
	
	@ApiIgnore
	private String getClientIP(HttpServletRequest request) {
	    String xfHeader = request.getHeader("X-Forwarded-For");
	    if (xfHeader == null){
	        return request.getRemoteAddr();
	    }
	    return xfHeader.split(",")[0];
	}
}
