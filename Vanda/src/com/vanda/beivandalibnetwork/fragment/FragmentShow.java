package com.vanda.beivandalibnetwork.fragment;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vanda.beivandalibnetwork.adapter.BrandFilterActivityAdapter;
import com.vanda.beivandalibnetwork.pojo.BrandData;
import com.vanda.beivandalibnetwork.pojo.BrandData.Brand;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.fragment.BaseListViewFragment;

public class FragmentShow extends BaseListViewFragment<Brand, BrandData> {

	// private int flag;
	private int compid = 0, recmnd_tp = 0, cat2_id = 0, free_deliver = 0,
			isnew = 0, ishot = 0, isasc = 0, isdesc = 0, maxorder = 0,
			maxprice = 0, minprice = 0;

	public static FragmentShow newInstance(int flag) {
		FragmentShow newFragment = new FragmentShow();
		Bundle bundle = new Bundle();
		bundle.putInt("flag", flag);
		newFragment.setArguments(bundle);
		return newFragment;
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
		View view = inflater.inflate(R.layout.fragment_listview, null);
		mArrayList = new ArrayList<BrandData>();
		mList = new ArrayList<BrandData>();
		mPullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.shopping_car_listview_list);
		mPullLoadArrayAdaper = new BrandFilterActivityAdapter(getActivity(),
				R.layout.search_item, mList);
		mPullToRefreshListView.setAdapter(mPullLoadArrayAdaper);
		return view;
	}

	@Override
	public void onResume() {
		startExecuteRequest(Method.GET);
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

}
