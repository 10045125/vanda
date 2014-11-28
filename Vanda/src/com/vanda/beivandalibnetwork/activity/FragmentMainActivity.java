package com.vanda.beivandalibnetwork.activity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.vanda.beivandalibnetwork.fragment.ActionBarFragment;
import com.vanda.beivandalibnetwork.fragment.BbsFragmentSuper;
import com.vanda.beivandalibnetwork.fragment.DrawerFragment;
import com.vanda.beivandalibnetwork.fragment.DrawerFragment.FragmentInterface;
import com.vanda.beivandalibnetwork.fragment.FormeFragment;
import com.vanda.beivandalibnetwork.fragment.FragmentSuper;
import com.vanda.beivandalibnetwork.fragment.InformationFragment;
import com.vanda.beivandalibnetwork.fragment.SingleFragment;
import com.vanda.beivandalibnetwork.pojo.Category;
import com.vanda.beivandalibnetwork.utils.BlurFoldingActionBarToggle;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.fragment.BaseSwipeRefreshFragment.OnPullDownRefresh;

public class FragmentMainActivity extends SherlockFragmentActivity implements
		FragmentInterface, OnPullDownRefresh {
	private DrawerLayout mDrawerLayout;
	private FrameLayout mFrameLayout;
	private BlurFoldingActionBarToggle mDrawerToggle;
	private ImageView mImageView;
	private Category mCategory;

	private Menu mMenu;
	private PullToRefreshAttacher mPullToRefreshAttacher;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.fragment_layout);
		findViews();
		initActionBar();
		setTitle("sunnsoft for android");
		mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
		mImageView = (ImageView) findViewById(R.id.blur_image);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerLayout.setScrimColor(Color.argb(100, 0, 0, 0));
		mDrawerToggle = new BlurFoldingActionBarToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				if (mContentFragment instanceof FragmentSuper
						|| mContentFragment instanceof InformationFragment) {
					mMenu.findItem(R.id.action_refresh).setVisible(false);
				} else {
					mMenu.findItem(R.id.action_refresh).setVisible(true);
				}
				mImageView.setVisibility(View.GONE);
				mImageView.setImageBitmap(null);
			}

			public void onDrawerOpened(View drawerView) {
				mMenu.findItem(R.id.action_refresh).setVisible(false);
			}
		};
		mDrawerToggle.setBlurImageAndView(mImageView, mFrameLayout);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		PullToRefreshAttacher.Options options = new PullToRefreshAttacher.Options();
		options.headerInAnimation = R.anim.pulldown_fade_in;
		options.headerOutAnimation = R.anim.pulldown_fade_out;
		options.refreshScrollDistance = 0.3f;
		options.headerLayout = R.layout.pulldown_header;
		mPullToRefreshAttacher = new PullToRefreshAttacher(
				FragmentMainActivity.this, options);
		DrawerFragment mDrawerFragment = new DrawerFragment();
		mDrawerFragment.setFragmentInterface(this);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.left_drawer, mDrawerFragment).commit();
	}

	private void findViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
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
		getSupportMenuInflater().inflate(R.menu.main, menu);
		mMenu = menu;
		mProgressMenu = menu.findItem(R.id.refresh_loading);
		mMenu.findItem(R.id.action_refresh).setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_refresh:
			if (mContentFragment instanceof ActionBarFragment) {
				((ActionBarFragment) mContentFragment)
						.loadFirstPageAndScrollToTop();
			}
			if (mContentFragment instanceof SingleFragment) {
				((SingleFragment) mContentFragment)
						.loadFirstPageAndScrollToTop();
			}
			// if (mContentFragment instanceof FragmentSuper) {
			// ((FragmentSuper) mContentFragment).updateData();
			// }
			return true;
		case R.id.action_settings:
			setCategory(Category.set);
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

	private Fragment mContentFragment;

	public void setCategory(Category category) {
		mDrawerLayout.closeDrawer(GravityCompat.START);
		if (mCategory == category) {
			return;
		}
		mCategory = category;
		// mPullToRefreshAttacher.setRefreshing(false);
		if (category == Category.dashi) {
			// mContentFragment = FragmentShow.newInstance(1);
			setTitle(category.getDisplayName());
			mContentFragment = SingleFragment.newInstance(
					FragmentMainActivity.this, 192, 1);
		} else if (category == Category.bbs) {
			setTitle(category.getDisplayName());
			mContentFragment = BbsFragmentSuper
					.newInstance(FragmentMainActivity.this);
		} else if (category == Category.BBSWnt) {
			setTitle(category.getDisplayName());
			// mMenu.findItem(R.id.action_refresh).setVisible(false);
			mContentFragment = FragmentSuper
					.newInstance(FragmentMainActivity.this);
		} else if (category == Category.equipment) {
			setTitle(category.getDisplayName());
			mContentFragment = SingleFragment.newInstance(
					FragmentMainActivity.this, 296, 2);
		} else if (category == Category.information) {
			setTitle(category.getDisplayName());
			mContentFragment = InformationFragment
					.newInstance(FragmentMainActivity.this);
		} else {
			setTitle("关于我-Vanda");
			mContentFragment = new FormeFragment();
		}
		if ((mContentFragment instanceof FragmentSuper
				|| mContentFragment instanceof InformationFragment || mContentFragment instanceof BbsFragmentSuper)
				&& mMenu != null) {
			mMenu.findItem(R.id.action_refresh).setVisible(false);
		} else if (mMenu != null) {
			mMenu.findItem(R.id.action_refresh).setVisible(true);
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.setCustomAnimations(R.anim.pulldown_fade_in,
				R.anim.pulldown_fade_out);
		ft.replace(R.id.content_frame, mContentFragment).commit();
	}

	public PullToRefreshAttacher getPullToRefreshAttacher() {
		return mPullToRefreshAttacher;
	}

	@Override
	public void onPullDownRefreshComplete() {
		setLoadingState(false);
	}

	@Override
	public void onPullDownRefreshing() {
		setLoadingState(true);
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
