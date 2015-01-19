package com.vanda.beivandalibnetwork.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetworkdemo.R;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewActivity extends SherlockFragmentActivity {

	private String url;
	private int docid;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.webview_information);
		initActionBar();
		setTitle("详情");
		url = getIntent().getStringExtra("url");
		docid = getIntent().getIntExtra("docId",-1);
		initData();

	}

	@SuppressLint("DefaultLocale")
	private String getUrl() {
		return String
				.format("http://api.fengniao.com/app_ipad/news_iphone_doc_v2.php?docid=%s&isPad=2",
						docid);
	}

	private void initData() {
		// setLoadingState(true);
		final WebView webview = (WebView) findViewById(R.id.webview_information_webview);
		// webview.requestFocus();
		WebSettings settings = webview.getSettings();
		// 设置支持javaScript脚本语言
		settings.setJavaScriptEnabled(true);
		// settings.setSupportZoom(true);
		// settings.setAllowContentAccess(true);
		// settings.setAllowFileAccess(true);
		// settings.setBuiltInZoomControls(true);
		// 这里是支持flash的相关设置
		// settings.setPluginState(WebSettings.PluginState.ON);
		// mWebViewMyContent.loadUrl(getUrl());
		// mWebViewMyContent.setWebViewClient(new myWebViewClient());
		// }

		// WebSettings webSettings = webview.getSettings();
		// webSettings.setJavaScriptEnabled(true);
		// webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// webview.getSettings().setLoadWithOverviewMode(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webview.loadUrl(getUrl());
		webview.setWebViewClient(new myWebViewClient());

	}

	private class myWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			view.loadUrl("javascript:lazyLoad()");
			// mLinearLayoutMyProgress.setVisibility(View.GONE);
			// mWebViewMyContent.setVisibility(View.VISIBLE);
			// mLinearLayoutMyContent.setVisibility(View.VISIBLE);
			setLoadingState(false);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			// view.loadUrl(url);

			// 如果不需要其他对点击链接事件的处理返回true，否则返回false
			return true;

		}
	}

	protected ActionBar actionBar;
	private ShimmerTextView mActionBarTitle;

	private void initActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ic_actionbar);
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

	private MenuItem mProgressMenu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.progress_menu, menu);
		mProgressMenu = menu.findItem(R.id.refresh_loading);
		setLoadingState(true);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			WebViewActivity.this.finish();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setLoadingState(boolean refreshing) {
		if (mProgressMenu != null) {
			if (refreshing) {
				mProgressMenu.setActionView(R.layout.menu_progress);
				mProgressMenu.setVisible(true);
			} else {
				mProgressMenu.setVisible(false);
				mProgressMenu.setActionView(null);
			}
		}
	}
}
