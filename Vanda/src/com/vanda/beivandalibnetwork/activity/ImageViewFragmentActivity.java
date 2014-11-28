package com.vanda.beivandalibnetwork.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.adapter.PhotoViewPageAdapter;
import com.vanda.beivandalibnetwork.fragment.PhotoImageViewFragment.OnPhotoViewClickListener;
import com.vanda.beivandalibnetwork.pojo.ImageViewPoJo;
import com.vanda.beivandalibnetwork.utils.HttpUtils;
import com.vanda.beivandalibnetwork.utils.ViewPagerFixed;
import com.vanda.beivandalibnetworkdemo.R;
import com.wzl.vandan.dialog.VandaAlert;

public class ImageViewFragmentActivity extends SherlockFragmentActivity
		implements OnPhotoViewClickListener {

	private ArrayList<ImageViewPoJo.Data> mArrayList;
	private String url;
	private String title;
	private PhotoViewPageAdapter mPhotoViewPageAdapter;
	private ViewPagerFixed mViewPager;
	private TextView mTextViewTitle, mTextViewPage, mTextViewPageCount;
	private FrameLayout mFrameLayout;

	private boolean isHide = false;
	private Animation inAnimation;
	private Animation outAnimation;
	private Dialog mDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.photo_imageview_viewpage);
		mViewPager = (ViewPagerFixed) findViewById(R.id.content_viewpager);
		mArrayList = new ArrayList<ImageViewPoJo.Data>();
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		mPhotoViewPageAdapter = new PhotoViewPageAdapter(this,
				getSupportFragmentManager(), mArrayList);
		initActionBar();
		setTitle("美图详情");
		initData();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				arg0 += 1;
				mTextViewPage.setText("" + arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mDialog = VandaAlert.createLoadingDialog(this, "数据载入中...");
		startGetData();
	}

	private void initData() {
		mTextViewTitle = (TextView) findViewById(R.id.title);
		mTextViewTitle.setText(title);
		mTextViewPageCount = (TextView) findViewById(R.id.title_3);
		mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
		mTextViewPage = (TextView) findViewById(R.id.title_1);
		mTextViewPage.setText("" + 1);
		inAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		outAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
	}

	private void startGetData() {
		mDialog.show();
		new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				String json = null;
				try {
					json = HttpUtils.get(url);
					Gson gson = new Gson();
					JsonParser parser = new JsonParser();
					JsonArray Jarray = parser.parse(json).getAsJsonArray();

					// ArrayList<ImageViewPoJo.Data> lcs = new
					// ArrayList<ImageViewPoJo.Data>();

					for (JsonElement obj : Jarray) {
						ImageViewPoJo.Data cse = gson.fromJson(obj,
								ImageViewPoJo.Data.class);
						mArrayList.add(cse);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return "error";
				}
				return "ok";
			}

			@Override
			protected void onPostExecute(String result) {
				mDialog.dismiss();
				if (result.equals("ok")) {
					mViewPager.setAdapter(mPhotoViewPageAdapter);
					mPhotoViewPageAdapter.notifyDataSetChanged();
					mTextViewPageCount.setText("" + mArrayList.size());
				}
				super.onPostExecute(result);
			}
		}.execute();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			ImageViewFragmentActivity.this.finish();
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void setOnClickEvent() {
		isHide = !isHide;
		if (isHide) {
			actionBar.hide();
			// TranslateAnimation animate = new TranslateAnimation(0,
			// -mFrameLayout.getHeight(), 0, 0);
			// animate.setDuration(500);
			// animate.setFillAfter(true);
			mFrameLayout.startAnimation(outAnimation);
			mFrameLayout.setVisibility(View.GONE);
		} else {
			actionBar.show();
			// TranslateAnimation animate = new TranslateAnimation(0,
			// mFrameLayout.getHeight(), 0, 0);
			// animate.setDuration(500);
			// animate.setFillAfter(true);
			mFrameLayout.startAnimation(inAnimation);
			mFrameLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void downImage() {
		Toast.makeText(this, "下载中...", Toast.LENGTH_SHORT).show();
	}
}
