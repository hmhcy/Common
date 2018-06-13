package com.hxhy.api.responseEntity;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import com.hxhy.config.util.ConfigureProperties;
import com.hxhy.config.util.ConstantUtils;
import com.hxhy.config.util.SpringUtil;

import io.swagger.annotations.ApiModelProperty;

/**
 * 返回结果封装
 * @author Administrator
 *
 * @param <T>
 */

public class ResultBean<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static ConfigureProperties config = SpringUtil.getBean(ConfigureProperties.class);
    
    @ApiModelProperty(value = "图片头部链接", hidden = false)
  	private String headUrl = config.getHeadUrl();
    
    @ApiModelProperty(value = "操作说明", hidden = false, example = "操作成功")
    private String msg = "操作成功。";
    
    @ApiModelProperty(value = "操作状态", hidden = false, example = "1")
    private int code = ConstantUtils.RESULT_SUCCESS;
    
    @ApiModelProperty(value = "返回数据", hidden = false)
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = ConstantUtils.RESULT_FAILED;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
}
