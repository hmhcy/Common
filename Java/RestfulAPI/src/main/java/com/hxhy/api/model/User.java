package com.hxhy.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * 
 **/
@ApiModel
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private String id;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "用户密码")
	private String password;

	@ApiModelProperty(value = "密码盐")
	private String passwordSalt;
	
	@ApiModelProperty(value = "真实姓名")
	private String realName;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "注册时间")
	private Date createtime;
	
	@ApiModelProperty(value = "用户头像")
	private String avatar;
	
	@ApiModelProperty(value = "用户口令")
	private String userToken;
	
	@ApiModelProperty(value = "用户状态")
	private Integer status;
	
	@ApiModelProperty(value = "用户昵称")
	private String nickname;


	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return this.id;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return this.username;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return this.password;
	}

	public void setPasswordSalt(String passwordSalt){
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordSalt(){
		return this.passwordSalt;
	}

	public void setRealName(String realName){
		this.realName = realName;
	}

	public String getRealName(){
		return this.realName;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return this.phone;
	}

	public void setCreatetime(Date createtime){
		this.createtime = createtime;
	}

	public Date getCreatetime(){
		return this.createtime;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return this.avatar;
	}

	public void setUserToken(String userToken){
		this.userToken = userToken;
	}

	public String getUserToken(){
		return this.userToken;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Override
	public String toString() {
		return "{" +
			"userId: '" + id + 
			(userToken != null? "' , userToken: '" + userToken :"") + 
			(avatar != null? "' , avatar: '" + avatar :"") +
			(username != null? "' , userName: '" + username :"") + 
			(realName != null? "' , realName: '" + realName:"") + 
			(phone != null? "' , phone: '" + phone:"") + 
			(createtime != null? "' , createTime: '" + createtime:"") + 
			(status != null? "' , status: '" + status:"") + 
			(nickname != null? "' , nickname: '" + nickname:"") + 
			"' }";
	}
}
