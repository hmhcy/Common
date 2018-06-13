package com.hxhy.api.responseEntity;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import io.swagger.annotations.ApiModelProperty;

/**
 * 列表返回对象封装
 * @author Administrator
 *
 * @param <T>
 */
@PropertySource("classpath:configure.properties")
public class ListEntity<T> {
	
    @ApiModelProperty(value = "列表数据", hidden = false)
    private T list;

    public ListEntity() {
        super();
    }

	public T getList() {
		return list;
	}

	public void setList(T list) {
		this.list = list;
	}
}
