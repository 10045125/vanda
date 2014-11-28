package com.vanda.beivandalibnetwork.pojo;

import java.util.ArrayList;

import android.content.res.Resources;

import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.application.AppData;

public class Bbs {
	/*
	 * <item>热帖</item> <item>精华帖</item> <item>最新帖子</item> <item>最新回复</item>
	 * </string-array> <integer-array name="bbs_sub_class_id_0"> <item>0</item>
	 * <item>1</item> <item>2</item> <item>3</item>
	 */

	public class ItemData {
		public String title;
		public int id;
	}

	public ArrayList<ItemData> getAllData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		if (itemData != null)
			itemData.clear();
		ItemData mItemData = new ItemData();
		mItemData.id = 0;
		mItemData.title = "热帖";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 1;
		mItemData.title = "精华帖";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 2;
		mItemData.title = "最新帖子";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 3;
		mItemData.title = "最新回复";
		itemData.add(mItemData);
		return itemData;

	}

	/*
	 * <item>人像</item> <item>风光</item> <item>纪实</item> <item>旅行</item>
	 * <item>人体</item> <item>儿童</item> <item>体育</item> <item>建筑</item>
	 * <item>生态</item> <item>宠物</item> </string-array> <integer-array
	 * name="bbs_sub_class_id_1"> <item>101</item> <item>125</item>
	 * <item>20</item> <item>15</item> <item>77</item> <item>297</item>
	 * <item>164</item> <item>165</item> <item>16</item> <item>30</item>
	 */

	public ArrayList<ItemData> getTiCaiData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		if (itemData != null)
			itemData.clear();
		ItemData mItemData = new ItemData();
		mItemData.id = 101;
		mItemData.title = "人像";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 125;
		mItemData.title = "风光";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 20;
		mItemData.title = "纪实";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 15;
		mItemData.title = "旅行";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 77;
		mItemData.title = "人体";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 297;
		mItemData.title = "儿童";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 164;
		mItemData.title = "体育";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 165;
		mItemData.title = "建筑";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 16;
		mItemData.title = "生态";
		itemData.add(mItemData);
		mItemData = new ItemData();
		mItemData.id = 30;
		mItemData.title = "宠物";
		itemData.add(mItemData);
		return itemData;
	}

	// Resources res = getResources();
	// String[] planets = res.getStringArray(R.array.planets_array);

	public ArrayList<ItemData> getSheYingData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		Resources res = AppData.getContext().getResources();
		String[] planets = res.getStringArray(R.array.bbs_sub_class_2);
		int[] planets1 = res.getIntArray(R.array.bbs_sub_class_id_2);
		for (int i = 0; i < planets.length; i++) {
			ItemData mItemData = new ItemData();
			mItemData.id = planets1[i];
			mItemData.title = planets[i];
			itemData.add(mItemData);
		}
		return itemData;
	}

	public ArrayList<ItemData> getErShouData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		Resources res = AppData.getContext().getResources();
		String[] planets = res.getStringArray(R.array.bbs_sub_class_3);
		int[] planets1 = res.getIntArray(R.array.bbs_sub_class_id_3);
		for (int i = 0; i < planets.length; i++) {
			ItemData mItemData = new ItemData();
			mItemData.id = planets1[i];
			mItemData.title = planets[i];
			itemData.add(mItemData);
		}
		return itemData;
	}

	public ArrayList<ItemData> getQuanGuoData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		Resources res = AppData.getContext().getResources();
		String[] planets = res.getStringArray(R.array.bbs_sub_class_4);
		int[] planets1 = res.getIntArray(R.array.bbs_sub_class_id_4);
		for (int i = 0; i < planets.length; i++) {
			ItemData mItemData = new ItemData();
			mItemData.id = planets1[i];
			mItemData.title = planets[i];
			itemData.add(mItemData);
		}
		return itemData;
	}

	public ArrayList<ItemData> getQiCaiData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		Resources res = AppData.getContext().getResources();
		String[] planets = res.getStringArray(R.array.bbs_sub_class_5);
		int[] planets1 = res.getIntArray(R.array.bbs_sub_class_id_5);
		for (int i = 0; i < planets.length; i++) {
			ItemData mItemData = new ItemData();
			mItemData.id = planets1[i];
			mItemData.title = planets[i];
			itemData.add(mItemData);
		}
		return itemData;
	}

	public ArrayList<ItemData> getFuWuData() {
		ArrayList<ItemData> itemData = new ArrayList<Bbs.ItemData>();
		Resources res = AppData.getContext().getResources();
		String[] planets = res.getStringArray(R.array.bbs_sub_class_6);
		int[] planets1 = res.getIntArray(R.array.bbs_sub_class_id_6);
		for (int i = 0; i < planets.length; i++) {
			ItemData mItemData = new ItemData();
			mItemData.id = planets1[i];
			mItemData.title = planets[i];
			itemData.add(mItemData);
		}
		return itemData;
	}
}
