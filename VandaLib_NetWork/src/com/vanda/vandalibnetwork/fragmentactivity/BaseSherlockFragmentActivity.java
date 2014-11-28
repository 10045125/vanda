package com.vanda.vandalibnetwork.fragmentactivity;

import java.util.Map;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.vanda.vandalibnetwork.daterequest.GsonRequest;
import com.vanda.vandalibnetwork.daterequest.RequestManager;

public abstract class BaseSherlockFragmentActivity<T> extends
		SherlockFragmentActivity {

	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(BaseSherlockFragmentActivity.this);
	}

	protected void executeRequest(Request<T> request) {
		RequestManager.addRequest(request, BaseSherlockFragmentActivity.this);
	}

	protected void processData(T response) {
		RequestManager.cancelAll(BaseSherlockFragmentActivity.this);
	}

	protected void errorData(VolleyError volleyError) {
		RequestManager.cancelAll(BaseSherlockFragmentActivity.this);
	}

	protected abstract String getRequestUrl();

	protected abstract Class<T> getResponseDataClass();

	protected abstract Map<String, String> getParamMap();

	public void startExecuteRequest(int method) {
		executeRequest(new GsonRequest<T>(method, getRequestUrl(),
				getResponseDataClass(), getParamMap(),
				new Response.Listener<T>() {
					@Override
					public void onResponse(final T response) {
						processData(response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						errorData(volleyError);
					}
				}));
	}
}
