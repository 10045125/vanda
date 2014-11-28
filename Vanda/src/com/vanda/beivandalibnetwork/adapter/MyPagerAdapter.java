package com.vanda.beivandalibnetwork.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vanda.beivandalibnetwork.fragment.ShowPicFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

	private String strTitle[] = { "人像", "风光", "生态", "数码", "新手", "宠物", "生活",
			"建筑" };
	private Context context;
	private ArrayList<Fragment> mArrayList;

	public MyPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
		mArrayList = new ArrayList<Fragment>();
		mArrayList.add(ShowPicFragment.newInstance(context, 101, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 125, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 16, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 24, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 27, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 30, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 115, 0));
		mArrayList.add(ShowPicFragment.newInstance(context, 165, 0));
	}

	/*
	 * <item>16</item> <item>24</item> <item>27</item> <item>30</item>
	 * <item>115</item> <item>165</item>
	 */
	@Override
	public Fragment getItem(int arg0) {
		// int flag = 0;
		// switch (arg0) {
		// case 0:
		// flag = 101;
		// break;
		// case 1:
		// flag = 125;
		// break;
		// case 2:
		// flag = 16;
		// break;
		// case 3:
		// flag = 24;
		// break;
		// case 4:
		// flag = 27;
		// break;
		// case 5:
		// flag = 30;
		// break;
		// case 6:
		// flag = 115;
		// break;
		// case 7:
		// flag = 165;
		// break;
		// }

		return mArrayList.get(arg0);
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
