package com.vanda.beivandalibnetwork.fragment;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request.Method;
import com.vanda.beivandalibnetwork.activity.FragmentMainActivity;
import com.vanda.beivandalibnetwork.adapter.SSAdapter;
import com.vanda.beivandalibnetwork.pojo.SS;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.fragment.BaseSwipeRefreshFragment;

public class ShowPicFragment extends BaseSwipeRefreshFragment<SS, SS.List> {// BasePageListFragment<SS,
																			// SS.List>
																			// {

	private int flag = 0;
	private int item = 0;
	private Context context;

	public static ShowPicFragment newInstance(Context context, int flag,
			int item) {
		ShowPicFragment newFragment = new ShowPicFragment();
		newFragment.flag = flag;
		newFragment.item = item;
		newFragment.context = context;
		return newFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.swipe_ref_layout, null);

		initArrayListData();
		initPullLoadArrayAdaperData(new SSAdapter(context,
				R.layout.listitem_shot, mList));
		initSwipeLayoutData(view, R.id.id_swipe_ly);
		initListViewData(view, R.id.id_listview);
		initData();
		setOnPullDownRefresh((FragmentMainActivity) context);
		startExecuteRequest(Method.GET);
		return view;
	}

	@Override
	protected void addArrayListData(SS response) {
		if (response != null && response.list != null) {
			setArrayListData(response.list);
			setDataItemCount(response.totalpage.totalpage);
		}
	}

	@Override
	protected String getRefDataUrl(int page, int size) {
		String url = null;
		switch (item) {
		case 0:
			url = Urls.getSSUrl(flag, page);
			break;
		case 1:
			url = Urls.getDaShiUrl(flag, page);
			break;
		case 2:
			url = Urls.getDaShiUrl(flag, page);
			break;
		}
		return url;
	}

	@Override
	protected String getRequestUrl() {
		String url = null;
		switch (item) {
		case 0:
			url = Urls.getSSUrl(flag, 1);
			break;
		case 1:
			url = Urls.getDaShiUrl(flag, 1);
			break;
		case 2:
			url = Urls.getDaShiUrl(flag, 1);
			break;
		}
		return url;
	}

	@Override
	protected Class<SS> getResponseDataClass() {
		return SS.class;
	}

	@Override
	protected Map<String, String> getParamMap() {
		return null;
	}

}
