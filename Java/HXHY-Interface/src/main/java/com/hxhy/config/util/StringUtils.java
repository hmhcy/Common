package com.hxhy.config.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * 字符串工具类
 * 
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {


	// Constant
	/**
	 * 破折号
	 */
	public static final String DASH = "-";
	public static final String EMPTY = null;

	/**
	 * 将有规律格式的字符串根据分隔符转化成数组
	 * 
	 * @param str
	 * @param split
	 * @return
	 */
	public static String[] strToArray(String str, String split) {
		List<String> resultList = new ArrayList<String>();

		int index = 0;
		String actionTarget = str;
		while (index <= str.length()) {
			int nextIndex = actionTarget.indexOf(split);
			resultList.add(str.substring(index, nextIndex));
			actionTarget = str.substring(nextIndex);
			index = nextIndex;
		}

		return (String[]) resultList.toArray();
	}

	/**
	 * 生成主键id
	 * 
	 * @return
	 */
	public static String createId() {
		String id;
		java.util.Random r = new java.util.Random();
		while (true) {
			int i = r.nextInt(99999999);
			if (i < 0)
				i = -i;
			id = String.valueOf(i);
			if (id.length() < 8) {
				continue;
			}
			if (id.length() >= 8) {
				id = id.substring(0, 8);
				break;
			}
		}

		id = new Date().getTime() + id;
		return id;
	}
	
	/**
	 * 产生序列号
	 * 
	 * @return
	 */
	public synchronized static String getSeqString() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		String seq = DateUtils.dateToString(new Date(), "yyMMddHHmmssSSS");
		String idStr = seq.toString();
		return idStr;
	}
	
	/**
	 * 获取字符串编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}
	
	public static class Reg {

		/**
		 * 手机号码正则
		 */
		public final static String REG_TEL = "1[3|4|5|6|7|8|9]\\d{9}";

		/**
		 * e-mail 正则
		 */
		public final static String REG_EMAIL = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";
		
		public final static String REG_CARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";

		/**
		 * 手机号码格式验证
		 * 
		 * @param str
		 * @return
		 */
		public static boolean tel(String str) {
			Pattern pattern = Pattern.compile(REG_TEL);
			return pattern.matcher(str).matches();
		}

		/**
		 * Email 格式验证
		 * 
		 * @param str
		 * @return
		 */
		public static boolean email(String str) {
			return Pattern.compile(REG_EMAIL).matcher(str).matches();
		}
		
		/**
		 * 身份证 格式验证
		 * 
		 * @param str
		 * @return
		 */
		public static boolean idCard(String str) {
			return Pattern.compile(REG_CARD).matcher(str).matches();
		}
	}
}
