package com.hxhy.api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import com.hxhy.config.exception.LoginAttempFailureException;
import com.hxhy.config.util.ConstantUtils;
import com.hxhy.config.util.DateUtils;
import com.hxhy.redis.RedisUtil;

import redis.clients.jedis.exceptions.JedisException;

@Service
public class LoginAttemptService {
     
    @Autowired
	private RedisUtil redisUtil;
 
    public LoginAttemptService() {
        super();
    }
 
    public void loginAttempt(String key) throws LoginAttempFailureException {
    	if(isBlocked(key)) throw new LoginAttempFailureException("尝试登陆次数超过"+ ConstantUtils.MAX_ATTEMPT +"次,账号已被锁定");
    	Long expires = DateUtils.getTimeToNextMidNight(new Date(), "s");
    	if(!redisUtil.hasKey(key)) redisUtil.set(key,0,expires);
    }
    
    public void loginSucceeded(String key) {
    	redisUtil.del(key);
    }
 
    public void loginFailed(String key) {
        int attempts = 0;
        try {
        	if(redisUtil.hasKey(key)) {
        		attempts = (int) redisUtil.get(key);
        	}
            attempts++;
            redisUtil.set(key,attempts);
        }catch (JedisException  e) {
           e.printStackTrace();
        }

    }
 
    public boolean isBlocked(String key) {
        try {
            return redisUtil.hasKey(key) && (int) redisUtil.get(key) >= ConstantUtils.MAX_ATTEMPT;
        } catch (RedisConnectionFailureException  e) {
            return false;
        }
    }
}