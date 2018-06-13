package com.hxhy.config.util;

/**
 * 常量类
 * 
 * @author Abner
 */
public class ConstantUtils {

    /**
     * 数字枚举
     * 
     * @author Abner
     */
    public static class NUMCNST {
        // int
        public final static Integer INT_ZERO = 0;
        public final static Integer INT_ONE = 1;
        public final static Integer INT_TWO = 2;
        public final static Integer INT_THREE = 3;
        public final static Integer INT_FOUR = 4;
        public final static Integer INT_FIVE = 5;
        public final static Integer INT_SIX = 6;
        public final static Integer INT_SERVEN = 7;
        public final static Integer INT_EIGNT = 8;
        public final static Integer INT_NINE = 9;
        public final static Integer INT_TEN = 10;

        // String
        public final static String STR_ZERO = "0";
        public final static String STR_ONE = "1";
        public final static String STR_TWO = "2";
        public final static String STR_THREE = "3";
        public final static String STR_FOUR = "4";
        public final static String STR_FIVE = "5";
        public final static String STR_SIX = "6";
        public final static String STR_SEVEN = "7";
        public final static String STR_EIGHT = "8";
        public final static String STR_NINE = "9";
        public final static String STR_TEN = "10";
         
    }

    // result code
    /**
     * general code
     */
    public static final int RESULT_SUCCESS = 1;

    public static final int RESULT_FAILED = -1;
    
    /**
     * 用户相关，-1000往后
     */
    public static final int PERMISSION_DENIED = -1000;
    public static final int USER_NOT_EXIST = -1001;
    public static final int USER_EXIST = -1002;
    /**
     * 普通错误类
     */
    public static final int FIELD_INVALID = -2;
    public static final int DATA_INVALID = -3;
    
    public static final int INCORRECT_PWD = -4;
    
    public static final int INCORRECT_SMS_CODE = -5;
    
    public static final int KAPTCHA_INCORRECT = -10;
    public static final int KAPTCHA_TIME_OUT = -11;
    public static final int KAPTCHA_REFRESH_NEED = -12;

    // charset
    /**
     * charset utf8
     */
    public final static String CHARSET_CNST_UTF8 = "UTF-8";

    
    /**
     * 身份验证验证码:SMS_134125254   登录确认验证码:SMS_134125253  登录异常验证码  SMS_134125252
 *                      用户注册验证码:SMS_134125251   修改密码验证码:SMS_134125250  信息变更验证码  SMS_134125249
     */
    public final static String SMS_AUTH = "SMS_134125254";
    public final static String SMS_LOGIN_COMFIRM = "SMS_134125253";
    public final static String SMS_LOGIN_EXCEPT = "SMS_134125252";
    public final static String SMS_REGISTER_CODE = "SMS_134125251";
    public final static String SMS_PWD_CHANGE = "SMS_134125250";
    public final static String SMS_INFO_UPDATE = "SMS_134125249";
    
    /**
     * 最大登录尝试次数
     */
    
    public final static int MAX_ATTEMPT = 4;
}
