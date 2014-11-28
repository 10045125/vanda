package com.vanda.vandalibnetwork.fragmentactivity;

import java.util.Map;

import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.vandalibnetwork.R;
import com.vanda.vandalibnetwork.daterequest.GsonRequest;
import com.vanda.vandalibnetwork.daterequest.RequestManager;

public abstract class BaseFragmentActivity<T> extends SherlockFragmentActivity {

	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(BaseFragmentActivity.this);
	}

	protected void executeRequest(Request<T> request) {
		RequestManager.addRequest(request, BaseFragmentActivity.this);
	}

	protected void processData(T response) {
		RequestManager.cancelAll(BaseFragmentActivity.this);
	}

	protected void errorData(VolleyError volleyError) {
		RequestManager.cancelAll(BaseFragmentActivity.this);
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

	@Override
	protected void onCreate(Bundle arg0) {
		initActionBar();
		super.onCreate(arg0);
	}

	protected ActionBar actionBar;
	private ShimmerTextView mActionBarTitle;

	private void initActionBar() {
		actionBar = getSupportActionBar();
		// actionBar.setIcon(R.drawable.ic_actionbar);// R.drawable.ic_actionbar
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		View view = View.inflate(this, R.layout.actionbar_title, null);
		mActionBarTitle = (ShimmerTextView) view.findViewById(R.id.tv_shimmer);
		new Shimmer().start(mActionBarTitle);
		actionBar.setCustomView(view);
	}

	public void setTitle(int resId) {
		mActionBarTitle.setText(resId);
	}

	public void setTitle(CharSequence text) {
		mActionBarTitle.setText(text);
	}
}
