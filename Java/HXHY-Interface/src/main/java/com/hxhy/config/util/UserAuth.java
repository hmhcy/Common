package com.hxhy.config.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.fastjson.JSONObject;

public class UserAuth {	
	private static Logger logger = LoggerFactory.getLogger(UserAuth.class);
	
    private static ConfigureProperties config = SpringUtil.getBean(ConfigureProperties.class);
	
	public static SecretKey generalKey(){
        byte[] encodedKey = config.getAesKey().getBytes();
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

	
	public static String generateAuth(JSONObject user){
		String userName = user.getString("userName");
		String userToken = user.getString("userToken");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String token = userToken + ":" + timestamp.getTime() + ":" + userName; 

		String hash = MD5Utils.encode(token);
		SecretKey key = generalKey();
		String auth = Jwts.builder()
				  .setSubject( userToken + ":" + timestamp.getTime() + ":" + userName + ":" + hash)
				  .signWith(SignatureAlgorithm.HS256, key)
				  .compact();
		return auth;
	}
	
	public static JSONObject verifyUser(String auth){
		SecretKey key = generalKey();
		//assert Jwts.parser().setSigningKey(key).parseClaimsJws(auth).getBody().getSubject().equals("Joe");
		try {
		    Jws<Claims> verify = Jwts.parser().setSigningKey(key).parseClaimsJws(auth);
		    String token = verify.getBody().getSubject();
		    List<String> detailList = Arrays.asList(token.split(":"));
		    String userToken = detailList.get(0);
			String timestamp = detailList.get(1);
			String userName = detailList.get(2);
			String hash = detailList.get(3);
			
			String decode = userToken + ":" + timestamp + ":" + userName;
			String decodehash = MD5Utils.encode(decode);
			if(hash.equals(decodehash)){
				JSONObject user = new JSONObject();
				user.put("userName", userName);
				user.put("userToken", userToken);
				user.put("timestamp", timestamp);
				return user;
			}
		} catch (SignatureException e) {
			logger.error(DateUtils.getSdfAllDayTime() + "verification fails:enemy attacks!");
		}
		return null;
	}
	
}
