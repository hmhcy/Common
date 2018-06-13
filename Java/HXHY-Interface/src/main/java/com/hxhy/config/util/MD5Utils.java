package com.hxhy.config.util;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Title: MD5加密工具类<br/>
 * 
 * @version V1.0.0
 */
public class MD5Utils {
	private static final Random RANDOM = new SecureRandom();
	
	/*
	 * 生成随机加密salt
	 */
	public static String saltGenerator() throws UnsupportedEncodingException{
		String result = new String();
		byte[] salt = new byte[7];
		RANDOM.nextBytes(salt);
		 int i = 0;
	     for(; i < salt.length; i++)     // do not write i=0 in for loop as i is used in next for loop also
	     {
	       result += salt[i];
	     }
		return result;
	}
    /**
     * 默认使用 UTF-8 签名字符串
     * 
     * @param text
     * @return
     */
    public static String encode(String text) {
        return encode(text, "UTF-8");
    }

    /**
     * 签名字符串
     * 
     * @param text
     *            需要签名的字符串
     * @param input_charset
     *            编码格式
     * @return 签名结果
     */
    public static String encode(String text, String input_charset) {
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     * 
     * @param text
     *            需要签名的字符串
     * @param key
     *            密钥
     * @param input_charset
     *            编码格式
     * @return 签名结果
     */
    public static String encode(String text, String key, String input_charset) {
        text = text + key;
        System.out.println(text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }

    /**
     * 签名字符串
     * 
     * @param text
     *            需要签名的字符串
     * @param sign
     *            签名结果
     * @param key
     *            密钥
     * @param input_charset
     *            编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
