package com.johnliu.swipefinish.core;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

/**
 * SwipeFinishActivity.
 * <p>
 * Base activity for swiping to finish activity.
 * 
 * Created by John on 2014-6-19
 */
public class SwipeFinishActivity extends FragmentActivity {

	SwipeFinishLayout swipeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		swipeLayout = new SwipeFinishLayout(SwipeFinishActivity.this);
		swipeLayout.init(SwipeFinishActivity.this);
		ViewGroup.LayoutParams params = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		super.setContentView(swipeLayout, params);
	}

	public void setContentView(int resid) {
		View content = LayoutInflater.from(getApplicationContext()).inflate(
				resid, null);
		swipeLayout.addView(content);
	}

	/**
	 * /** Intercept a ViewPager.
	 * <p>
	 * To avoid conflict.
	 * 
	 * @param viewPager
	 */
	public void interceptViewPager(ViewPager viewPager) {
		swipeLayout.interceptViewPager(viewPager);
	}

}
