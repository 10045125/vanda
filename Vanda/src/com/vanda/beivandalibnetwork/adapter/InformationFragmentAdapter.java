package com.vanda.beivandalibnetwork.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vanda.beivandalibnetwork.fragment.InformationChildFragment;
import com.vanda.beivandalibnetwork.fragment.JingXuanFragment;

public class InformationFragmentAdapter extends FragmentStatePagerAdapter {

	private String strTitle[] = { "精选", "器材", "影像", "学院", "旅游", "汽车", "手机" };
	private Context context;
	private ArrayList<Fragment> mArratList;

	public InformationFragmentAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
		mArratList = new ArrayList<Fragment>();
		mArratList.add(JingXuanFragment.newInstance(context, 296));
		mArratList.add(InformationChildFragment.newInstance(context, 296, 1));
		mArratList.add(InformationChildFragment.newInstance(context, 192, 2));
		mArratList.add(InformationChildFragment.newInstance(context, 190, 3));
		mArratList.add(InformationChildFragment.newInstance(context, 278, 4));
		mArratList.add(InformationChildFragment.newInstance(context, 305, 5));
		mArratList.add(InformationChildFragment.newInstance(context, 340, 6));
	}

	/*
	 * <item>0</item> <item>296</item> <item>192</item> <item>190</item>
	 * <item>278</item> <item>305</item> <item>340</item>
	 */
	@Override
	public Fragment getItem(int arg0) {
		// int flag = 0;
		// switch (arg0) {
		// case 0:
		// flag = 296;
		// return JingXuanFragment.newInstance(context, flag);
		// case 1:
		// flag = 296;
		// break;
		// case 2:
		// flag = 192;
		// break;
		// case 3:
		// flag = 190;
		// break;
		// case 4:
		// flag = 278;
		// break;
		// case 5:
		// flag = 305;
		// break;
		// case 6:
		// flag = 340;
		// break;
		// }
		return mArratList.get(arg0);
	}

	@Override
	public int getCount() {
		return strTitle.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return strTitle[position];
	}
}