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
package com.vanda.beivandalibnetwork.utils;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.text.TextUtils;
/**
 * 通用工具
 * @author jgnan1981@163.com
 *
 */
public final class CommonUtils {
	private final static String FILE_PROTOCAL = "file://";
	
	public static String formatNumber(int num){
		return NumberFormat.getInstance().format(num);
	}
	
	/**
	 * Change something to string
	 * @param obj
	 * @return
	 */
	public static String str(Object obj){
		return obj == null ? null : obj.toString();
	}
	
	/**
	 * 判断两个类之间是否存在关系
	 * @param clazz1
	 * @param clazz2
	 * @return
	 */
	public static boolean isAssignable(Class<?> clazz1, Class<?> clazz2){
		if(clazz1 == null || clazz2 == null) return false;
		return clazz1.isAssignableFrom(clazz2) || clazz2.isAssignableFrom(clazz1);
	}
	
	/**
	 * 判断两个类之间是否存在关系
	 * @param clazz1
	 * @param clazz2
	 * @return
	 */
	public static boolean isAssignable(Object obj, Class<?> clazz){
		return isAssignable(obj == null ? null : obj.getClass(), clazz);
	}
	
	/**
	 * 通过URL获取文件名
	 * @param url
	 * @return
	 */
	public static String getFileNameFromUrl(String url) {
		if(url == null) return null;
		String[] segs = url.split("/");
		return segs[segs.length-1];
	}
	
	public static String readString(Parcel in){
		String val = in.readString();
		return TextUtils.isEmpty(val) || val.equals("null") ? null : val;
	}
	
	public static String readString(JSONObject json,String name) throws JSONException{
		String val = json.has(name) ? json.getString(name) : null;
		return TextUtils.isEmpty(val) || val.equals("null") ? null : val;
	}
	
	public static String addFilePrefix(String dataString) {
		return dataString != null && !dataString.startsWith(FILE_PROTOCAL) ? FILE_PROTOCAL + dataString : dataString;
	}

}
