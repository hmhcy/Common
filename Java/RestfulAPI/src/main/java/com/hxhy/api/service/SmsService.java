package com.hxhy.api.service;

import com.aliyuncs.exceptions.ClientException;

public interface SmsService {

    /**
     *
     * @param userPhone (STRING	可选	电话)
     * @param userCode  (STRING	可选	验证码)
     * @param smsCode   (STRING	可选	验证类型
     *                      身份验证验证码:SMS_134125254   登录确认验证码:SMS_134125253  登录异常验证码  SMS_134125252
     *                      用户注册验证码:SMS_134125251   修改密码验证码:SMS_134125250  信息变更验证码  SMS_134125249
     * @return boolean
     * @throws ClientException
     */
    public boolean sendSms(String userPhone,String userCode,String smsCode) throws ClientException ;
}
