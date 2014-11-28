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

import java.util.Collection;

public final class ValidationUtils {
	public static final String NULL_STR = "null";
	public static final String EMPTY_STR = "";
	
	public static boolean inLongArray(long val1,long[] range)
	{
		if(range==null) return false;
		int k=0;
		for(int i=0;i<range.length;i++)
		{
			if(val1==range[i])
			{
				return true;
			}else
			{
				k++;
			}
		}
		if(k==range.length)
		{
			return false;
		}
		return false;
	}
	
	public static boolean notInLongArray(long val1,long[] range)
	{
		return !inLongArray(val1, range);
	}
	public static int inPosition(Object val1,Object... range)
	{
		if(val1==null||range==null||range.length<1) return -1;
		for(int i=0;i<range.length;i++)
		{
			if(val1.equals(range[i]))
			{
				return i;
			}
		}
		return -1;
	}
	/**
	 * 判断某值是否在指定某几个值内
	 * @param val1 值1
	 * @param range 范围值
	 * @return
	 */
	public static boolean in(Object val1, Object... range){
		if(val1 == null || range == null || range.length < 1) return false;
		for(Object aVal : range){
			if(val1.equals(aVal)) return true;
		}
		return false;
	}
	/**
	 * 判断某值是否在不在指定某几个值内
	 * @param val1 值1
	 * @param range 范围值
	 * @return
	 */
	public static boolean notIn(Object val1, Object... range){
		return !in(val1,range);
	}
	/**
	 * 确保值在指定范围内
	 * @param val
	 * @param min
	 * @param max
	 * @return
	 */
	public static long ensureInRange(long val, long min, long max){
		val = Math.max(min, val);
		val = Math.min(max,val);
		return val;
	}
	/**
	 * Check whether a string is null or blank.
	 * @param str A string
	 * @return True if <code>str</code> is null or empty string.
	 */
	public static boolean isEmpty(String str){
		return str == null || str.trim().equals("") || str.trim().equals(NULL_STR);
	}
	
	/**
	 * Indicates whether a collection is null or consist of null
	 * @param cols A collection
	 * @return True if the collection input is null, or consist of null. False if one of its item is not null
	 */
	public static boolean isEmpty(Collection<?> cols){
		if(cols == null || cols.isEmpty()) return true;
		for(Object o : cols){
			if(o != null) return false;
		}
		return true;
	}
	
	/**
	 * Indicates whether an array are null or consist of null
	 * @param array An array
	 * @return True if the array input is null, or consist of null. False if one of its item is not null
	 */
	public static boolean isEmpty(Object[] array){
		if(array == null || array.length == 0) return true;
		for(Object o : array){
			if(o != null) return false;
		}
		return true;
	}
	
	
	public static long withDefault(Long val, long l) {
		return val == null ? l : val;
	}
	
	public static int withDefault(Integer val, int i) {
		return val == null ? i : val;
	}
	
	public static String withDefault(String string, String defaultStr) {
		return isEmpty(string) ? defaultStr : string;
	}
	
	public static <T> boolean all(Validator validator, T base, T... vals){
		if(validator == null) validator = EQ;
		for(T val : vals){
			if(!validator.check(val,base)) return false;
		}
		return true;
	}
	
	public static <T> boolean any(Validator validator, T base, T... vals){
		if(validator == null) validator =  EQ;
		for(T val : vals){
			if(validator.check(val,base)) return true;
		}
		return false;
	}
	
	public static <T> boolean none(Validator validator, T base, T... vals){
		if(validator == null) validator = EQ;
		if(vals == null) return false;
		for(T val : vals){
			if(validator.check(val,base)) return false;
		}
		return true;
	}
	
	/**
	 * Use for comparation between values
	 * @author jgnan1981@163.com
	 *
	 * @param <T>
	 */
	public static interface Validator{
		boolean check(Object val, Object base);
	}
		
	
	public static final Validator GT = new Validator() {

		@SuppressWarnings("unchecked")
		@Override
		public boolean check(Object val, Object base) {
			if(val == null) return false;	//to simplify our logic, always return false when the base value is null
			return base == null ? true : 
				(val instanceof Comparable<?>) ? ((Comparable<Object>)val).compareTo(base) < 0 : false;
		}

	};
	
	public static final Validator LT = new Validator() {

		@SuppressWarnings("unchecked")
		@Override
		public boolean check(Object val, Object base) {
			if(val == null) return false;	//to simplify our logic, always return -1 when the base value is null
			//base value wins return 1
			return base == null ? true : 
				(val instanceof Comparable<?>) ? ((Comparable<Object>)val).compareTo(base) > 0 : false;
		}
	};
	
	public static final Validator EQ = new Validator() {

		@Override
		public boolean check(Object val, Object base) {
			return val == base || (val != null && base != null && val.equals(base));
		}
	};
}
