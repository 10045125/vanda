package com.vanda.beivandalibnetwork.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vanda.beivandalibnetwork.fragment.BbsChildFragment;
import com.vanda.beivandalibnetwork.pojo.Bbs;

public class BbsPageAdapter extends FragmentStatePagerAdapter {

	private String strTitle[] = { "全部论坛", "题材作品区", "特别摄影区", "二手交易区", "全国分站区",
			"器材讨论区", "论坛服务区" };
	private Context context;
	private Bbs mBbs;
	private ArrayList<Bbs.ItemData> mArrayList;

	public BbsPageAdapter(Context context, FragmentManager fm) {
		super(fm);
		this.context = context;
		mBbs = new Bbs();
	}

	/*
	 * <item>16</item> <item>24</item> <item>27</item> <item>30</item>
	 * <item>115</item> <item>165</item>
	 */
	@Override
	public Fragment getItem(int arg0) {
		int flag = 0;
		switch (arg0) {
		case 0:
			flag = 0;
			mArrayList = mBbs.getAllData();
			break;
		case 1:
			flag = 1;
			mArrayList = mBbs.getTiCaiData();
			break;
		case 2:
			flag = 2;
			mArrayList = mBbs.getSheYingData();
			break;
		case 3:
			flag = 3;
			mArrayList = mBbs.getErShouData();
			break;
		case 4:
			flag = 4;
			mArrayList = mBbs.getQuanGuoData();
			break;
		case 5:
			flag = 5;
			mArrayList = mBbs.getQiCaiData();
			break;
		case 6:
			flag = 6;
			mArrayList = mBbs.getFuWuData();
			break;
		}
		return BbsChildFragment.newInstance(flag, mArrayList);
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
