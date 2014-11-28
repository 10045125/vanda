package com.vanda.beivandalibnetwork.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.vanda.beivandalibnetwork.activity.FragmentMainActivity;
import com.vanda.beivandalibnetwork.adapter.BrandFilterActivityAdapter;
import com.vanda.beivandalibnetwork.pojo.BrandData;
import com.vanda.beivandalibnetwork.pojo.BrandData.Brand;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.fragment.BasePageListFragment;

public class ActionBarFragment extends BasePageListFragment<Brand, BrandData> {

	private int compid = 0, recmnd_tp = 0, cat2_id = 0, free_deliver = 0,
			isnew = 0, ishot = 0, isasc = 0, isdesc = 0, maxorder = 0,
			maxprice = 0, minprice = 0;

	public static ActionBarFragment newInstance(Context context, int flag) {
		ActionBarFragment newFragment = new ActionBarFragment(context);
		Bundle bundle = new Bundle();
		bundle.putInt("flag", flag);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	public ActionBarFragment(Context context) {
		super(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Bundle args = getArguments();
		// if (args != null) {
		// flag = args.getInt("flag");
		// }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shot, null);
		mArrayList = new ArrayList<BrandData>();
		mList = new ArrayList<BrandData>();
		mListView = (ListView) view.findViewById(R.id.listView);
		mPullLoadArrayAdaper = new BrandFilterActivityAdapter(getActivity(),
				R.layout.search_item, mList);
		mPullToRefreshAttacher = ((FragmentMainActivity) getActivity())
				.getPullToRefreshAttacher();
		initData();
		startExecuteRequest(Method.GET);
		return view;
	}

	@Override
	public void onResume() {
		Log.i("onResume", "onResume");
		super.onResume();
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
	protected void addArrayListData(Brand response) {
		if (response != null && response.data != null
				&& response.data.records != null) {
			mArrayList = response.data.records;
			mDataItemCount = response.data.count;
		}
	}

	@Override
	protected String getRefDataUrl(int page, int size) {
		return Urls.getBrandFilterUrlData(compid, recmnd_tp, cat2_id,
				free_deliver, isnew, ishot, isasc, isdesc, maxorder, maxprice,
				minprice, page, size);
	}

	@Override
	protected Class<Brand> getResponseDataClass() {
		return Brand.class;
	}

	@Override
	protected Map<String, String> getParamMap() {
		return null;
	}

}
