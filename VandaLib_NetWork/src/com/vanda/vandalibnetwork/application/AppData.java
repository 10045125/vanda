package com.vanda.vandalibnetwork.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.vanda.vandalibnetwork.cookiestore.PersistentCookieStore;
import com.vanda.vandalibnetwork.daterequest.RequestManager;
import com.vanda.vandalibnetwork.staticdata.StaticData;

public class AppData extends Application {
	public static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();
		DisplayMetrics metric = new DisplayMetrics();
		WindowManager manager = (WindowManager) sContext
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metric);
		StaticData.ScreenWidth = metric.widthPixels;
		RequestManager.myCookieStore = new PersistentCookieStore(sContext);
		RequestManager.cookieContext(true); // login out clean HttpContext;
	}

	public static Context getContext() {
		return sContext;
	}

}
