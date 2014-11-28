package com.vanda.beivandalibnetwork.activity;

import java.util.ArrayList;
import java.util.Map;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.adapter.BrandFilterActivityAdapter;
import com.vanda.beivandalibnetwork.fragment.DrawerFragment;
import com.vanda.beivandalibnetwork.pojo.BrandData;
import com.vanda.beivandalibnetwork.pojo.BrandData.Brand;
import com.vanda.beivandalibnetwork.pojo.Category;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetwork.utils.BlurFoldingActionBarToggle;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.fragmentactivity.BaseListViewSherlockFragmentActivity;

public class MainActivity extends
		BaseListViewSherlockFragmentActivity<Brand, BrandData> {

	private int compid = 0, recmnd_tp = 0, cat2_id = 0, free_deliver = 0,
			isnew = 0, ishot = 0, isasc = 0, isdesc = 0, maxorder = 0,
			maxprice = 0, minprice = 0;

	private DrawerLayout mDrawerLayout;
	private FrameLayout mFrameLayout;
	private BlurFoldingActionBarToggle mDrawerToggle;
	private ImageView mImageView;
	private Category mCategory;

	private Menu mMenu;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);
		findViews();
		initActionBar();
		setTitle("sunnsoft");
		mArrayList = new ArrayList<BrandData>();
		mList = new ArrayList<BrandData>();

		mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mImageView = (ImageView) findViewById(R.id.blur_image);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.shopping_car_listview_list);
		mPullLoadArrayAdaper = new BrandFilterActivityAdapter(this,
				R.layout.search_item, mList);
		mPullToRefreshListView.setAdapter(mPullLoadArrayAdaper);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));
		mDrawerToggle = new BlurFoldingActionBarToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				mMenu.findItem(R.id.action_refresh).setVisible(true);
				mImageView.setVisibility(View.GONE);
				mImageView.setImageBitmap(null);
			}

			public void onDrawerOpened(View drawerView) {
				mMenu.findItem(R.id.action_refresh).setVisible(false);
			}
		};
		mDrawerToggle.setBlurImageAndView(mImageView, mFrameLayout);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.left_drawer, new DrawerFragment()).commit();
		startExecuteRequest(Method.GET);
	}

	private void findViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	}

	@Override
	protected void processData(Brand response) {
		super.processData(response);
	}

	@Override
	protected void errorData(VolleyError volleyError) {
		super.errorData(volleyError);
	}

	@Override
	protected String getRequestUrl() {
		return Urls.getBrandFilterUrlData(compid, recmnd_tp, cat2_id,
				free_deliver, isnew, ishot, isasc, isdesc, maxorder, maxprice,
				minprice, 1, 20);
	}

	@Override
	protected Class<Brand> getResponseDataClass() {
		return Brand.class;
	}

	@Override
	protected Map<String, String> getParamMap() {
		return null;
	}

	@Override
	protected void addArrayListData(Brand response) {
		if (response != null && response.data != null
				&& response.data.records != null)
			mArrayList = response.data.records;
		mDataItemCount = response.data.count;
	}

	@Override
	protected String getRefDataUrl(int page, int size) {
		return Urls.getBrandFilterUrlData(compid, recmnd_tp, cat2_id,
				free_deliver, isnew, ishot, isasc, isdesc, maxorder, maxprice,
				minprice, page, size);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_refresh:
			return false;
		case R.id.action_settings:
			startActivity(new Intent(MainActivity.this,
					FragmentMainActivity.class));
			return false;
		case android.R.id.home:
			return false;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void setCategory(Category category) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		if (mCategory == category) {
			return;
		}
		mCategory = category;

		if (category == Category.dashi) {
			// mContentFragment = FollowingFragment.newInstance();
		} else if (category == Category.equipment) {
			// mContentFragment = LikeFragment.newInstance();
		} else {
			// mContentFragment = ShotsFragment.newInstance(category);
		}
		// FragmentManager fragmentManager = getSupportFragmentManager();
		// fragmentManager.beginTransaction().replace(R.id.content_frame,
		// mContentFragment).commit();
	}

}
