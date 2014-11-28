package com.vanda.beivandalibnetwork.application;

import com.vanda.vandalibnetwork.application.AppData;
import com.vanda.vandalibnetwork.daterequest.RequestManager;

public class VandaApplication extends AppData {

	@Override
	public void onCreate() {
		super.onCreate();
		RequestManager.newImageLoader();
	}
}
