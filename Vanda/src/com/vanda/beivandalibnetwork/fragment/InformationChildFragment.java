package com.vanda.beivandalibnetwork.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.vanda.beivandalibnetwork.activity.FragmentMainActivity;
import com.vanda.beivandalibnetwork.adapter.ImagePagerAdapter;
import com.vanda.beivandalibnetwork.adapter.InformationAdapter;
import com.vanda.beivandalibnetwork.pojo.InformationIndication;
import com.vanda.beivandalibnetwork.pojo.SS;
import com.vanda.beivandalibnetwork.url.Urls;
import com.vanda.beivandalibnetwork.utils.HttpUtils;
import com.vanda.beivandalibnetwork.view.utils.AutoScrollViewPager;
import com.vanda.beivandalibnetworkdemo.R;
import com.vanda.vandalibnetwork.fragment.BaseSwipeRefreshFragment;
import com.vanda.vandalibnetwork.staticdata.StaticData;
import com.viewpagerindicator.CirclePageIndicator;

public class InformationChildFragment extends
		BaseSwipeRefreshFragment<SS, SS.List> {

	private int flag = 0;
	private int item = 0;
	private Context context;
	private AsyncTask<String, String, String> mAsyncTask;

	public static InformationChildFragment newInstance(Context context,
			int flag, int item) {
		InformationChildFragment newFragment = new InformationChildFragment();
		newFragment.flag = flag;
		newFragment.item = item;
		newFragment.context = context;
		return newFragment;
	}

	@Override
	public void onDestroy() {
		if (mAsyncTask != null)
			mAsyncTask.cancel(true);
		super.onDestroy();
	}

	@Override
	public void onResume() {
		Log.i("InformationChildFragment onResume", "onResume");
		super.onResume();
	}

	private ImagePagerAdapter advAdapter;
	private AutoScrollViewPager advViewPager;
	private CirclePageIndicator mIndicator;
	private View head;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.swipe_ref_layout, null);
		mArrayListInformation = new ArrayList<InformationIndication>();
		initArrayListData();
		initSwipeLayoutData(view, R.id.id_swipe_ly);
		initListViewData(view, R.id.id_listview);
		initPullLoadArrayAdaperData(new InformationAdapter(context,
				R.layout.information_list_item_shot, mList,
				mArrayListInformation));
		setOnPullDownRefresh((FragmentMainActivity) context);
		initData();
		startGetData();
		startExecuteRequest(Method.GET);
		return view;
	}

	private ArrayList<InformationIndication> mArrayListInformation;

	private void startGetData() {
		mAsyncTask = new AsyncTask<String, String, String>() {

			@Override
			protected String doInBackground(String... params) {
				String json = null;
				try {
					json = HttpUtils.get(Urls
							.getInformationImageUrl(19928 + item));
					Gson gson = new Gson();
					JsonParser parser = new JsonParser();
					JsonArray Jarray = parser.parse(json).getAsJsonArray();
					mArrayListInformation.clear();
					for (JsonElement obj : Jarray) {
						InformationIndication cse = gson.fromJson(obj,
								InformationIndication.class);
						mArrayListInformation.add(cse);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return "error";
				}
				return "ok";
			}

			@Override
			protected void onPostExecute(String result) {
				if (result.equals("ok")) {
					advAdapter = new ImagePagerAdapter(mArrayListInformation,
							context);
					head = LayoutInflater.from(context).inflate(
							R.layout.viewpager_indicator, null);
					RelativeLayout rl = (RelativeLayout) head
							.findViewById(R.id.viewpager_relayout);
					LayoutParams params = rl.getLayoutParams();
					params.width = StaticData.ScreenWidth;
					params.height = params.width * 14 / 29;
					advViewPager = (AutoScrollViewPager) head
							.findViewById(R.id.home_fragment_viewpager);
					advViewPager.setAdapter(advAdapter);
					advViewPager
							.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
					mIndicator = (CirclePageIndicator) head
							.findViewById(R.id.home_fragment_circlepageindicator);
					mIndicator.setViewPager(advViewPager);
					mListView.addHeaderView(head);
					advAdapter.notifyDataSetChanged();
				}
				super.onPostExecute(result);
			}
		}.execute();
	}

	@Override
	protected void processData(SS response) {
		super.processData(response);
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
		return Urls.getInformationUrl(flag, page);
	}

	@Override
	protected String getRequestUrl() {
		return Urls.getInformationUrl(flag, 1);
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
