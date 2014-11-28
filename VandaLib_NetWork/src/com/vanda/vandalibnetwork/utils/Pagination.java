/***********************************************************************************************************************
 * 
 * Copyright (C) 2013, 2014 by sunnsoft (http://www.sunnsoft.com)
 * http://www.sunnsoft.com/
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package com.vanda.vandalibnetwork.utils;

/**
 * 分页工具类.
 * @author jgnan1981@163.com
 *
 */
public class Pagination {
	public long loaded = 0;
	public long total = 0;
	public int page = 1;
	public int size = 10;
	
	public Pagination(){}
	public Pagination(int size){
		this.size = size;
	}
	public Pagination(long loaded, long total, int size){
		this.loaded = loaded;
		this.total = total;
		this.size = size;
	}
	
	public Pagination updateLoaded(int count){
		this.loaded += count;
		return this;
	}
	
	/**
	 * 清除翻页信息
	 */
	public Pagination clear(){
		loaded = 0;
		total = 0;
		page = 1;
		return this;
	}
	
	
	/**
	 * 是否最后一条记录
	 * @return
	 */
	public boolean isLast(){
		return loaded == total;
	}
	

	
	/**
	 * 是否需要加载更多数据
	 * @return
	 */
	public boolean hasMore(){
		return loaded < total;
	}
}
