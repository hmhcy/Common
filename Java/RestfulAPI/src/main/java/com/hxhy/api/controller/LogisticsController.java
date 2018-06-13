package com.hxhy.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hxhy.api.responseEntity.ResultBean;
import com.hxhy.api.service.VerifyService;
import com.hxhy.config.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "logistics")
@Api(value = "物流信息", tags = { "物流信息" })
public class LogisticsController {
	@Autowired
	private VerifyService verifyService;
	
	
	@ApiOperation(value = "全球物流快递查询")
	@RequestMapping(value = "/selectLogistics", method = RequestMethod.POST)
	public ResultBean<JSONObject> selectLogistics(@RequestParam String logisticsNum) {
		ResultBean<JSONObject> result = new ResultBean<JSONObject>();
		if (StringUtils.isEmpty(logisticsNum)) {
			result.setCode(-1);
			result.setMsg("物流单号不能为空");
		} else {
			result = verifyService.selectLogistics(logisticsNum);
		}
		return result;
	}
}
