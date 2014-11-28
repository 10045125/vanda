package com.vanda.beivandalibnetwork.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.vanda.beivandalibnetwork.adapter.MyPagerAdapter;
import com.vanda.beivandalibnetwork.utils.ZoomOutPageTransformer;
import com.vanda.beivandalibnetworkdemo.R;

public class FragmentSuper extends SherlockFragment {

	private ViewPager mViewPager;
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private Context context;
	private MyPagerAdapter mMyPagerAdapter;
	private Fragment mFragment;

	public static FragmentSuper newInstance(Context context) {
		FragmentSuper mFragmentSuper = new FragmentSuper();
		mFragmentSuper.context = context;
		return mFragmentSuper;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_child_layout, null);
		mViewPager = (ViewPager) view.findViewById(R.id.content_pager);
		mMyPagerAdapter = new MyPagerAdapter(context, getFragmentManager());
		mFragment = mMyPagerAdapter.getItem(0);
		mViewPager.setAdapter(mMyPagerAdapter);
		mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		mPagerSlidingTabStrip = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mFragment = mMyPagerAdapter.getItem(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		// mPagerSlidingTabStrip.setTextColor(Color.parseColor("#fe4200"));
		mPagerSlidingTabStrip.setTextSize(26);
		mPagerSlidingTabStrip.setSelectTabTextSize(28);
		return view;
	}

	public void updateData() {
		if (mFragment != null)
			((ShowPicFragment) mFragment).loadFirstPageAndScrollToTop();
	}

}
