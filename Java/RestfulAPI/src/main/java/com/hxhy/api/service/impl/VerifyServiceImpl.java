package com.hxhy.api.service.impl;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.api.service.VerifyService;
import com.hxhy.config.util.HttpUtils;



@Service
@Transactional
public class VerifyServiceImpl implements VerifyService{

    private String appcode = "2c0e06833e0648cdad0e6a0b6fca6996";
    private String method = "GET";

    /**
     * 身份证实名认证
     * @param userName      （STRING	必选   姓名)
     * @param userIdentity （STRING	必选   身份证)
     * @return      （0正常、5名字和身份证不匹配、14身份证错误、96请求接口异常）
     */
    public ResultBean<String> verifyIdentity(String userName, String userIdentity){
        ResultBean<String> result = new ResultBean<String>();
        String content = "";
        String host = "http://idcard.market.alicloudapi.com";
        String path = "/lianzhuo/idcard";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("cardno", userIdentity);
        querys.put("name", userName);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for(String s = reader.readLine(); s != null; s = reader.readLine()){
                content += s;
            }

            JSONObject Object1 =  JSON.parseObject(content);

            //System.out.println("Object1 === " + Object1.get("resp"));
            JSONObject Object2 =  JSON.parseObject(Object1.get("resp").toString());
            //System.out.println("Object2 === " + Object2.get("code"));
            if (Object2.get("code").toString().equals("0")){
                result.setCode(0);
            }else{
                result.setCode(1);
                if(Object2.get("code").toString().equals("5")){
                    result.setMsg("输入的名字和身份证不匹配，请您核对信息！");
                }else if(Object2.get("code").toString().equals("14")){
                    result.setMsg("身份证输入有误，请您核对信息！");
                }else{
                    result.setMsg("抱歉！平台繁忙，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 三网手机号实名认证
     * @param userName      （STRING	必选   姓名)
     * @param userIdentity （STRING	必选   身份证)
     * @param userPhone    （STRING	必选   电话)
     * @return     （0正常、1名字、身份证和手机不匹配、2阿里接口未能找到数据、10手机或身份错误、96请求接口异常）
     */
    public ResultBean<String> verifyPhone(String userName, String userIdentity, String userPhone){
        ResultBean<String> result = new ResultBean<String>();
        String content = "";
        String host = "http://telvertify.market.alicloudapi.com";
        String path = "/lianzhuo/telvertify";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("id", userIdentity);
        querys.put("name", userName);
        querys.put("telnumber", userPhone);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for(String s = reader.readLine(); s != null; s = reader.readLine()){
                content += s;
            }
            //System.out.println("====" + content);
            JSONObject Object1 =  JSON.parseObject(content);

            //System.out.println("Object1 === " + Object1.get("resp"));
            JSONObject Object2 =  JSON.parseObject(Object1.get("resp").toString());
            //System.out.println("Object2 === " + Object2.get("code"));
            if (Object2.get("code").toString().equals("0")){
                result.setCode(0);
            }else{
                result.setCode(1);
                if(Object2.get("code").toString().equals("1")){
                    result.setMsg("输入的名字、身份证和手机不匹配，请您核对信息！");
                }else if(Object2.get("code").toString().equals("10")){
                    result.setMsg("手机或身份证输入有误，请您核对信息！");
                }else if(Object2.get("code").toString().equals("2")){
                    result.setMsg("平台未能查询与您相关的信息，请联系平台客服！");
                }else{
                    result.setMsg("抱歉！平台繁忙，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 全球物流快递查询（单号识别）
     * @param logisticsNum (STRING	必选   订单号)
     * @return ResultBean<JSONObject>
     */
    public ResultBean<JSONObject> selectLogistics(String logisticsNum){
        ResultBean<JSONObject> result = new ResultBean<JSONObject>();
        String content = "";
        String host = "https://goexpress.market.alicloudapi.com";
        String path = "/goexpress";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("no", logisticsNum);
        querys.put("type", "zto");
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for(String s = reader.readLine(); s != null; s = reader.readLine()){
                content += s;
            }
            //System.out.println("====" + content);
            JSONObject Object1 =  JSON.parseObject(content);
            result.setData(Object1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 失信人黑名单查询
     * @param userCardno (STRING	必选	个人公民身份号码或机构营业执照编号)
     * @param userName   (STRING	必选	个人或企业名称)
     * @param userPhone  (STRING	可选	手机号或电话号码)
     * @param userType   (STRING	必选	选择查询个人还是企业，person表示个人，company表示公司)
     * @return ResultBean<JSONObject>
     */
    public ResultBean<JSONObject> selectCredit(String userCardno,String userName,String userPhone,String userType){
        ResultBean<JSONObject> result = new ResultBean<JSONObject>();
        String content = "";
        String host = "https://blacklist.market.alicloudapi.com";
        String path = "/apixcredit/blacklist/dishonest";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("cardno", userCardno);
        querys.put("name", userName);
        querys.put("phoneNo", userPhone);
        querys.put("type",userType);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for(String s = reader.readLine(); s != null; s = reader.readLine()){
                content += s;
            }
//            System.out.println("====" + content);
            JSONObject Object1 =  JSON.parseObject(content);
            result.setData(Object1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 银行卡实名认证
     * @param userName      (STRING	可选	姓名)
     * @param userCard      (STRING	必选	银行卡号)
     * @param userIdentity (STRING	可选	身份证号码)
     * @param userPhone     (STRING	可选	预留手机号)
     * @return  ResultBean<String>
     */
    public ResultBean<String> selectBankCard(String userName,String userCard, String userIdentity, String userPhone){
        ResultBean<String> result = new ResultBean<String>();
        String content = "";
        String host = "http://lundroid.market.alicloudapi.com";
        String path = "/lianzhuo/verifi";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("acct_name", userName);
        querys.put("acct_pan", userCard);
        querys.put("cert_id", userIdentity);
        querys.put("phone_num", userPhone);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for(String s = reader.readLine(); s != null; s = reader.readLine()){
                content += s;
            }
            //System.out.println("====" + content);
            JSONObject Object1 =  JSON.parseObject(content);

            //System.out.println("Object1 === " + Object1.get("resp"));
            JSONObject Object2 =  JSON.parseObject(Object1.get("resp").toString());
            //System.out.println("Object2 === " + Object2.get("code"));
            if (Object2.get("code").toString().equals("0")){
                result.setCode(0);
            }else{
                result.setCode(1);
                if(Object2.get("code").toString().equals("4")){
                    result.setMsg("此卡被没收，请于发卡方联系！");
                }else if(Object2.get("code").toString().equals("5")){
                    result.setMsg("持卡人认证失败，信息不一致！");
                }else if(Object2.get("code").toString().equals("14")){
                    result.setMsg("无效卡号！");
                }else if(Object2.get("code").toString().equals("15")){
                    result.setMsg("此卡无对应发卡方！");
                }else if(Object2.get("code").toString().equals("21")){
                    result.setMsg("该卡未初始化或睡眠卡！");
                }else if(Object2.get("code").toString().equals("34")){
                    result.setMsg("作弊卡，吞卡！");
                }else if(Object2.get("code").toString().equals("40")){
                    result.setMsg("发卡方不支持的交易！");
                }else if(Object2.get("code").toString().equals("41")){
                    result.setMsg("此卡已经挂失！");
                }else if(Object2.get("code").toString().equals("43")){
                    result.setMsg("此卡被没收！");
                }else if(Object2.get("code").toString().equals("54")){
                    result.setMsg("该卡已过期！");
                }else if(Object2.get("code").toString().equals("57")){
                    result.setMsg("发卡方不允许此交易！");
                }else if(Object2.get("code").toString().equals("62")){
                    result.setMsg("受限制的卡！");
                }else if(Object2.get("code").toString().equals("75")){
                    result.setMsg("密码错误次数超限！");
                }else{
                    result.setMsg("抱歉！平台繁忙，请您稍后再试！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
//        VerifyServiceImpl v = new VerifyServiceImpl();
//        ResultBean<String> result = v.verifyIdentity("","");
//        ResultBean<String> result = v.verifyPhone("","","");
//        ResultBean<JSONObject> result = v.selectLogistics("780098068058");
//      ResultBean<JSONObject> result = v.selectCredit("532722197511040914","李尚钟","","person");
//
//       ResultBean<String> result = v.selectBankCard("", "","","");
//        System.out.println("result.getCode() = " + result.getCode());
//        System.out.println("result.getMsg() = " + result.getMsg());
//        System.out.println("result.getData() = " + result.getData());
    }
}
