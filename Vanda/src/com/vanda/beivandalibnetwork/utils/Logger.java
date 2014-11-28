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

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * 日志记录帮助类.
 * 
 * @author jgnan1981@163.com
 *
 */
public final class Logger {
	private static boolean DEBUG = false;
	private String tag;

	public static void init(Context context) {
		DEBUG = (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
	}

	public Logger(String tag) {
		this.tag = tag;
	}

	public Logger(Class<?> clazz) {
		this(clazz.getName());
	}

	public void i(String msg, Object... args) {
		Logger.info(tag, msg, args);
	}

	public void d(String msg, Object... args) {
		Logger.debug(tag, msg, args);
	}

	public void e(String msg, Object... args) {
		Logger.error(tag, msg, args);
	}

	public void w(String msg, Object... args) {
		Logger.warn(tag, msg, args);
	}

	public static void info(String tag, String msg, Object... args) {
		if (args != null && args.length > 0)
			Log.i(tag, String.format(msg, args));
		else
			Log.i(tag, msg);
	}

	public static void warn(String tag, String msg, Object... args) {
		if (args != null && args.length > 0)
			Log.w(tag, String.format(msg, args));
		else
			Log.w(tag, msg);
	}

	public static void debug(String tag, String msg, Object... args) {
		if (!DEBUG)
			return;
		if (args != null && args.length > 0)
			Log.d(tag, String.format(msg, args));
		else
			Log.d(tag, msg);
	}

	public static void error(String tag, String msg, Object... args) {
		if (args != null && args.length > 0)
			Log.e(tag, String.format(msg, args));
		else
			Log.e(tag, msg);
	}

}
