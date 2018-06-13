package com.hxhy.api.service;

import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.responseEntity.ResultBean;

public interface VerifyService {

    /**
     * 身份证实名认证
     * @param userName      （STRING	必选   姓名)
     * @param userIdentity （STRING	必选   身份证)
     * @return      （0正常、5名字和身份证不匹配、14身份证错误、96请求接口异常）
     */
    ResultBean<String> verifyIdentity(String userName, String userIdentity);

    /**
     * 三网手机号实名认证
     * @param userName      （STRING	必选   姓名)
     * @param userIdentity （STRING	必选   身份证)
     * @param userPhone    （STRING	必选   电话)
     * @return     （0正常、1名字、身份证和手机不匹配、2阿里接口未能找到数据、10手机或身份错误、96请求接口异常）
     */
    ResultBean<String> verifyPhone(String userName, String userIdentity, String userPhone);

    /**
     * 全球物流快递查询（单号识别）
     * @param logisticsNum (STRING	必选   订单号)
     * @return ResultBean<JSONObject>
     */
    ResultBean<JSONObject> selectLogistics(String logisticsNum);

    /**
     * 失信人黑名单查询
     * @param userCardno (STRING	必选	个人公民身份号码或机构营业执照编号)
     * @param userName   (STRING	必选	个人或企业名称)
     * @param userPhone  (STRING	可选	手机号或电话号码)
     * @param userType   (STRING	必选	选择查询个人还是企业，person表示个人，company表示公司)
     * @return ResultBean<JSONObject>
     */
    ResultBean<JSONObject> selectCredit(String userCardno,String userName,String userPhone,String userType);

    /**
     * 银行卡实名认证
     * @param userName      (STRING	可选	姓名)
     * @param userCard      (STRING	必选	银行卡号)
     * @param userIdentity (STRING	可选	身份证号码)
     * @param userPhone     (STRING	可选	预留手机号)
     * @return  ResultBean<String>
     */
    ResultBean<String> selectBankCard(String userName,String userCard, String userIdentity, String userPhone);
}
