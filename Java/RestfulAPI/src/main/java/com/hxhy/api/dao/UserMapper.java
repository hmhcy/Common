package com.hxhy.api.dao;



import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hxhy.api.model.User;

/**
 * 
 * UserMapper数据库操作接口类
 * 
 **/
@Repository
public interface UserMapper{


	/**
	 * 
	 * 查询（根据主键ID查询）
	 * 
	 **/
	User  selectByPrimaryKey ( @Param("id") String id );

	/**
	 * 
	 * 删除（根据主键ID删除）
	 * 
	 **/
	int deleteByPrimaryKey ( @Param("id") String id );

	/**
	 * 
	 * 添加
	 * 
	 **/
	int insert( User record );

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective( User record );

	/**
	 * 
	 * 修改 （匹配有值的字段）
	 * 
	 **/
	int updateByPrimaryKeySelective( User record );

	/**
	 * 
	 * 修改（根据主键ID修改）
	 * 
	 **/
	int updateByPrimaryKey ( User record );

	/**
	 * 
	 * 查询（根据用户名查重)
	 */
	int userExist (String param);
	
	/**
	 * 
	 * 查询（根据userId查询用户是否存在)
	 */
//	int userIdExist (@Param("id") String id );
	
	  /**
     * 通过账号查询用户
     */
	User getByAccount(String username);
	
	  /**
     * 通过手机号码查询用户
     */
	User getByPhone(String phone);
}