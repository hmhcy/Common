package com.hxhy.api.responseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


import io.swagger.annotations.ApiModelProperty;

/**
 * 分页返回对象封装
 * @author Administrator
 *
 * @param <T>
 */
@PropertySource("classpath:configure.properties")
public class PageEntity<T> {
    public static final boolean FIRST_PAGE = true;
    public static final boolean LAST_PAGE = false;
    public static final Integer COUNT = 0;
    
   
    @ApiModelProperty(value = "是否为第一页", hidden = false)
    private boolean firstPage = FIRST_PAGE;
    
    @ApiModelProperty(value = "是否为最后一页", hidden = false)
    private boolean lastPage = LAST_PAGE;
    
    @ApiModelProperty(value = "总数", hidden = false)
    private Integer count = COUNT;
    
    @ApiModelProperty(value = "列表数据", hidden = false)
    private List<T> list;

    public PageEntity() {
        super();
    }

	public boolean isFirstPage() {
		return firstPage;
	}

	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}

	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
}
