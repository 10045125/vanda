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
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 **********************************************************************************************************************/
package com.vanda.beivandalibnetwork.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Base64;

public final class HttpUtils {
	private static final String TAG = HttpUtils.class.getSimpleName();

	public static final String ENC_UTF8 = "utf8";
	// 通过nginx传过来的真实用户IP
	public static final String HEADER_REAL_IP = "X-Real-IP";
	// 通过nginx传过来的真实用户IP
	public static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
	/** Content-Type请求头 */
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	/** Content-Length请求头 */
	public static final String HEADER_CONTENT_LENGTH = "Content-Length";
	/** 基础校验请求头 */
	public static final String HEADER_AUTHORIZATION = "Authorization";
	/** Content-Type 为XML的请求头 */
	public static final String CONTENT_TYPE_XML = "text/xml; charset= utf-8";
	private static final String CHAR_AMP = "&";

	private static HttpContext CURRENT_CONTEXT;

	/**
	 * 设置当前使用的上下文环境
	 * 
	 * @param context
	 */
	public static void setContext(HttpContext context) {
		CURRENT_CONTEXT = context;
	}

	/**
	 * 对所有参数进行编码，然后返回query string
	 * 
	 * @param params
	 * @return
	 */
	public static String encodeParams(Map<String, String> params) {
		if (params == null)
			return null;
		// copy one
		StringBuilder builder = new StringBuilder();
		String key;
		for (Iterator<String> keyIter = params.keySet().iterator(); keyIter
				.hasNext();) {
			key = keyIter.next();
			try {
				builder.append(key).append("=")
						.append(URLEncoder.encode(params.get(key), ENC_UTF8));
				if (keyIter.hasNext())
					builder.append(CHAR_AMP);
			} catch (UnsupportedEncodingException e) {
				// one of the params could not encode
				Logger.warn(
						TAG,
						String.format(
								"[encodeParams] one of the params %s could not encode to utf8, value -> %s",
								key, params.get(key)));
			}
		}
		return builder.toString();
	}

	public static final String ENCODING_UTF8 = "utf-8";
	public static int ConnectionTimeout = 30000;
	public static int SoTimeout = 30000;

	/* 登录请求开头 */
	private static final String AUTH_METHOD_BASIC = "Basic ";

	// public static void setConnectionTimeout

	/**
	 * 创建一个使用Cookie的上下文环境
	 * 
	 * @param setAsCurrentContext
	 *            是否创建为当前的上下文环境，如果为true替换CURRENT_CONTEXT
	 * @return
	 */
	public static HttpContext cookieContext(boolean setAsCurrentContext) {
		if (getCookieStore(CURRENT_CONTEXT) != null)
			return CURRENT_CONTEXT;

		CookieStore store = new BasicCookieStore();
		HttpContext context = new BasicHttpContext();
		context.setAttribute(ClientContext.COOKIE_STORE, store);
		if (setAsCurrentContext)
			CURRENT_CONTEXT = context;
		return context;
	}

	/**
	 * 获取 Cookie存储.
	 * 
	 * @param context
	 *            上下文，为空则使用当前上下文
	 * @return
	 */
	public static CookieStore getCookieStore(HttpContext context) {
		HttpContext ctx = context == null ? CURRENT_CONTEXT : context;
		return (CookieStore) (ctx == null ? null : ctx
				.getAttribute(ClientContext.COOKIE_STORE));
	}

	/**
	 * 获取指定的Cookie信息
	 * 
	 * @param context
	 *            上下文，为空则使用默认上下文
	 * @param name
	 *            Cookie名称
	 * @return
	 */
	public static Cookie getCookie(final HttpContext context, String name) {
		HttpContext ctx = context == null ? CURRENT_CONTEXT : context;
		if (ctx == null)
			return null;
		CookieStore store = (CookieStore) ctx
				.getAttribute(ClientContext.COOKIE_STORE);
		if (store == null)
			return null;

		for (Cookie cookie : store.getCookies()) {
			if (cookie.getName().equals(name))
				return cookie;
		}
		return null;
	}

	/**
	 * Execute a specific request
	 * 
	 * @param request
	 * @param ctimeout
	 *            Cache timeout
	 * @param sotimeout
	 *            So timeout
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String execute(HttpUriRequest request, int ctimeout,
			int sotimeout) throws ClientProtocolException, IOException {
		return execute(request, null, ctimeout, sotimeout);
	}

	/**
	 * Execute a specific request
	 * 
	 * @param request
	 *            Request from user
	 * @param context
	 *            Context to use for the request
	 * @param ctimeout
	 *            Cache timeout
	 * @param sotimeout
	 *            So timeout
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String execute(HttpUriRequest request, HttpContext context,
			int ctimeout, int sotimeout) throws IOException {
		String result = null;
		HttpClient client = new DefaultHttpClient();

		if (context == null)
			context = CURRENT_CONTEXT;
		try {
			HttpConnectionParams.setConnectionTimeout(client.getParams(),
					ctimeout);
			HttpConnectionParams.setSoTimeout(client.getParams(), sotimeout);
			HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
			HttpResponse resp = client.execute(request, context);
			result = EntityUtils.toString(resp.getEntity(),
					getContentEncoding(resp));
		} catch (ClientProtocolException e) {
			Logger.warn(TAG,
					"[execute]Error when calling url >> " + request.getURI(), e);
		}
		// } catch (IOException e) {
		// if(e instanceof ConnectTimeoutException){
		// if(l != null)
		// l.connectTimeOutInter();
		// }
		// if(e instanceof SocketTimeoutException){
		// if(l != null)
		// l.socketTimeOutInter();
		//
		// }
		// Logger.warn(TAG, "[execute]IOException when calling url >> " +
		// request.getURI(), e);
		// }
		return result;
	}

	/**
	 * 执行方法
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String execute(HttpUriRequest request)
			throws ClientProtocolException, IOException {
		return execute(request, null);
	}

	/**
	 * 执行方法
	 * 
	 * @param request
	 * @param context
	 *            上下文
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String execute(HttpUriRequest request, HttpContext context)
			throws IOException {
		return execute(request, context, ConnectionTimeout, SoTimeout);
	}

	/**
	 * Encode url to specific character set
	 * 
	 * @param str
	 * @param charset
	 * @return
	 */
	public static String encodeUrl(String str, String charset) {
		String url = str;
		try {
			url = URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			Logger.warn(
					TAG,
					"[encodeUrl]Could not encode url. Error >>> "
							+ e.getMessage());
		}
		return url;
	}

	/**
	 * Encode url using utf-8
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeUrl(String str) {
		return encodeUrl(str, ENCODING_UTF8);
	}

	/**
	 * Create a url encoded form
	 * 
	 * @param request
	 *            Request object
	 * @param keyVals
	 *            Key and value pairs, the even(begins with 0) position params
	 *            are key and the odds are values
	 * @return
	 */
	public static HttpUriRequest urlEncodedForm(HttpPost request,
			Object... keyVals) {
		return urlEncodedForm(ENC_UTF8, request, keyVals);
	}

	/**
	 * Create a url encoded form with a given character encoding code.
	 * 
	 * @param enc
	 *            Encoding code
	 * @param request
	 *            Post request
	 * @param keyVals
	 * @return
	 */
	public static HttpUriRequest urlEncodedForm(String enc, HttpPost request,
			Object... keyVals) {
		enc = ValidationUtils.isEmpty(enc) ? ENC_UTF8 : enc;
		if (request == null || ValidationUtils.isEmpty(keyVals))
			return request;
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for (int i = 0; i < keyVals.length; i += 2) {
			pairs.add(new BasicNameValuePair(CommonUtils.str(keyVals[i]),
					i + 1 < keyVals.length ? CommonUtils.str(keyVals[i + 1])
							: ValidationUtils.EMPTY_STR));
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(pairs, enc));
		} catch (UnsupportedEncodingException e) {
			Logger.warn(TAG,
					"[urlEncodedForm]Fill data to form entity failed. Caused by "
							+ e.getMessage());
		}
		return request;
	}

	/**
	 * Execute a get request and return the string result
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String get(String url) throws IOException {
		return execute(new HttpGet(url), null);
	}

	/**
	 * 提交请求
	 * 
	 * @param url
	 *            请求地址
	 * @param keyVals
	 *            参数对
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String post(String url, Object... keyVals) {
		try {
			return execute(urlEncodedForm(new HttpPost(url), keyVals), null);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 提交请求
	 * 
	 * @param url
	 *            请求地址
	 * @param context
	 *            上下文
	 * @param keyVals
	 *            参数对
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String post(String url, HttpContext context,
			Object... keyVals) throws ClientProtocolException, IOException {
		return execute(urlEncodedForm(new HttpPost(url), keyVals), context);
	}

	/**
	 * 基础的校验检查
	 * 
	 * @param request
	 * @param username
	 * @param password
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	public static HttpUriRequest authenticate(HttpUriRequest request,
			String username, String password) {
		request.addHeader(
				HEADER_AUTHORIZATION,
				AUTH_METHOD_BASIC
						+ Base64.encodeToString(
								(username + ":" + password).getBytes(),
								Base64.NO_WRAP));
		return request;
	}

	/**
	 * Execute a get request and return the string result
	 * 
	 * @param url
	 *            Url to request
	 * @param context
	 *            HttpContext to use
	 * @return
	 * 
	 *         public static String get(String url, HttpContext context) {
	 *         return execute(new HttpGet(url),context); }
	 * 
	 *         /** Get response content encoding value. With utf-8 as default
	 *         value if not exists
	 * 
	 * @param resp
	 * @return
	 */
	public static String getContentEncoding(HttpResponse resp) {
		return getContentEncoding(resp, ENCODING_UTF8);
	}

	/**
	 * * Get response content encoding value. With <i>defaultEncoding</i> as
	 * default value if not exists
	 * 
	 * @param resp
	 * @param defaultEncoding
	 * @return
	 */
	public static String getContentEncoding(HttpResponse resp,
			String defaultEncoding) {
		String encode = defaultEncoding;
		if (resp != null && resp.getEntity() != null
				&& resp.getEntity().getContentEncoding() != null) {
			encode = resp.getEntity().getContentEncoding().getValue();
		}
		return encode;
	}

}
