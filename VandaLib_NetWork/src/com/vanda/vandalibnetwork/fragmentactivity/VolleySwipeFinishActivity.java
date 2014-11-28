package com.vanda.vandalibnetwork.fragmentactivity;

import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.johnliu.swipefinish.core.SwipeFinishActivity;
import com.vanda.vandalibnetwork.daterequest.GsonRequest;
import com.vanda.vandalibnetwork.daterequest.RequestManager;

public abstract class VolleySwipeFinishActivity<T> extends SwipeFinishActivity {

	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(VolleySwipeFinishActivity.this);
	}

	protected void executeRequest(Request<T> request) {
		RequestManager.addRequest(request, VolleySwipeFinishActivity.this);
	}

	protected void processData(T response) {
		RequestManager.cancelAll(VolleySwipeFinishActivity.this);
	}

	protected void errorData(VolleyError volleyError) {
		RequestManager.cancelAll(VolleySwipeFinishActivity.this);
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
