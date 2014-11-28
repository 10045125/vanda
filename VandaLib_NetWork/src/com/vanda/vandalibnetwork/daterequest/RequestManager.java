package com.vanda.vandalibnetwork.daterequest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.http.AndroidHttpClient;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.vanda.vandalibnetwork.application.AppData;
import com.vanda.vandalibnetwork.cookiestore.PersistentCookieStore;
import com.vanda.vandalibnetwork.utils.BitmapLruCache;
import com.vanda.vandalibnetwork.utils.CacheUtils;

/**
 * @author vanda 伍中联
 * 
 *         这里统一管理请求，默认的Volley是不支持Cookie的，这里加入对Cookie的支持
 *
 */
public class RequestManager {
	public static RequestQueue mRequestQueue = newRequestQueue();
	public static RequestQueue mImageRequestQueue;
	public static HttpContext CURRENT_CONTEXT;
	public static PersistentCookieStore myCookieStore;

	// List<Cookie> cookies = httpClient.getCookieStore().getCookies();
	// for (Cookie cookie:cookies){
	// myCookieStore.addCookie(cookie);
	// }

	/**
	 * 设置当前使用的上下文环境
	 * 
	 * @param context
	 */
	public static void setContext(HttpContext context) {
		CURRENT_CONTEXT = context;
	}

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

		CookieStore store = myCookieStore;
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

	public static final String ENC_UTF8 = "utf8";
	// 取运行内存阈值的1/3作为图片缓存
	private static final int MEM_CACHE_SIZE = 1024 * 1024 * ((ActivityManager) AppData
			.getContext().getSystemService(Context.ACTIVITY_SERVICE))
			.getMemoryClass() / 3;

	public static Drawable mDefaultImageDrawable = new ColorDrawable(
			Color.argb(0, 0, 0, 0));

	private static ImageLoader mImageLoader;

	private static DiskBasedCache mDiskCache;

	/**
	 * 如果设置这项，那么就会维护一个Volley的图片线程池和消息队列，是一个全局性的
	 */
	public static void newImageLoader() {
		if (mImageLoader == null) {
			mImageRequestQueue = newImageRequestQueue();
			mDiskCache = (DiskBasedCache) mImageRequestQueue.getCache();
			mImageLoader = new ImageLoader(mImageRequestQueue,
					new BitmapLruCache(MEM_CACHE_SIZE)); // 前面配置的图片下载线程，主要是为了图片下载工作
		}
	}

	private RequestManager() {
	}

	/**
	 * @return Cache 缓存的
	 */
	private static Cache openCache() {
		return new DiskBasedCache(CacheUtils.getExternalCacheDir(AppData
				.getContext()), 10 * 1024 * 1024);
	}

	/**
	 * 创建一个图片下载队列
	 * 
	 * @return
	 */
	private static RequestQueue newImageRequestQueue() {
		RequestQueue requestQueue = new RequestQueue(openCache(),
				new BasicNetwork(new HurlStack()));
		requestQueue.start();
		return requestQueue;
	}

	/**
	 * @param keyVals
	 *            可变参数转化为ArrayList
	 * @return ArrayList<NameValuePair>
	 */
	// public static ArrayList<NameValuePair> getA(Object... keyVals) {
	// ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
	// for (int i = 0; i < keyVals.length; i += 2) {
	// pairs.add(new BasicNameValuePair(
	// com.sunnsoft.easyandroid.utils.CommonUtils.str(keyVals[i]),
	// i + 1 < keyVals.length ? CommonUtils.str(keyVals[i + 1])
	// : ValidationUtils.EMPTY_STR));
	// }
	// return pairs;
	// }

	public static String getContentEncoding(HttpResponse resp,
			String defaultEncoding) {
		String encode = defaultEncoding;
		if (resp != null && resp.getEntity() != null
				&& resp.getEntity().getContentEncoding() != null) {
			encode = resp.getEntity().getContentEncoding().getValue();
		}
		return encode;
	}

	/**
	 * 创建一个网络请求队列，这个HttpClient加入Cookie和多线程的支持，主要重写了performRequest方法，加入一个上下文环境
	 * 还有就是改写了Volley的方法权限
	 * 
	 * @return RequestQueue 网络请求队列
	 */
	private static RequestQueue newRequestQueue() {
		HttpClient mHttpClient = AndroidHttpClient.newInstance("Android");
		RequestQueue requestQueue = Volley.newRequestQueueInDisk(AppData
				.getContext(), openCache(), new HttpClientStack(mHttpClient) {
			@Override
			public HttpResponse performRequest(Request<?> request,
					Map<String, String> additionalHeaders) throws IOException,
					AuthFailureError {
				HttpUriRequest httpRequest = createHttpRequest(request,
						additionalHeaders);
				addHeaders(httpRequest, additionalHeaders);
				addHeaders(httpRequest, request.getHeaders());
				onPrepareRequest(httpRequest);
				HttpParams httpParams = httpRequest.getParams();
				int timeoutMs = request.getTimeoutMs();
				// TODO: Reevaluate this connection timeout based on
				// more wide-scale
				// data collection and possibly different for wifi vs.
				// 3G.
				HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
				HttpConnectionParams.setSoTimeout(httpParams, timeoutMs);
				synchronized (mClient) {
					HttpResponse response;
					response = mClient.execute(httpRequest, CURRENT_CONTEXT);
					return response;
				}
			}

		});
		requestQueue.start();
		return requestQueue;
	}

	/**
	 * @param request
	 *            request实例请求
	 * @param tag
	 *            标记，可以方便的取消任务的执行，建议设置
	 */
	public static <T> void addRequest(Request<T> request, Object tag) {
		if (tag != null) {
			request.setTag(tag);
		}
		mRequestQueue.add(request);
	}

	/**
	 * @param tag
	 *            取消队列里面的指定tag的任务
	 */
	public static void cancelAll(Object tag) {
		mRequestQueue.cancelAll(tag);
	}

	/**
	 * @param url
	 *            返回指定URL的图片文件
	 * @return 如果创建了图片下载队列，返回File，反之返回null
	 */
	public static File getCachedImageFile(String url) {
		return mDiskCache != null ? mDiskCache.getFileForKey(url) : null;
	}

	/**
	 * @param requestUrl
	 *            图片请求的URL
	 * @param imageListener
	 *            监听器
	 * @return
	 */
	public static ImageLoader.ImageContainer loadImage(String requestUrl,
			ImageLoader.ImageListener imageListener) {
		return loadImage(requestUrl, imageListener, 0, 0);
	}

	public static ImageLoader.ImageContainer loadImage(String requestUrl,
			ImageLoader.ImageListener imageListener, int maxWidth, int maxHeight) {
		return mImageLoader.get(requestUrl, imageListener, maxWidth, maxHeight);
	}

	private static LinearLayout ll;

	public static void setLinearLayout(LinearLayout learLayout) {
		ll = learLayout;
	}

	public static ImageLoader.ImageListener getImageListener(
			final ImageView view, final int imageViewWidth,
			final Drawable defaultImageDrawable,
			final Drawable errorImageDrawable) {
		return new ImageLoader.ImageListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (errorImageDrawable != null) {
					view.setVisibility(View.VISIBLE);
					view.setImageDrawable(errorImageDrawable);
				}
			}

			@Override
			public void onResponse(ImageLoader.ImageContainer response,
					boolean isImmediate) {
				if (ll != null) {
					ll.setVisibility(View.GONE);
					ll = null;
				}
				view.setVisibility(View.VISIBLE);
				if (response.getBitmap() != null) {
					int width = response.getBitmap().getWidth();
					int height = response.getBitmap().getHeight();
					LayoutParams params = view.getLayoutParams();
					if (imageViewWidth != 0 && params != null) {
						params.width = imageViewWidth;
						params.height = (height * params.width) / width;
					}
					if (!isImmediate && defaultImageDrawable != null) {
						TransitionDrawable transitionDrawable = new TransitionDrawable(
								new Drawable[] {
										defaultImageDrawable,
										new BitmapDrawable(AppData.getContext()
												.getResources(),
												response.getBitmap()) });
						transitionDrawable.setCrossFadeEnabled(true);
						if (imageViewWidth != 0 && params != null)
							view.setLayoutParams(params);
						view.setImageDrawable(transitionDrawable);
						transitionDrawable.startTransition(100);
					} else {
						if (imageViewWidth != 0 && params != null)
							view.setLayoutParams(params);
						view.setImageBitmap(response.getBitmap());
					}
				} else if (defaultImageDrawable != null) {
					view.setImageDrawable(defaultImageDrawable);
				}
			}
		};
	}

	public static DisplayImageOptions DEFAULT_IMAGE_OPTIONS;
	public static ImageLoaderConfiguration DEFAULT_IMAGE_CONFIGS;
	public static File DEFAULT_IMAGE_CACHE_DIR;

	/**
	 * 配置图片下载，为图片安排两种方式。最好只设置一个。
	 * 
	 * @see newImageLoader
	 */
	public static void setNostra13ImageLoader() {
		DEFAULT_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();/*
																 * Builder().
																 * cacheOnDisc
																 * ().
																 * cacheInMemory
																 * () .
																 * delayBeforeLoading
																 * (100).
																 * bitmapConfig
																 * (
																 * Bitmap.Config
																 * .RGB_565) .
																 * resetViewBeforeLoading
																 * ().
																 * showImageForEmptyUri
																 * (0) .
																 * showImageOnFail
																 * ( 0).build(
																 * );
																 */
		DEFAULT_IMAGE_CACHE_DIR = CacheUtils.getExternalCacheDir(AppData
				.getContext());
		DEFAULT_IMAGE_CONFIGS = new ImageLoaderConfiguration.Builder(
				AppData.sContext)

				.threadPoolSize(3)
				// default
				.discCache(
						new FileCountLimitedDiscCache(DEFAULT_IMAGE_CACHE_DIR,
								100))
				// default
				.discCacheSize(1024 * 1024).discCacheFileCount(200)
				.memoryCacheSize(5 * 1024 * 1024)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// default
				.imageDownloader(new BaseImageDownloader(AppData.sContext)) // default
				.imageDecoder(new BaseImageDecoder(true)) // default
				.defaultDisplayImageOptions(DEFAULT_IMAGE_OPTIONS) // default
				.build();
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(
				DEFAULT_IMAGE_CONFIGS);
	}

	public interface RequestDataInterface<T> {
		public void requestDataSuccess(T response);

		public void requestDataFail(VolleyError error);
	}

	/**
	 * @param <T>
	 * @param <K>
	 * @param method
	 *            请求的方式，GET 或 POST
	 * @param url
	 *            请求的URL
	 * @param mClass
	 *            请求解析类
	 * @param map
	 *            如果为POST map为可变的body参数，如果为GET 置为null
	 * @param tag
	 *            为任务添加一个标识，建议不要为null
	 * @param l
	 *            回调接口
	 */
	public static <T> void requestData(int method, String url, Class<T> mClass,
			Map<String, String> map, final Object tag, Listener<T> listener,
			ErrorListener errorListener) {
		Request<T> mLoadRequestData = new GsonRequest<T>(method, url, mClass,
				map, listener, errorListener);
		RequestManager.addRequest(mLoadRequestData, tag != null ? tag
				: "context");
	}
}
