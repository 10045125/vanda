package com.vanda.vandalibnetwork.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * @author vanda 伍中联 主要是为Volley框架指定缓存策略 使用LruCache
 *
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements
		ImageLoader.ImageCache {

	public BitmapLruCache(int maxSize) {
		super(maxSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.util.LruCache#sizeOf(java.lang.Object,
	 * java.lang.Object)
	 * 
	 * 如果想实现缓存大小真实的控制，必须重载此方法，原方法是已一份一份为单位的。
	 */
	@Override
	protected int sizeOf(String key, Bitmap bitmap) {

		return ImageUtils.getBitmapSize(bitmap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.volley.toolbox.ImageLoader.ImageCache#getBitmap(java.lang
	 * .String) ImageLoader.ImageCache 接口实现
	 */
	@Override
	public Bitmap getBitmap(String url) {

		return get(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.android.volley.toolbox.ImageLoader.ImageCache#putBitmap(java.lang
	 * .String, android.graphics.Bitmap)ImageLoader.ImageCache 接口实现
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {

		put(url, bitmap);
	}
}
