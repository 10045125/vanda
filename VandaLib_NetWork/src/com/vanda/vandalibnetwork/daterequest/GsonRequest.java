package com.vanda.vandalibnetwork.daterequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author vanda 伍中联 Volley框架是一个线程池+消息队列的形式，而消息队列是以Request为基本单位。
 *         因此只要有访问网络的操作，只要建立一个request，将这个消息加入消息队列中就可以完成一系列操作
 * @param <T>
 */
public class GsonRequest<T> extends Request<T> {

	private final Gson mGson = new Gson();
	/** 解析类 */
	private final Class<T> mClazz;
	/** 回调接口提供解析响应. */
	private final Listener<T> mListener;

	private Map<String, String> mMap;

	/**
	 * @param method
	 *            常用的GET、POST
	 * @param url
	 *            请求的URL
	 * @param clazz
	 *            请求数据后的解析类
	 * @param params
	 *            支持POST 中body可变参数，GET 中置null
	 * @param listener
	 *            网络请求成功的接口调用
	 * @param errorListener
	 *            网络错误处理
	 */
	public GsonRequest(int method, String url, Class<T> clazz,
			Map<String, String> params, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		this.mClazz = clazz;
		this.mListener = listener;
		this.mMap = params;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#getParams() POST 方式将可变参数以Map的形式传入，GET
	 * 为null
	 */
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return this.mMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#deliverResponse(java.lang.Object)
	 * 
	 * 请求数据成功调用方法
	 */
	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.android.volley.Request#parseNetworkResponse(com.android.volley.
	 * NetworkResponse) 默认编码方式为iso-8899; 在工作线程解析响应。
	 */
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			Log.i("json->", json);
			return Response.success(mGson.fromJson(json, mClazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}

}