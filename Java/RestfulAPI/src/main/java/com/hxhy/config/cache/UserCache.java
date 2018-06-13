package com.hxhy.config.cache;

import com.alibaba.fastjson.JSONObject;

/**
 * @author insertor
 * @since 2018/01/07 15:32
 */
public class UserCache {
	private static ThreadLocal<JSONObject> TUser_CACHE = new ThreadLocal<JSONObject>();

	/**
	 * set TUser cache
	 * 
	 * @param user
	 */
	public static void set(JSONObject user) {
		TUser_CACHE.set(user);
	}

	/**
	 * get TUser from cache
	 * 
	 * @return
	 */
	public static JSONObject get() {
		return TUser_CACHE.get();
	}

	/**
	 * get TUser id from cache
	 * 
	 * @return
	 */
	public static String getUserId() {
		JSONObject user = TUser_CACHE.get();

		if (user != null) {
			return user.getString("userId");
		}

		return null;
	}

	/**
	 * get volumn id from cache
	 * 
	 * @return
	 */
	public static String get(String name) {
		JSONObject user = TUser_CACHE.get();

		if (user != null) {
			return user.getString(name);
		}

		return null;
	}

	/**
	 * remove TUser from cache
	 */
	public static void remove() {
		TUser_CACHE.remove();
	}
}
